package cn.sissors.hummingbird.runtime.logger;

import cn.sissors.hummingbird.runtime.namespace.NameManager;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Logger provides a series of simple interfaces to build logs. Currently, it supports 4 levels:
 * <ul>
 * <li>DEBUG</li>
 * <li>INFO</li>
 * <li>WARN</li>
 * <li>ERROR</li>
 * </ul>
 *
 * @author zyz
 * @version 2018-11-28
 */
public class Logger {
    /**
     * A series of logging levels.
     */
    public enum Level {
        ALL(org.apache.logging.log4j.Level.ALL),
        TRACE(org.apache.logging.log4j.Level.TRACE),
        DEBUG(org.apache.logging.log4j.Level.DEBUG),
        INFO(org.apache.logging.log4j.Level.INFO),
        WARN(org.apache.logging.log4j.Level.WARN),
        ERROR(org.apache.logging.log4j.Level.ERROR),
        FATAL(org.apache.logging.log4j.Level.FATAL);

        private org.apache.logging.log4j.Level level;

        Level(org.apache.logging.log4j.Level level) {
            this.level = level;
        }

        public org.apache.logging.log4j.Level log4j() {
            return level;
        }
    }

    private static org.apache.logging.log4j.Logger logger;
    private final static String LOGGER_CLASSNAME = Logger.class.getCanonicalName();

    private static boolean PROGRAMMATICALLY_BUILT = false;
    private static String LOG_OUTPUT_DIRECTORY = "null";
    private static String LOG_OUTPUT_FILENAME = "hummingbird";
    private static Level CONSOLE_LEVEL = Level.INFO;
    private static Level GLOBAL_LEVEL = Level.DEBUG;

    static {
        initialize();
    }

    /**
     * Build logger programmatically using default location and logging level.
     */
    public static void build() {
        String LOG_OUTPUT_DIRECTORY_DEFAULT = "/tmp/hummingbird/log/";
        if (PROGRAMMATICALLY_BUILT && !LOG_OUTPUT_DIRECTORY.equals(LOG_OUTPUT_DIRECTORY_DEFAULT)) {
            LOG_OUTPUT_DIRECTORY = LOG_OUTPUT_DIRECTORY_DEFAULT;
            programmaticallyBuild();
        }
    }

    /**
     * Set output directory of log file and build logger programmatically.
     *
     * @param outputDirectory the output directory of log file
     * @param outputFilename  the output file name of log file
     */
    public static void build(@NotNull String outputDirectory, @NotNull String outputFilename) {
        if (PROGRAMMATICALLY_BUILT && !LOG_OUTPUT_DIRECTORY.equals(outputDirectory)) {
            LOG_OUTPUT_DIRECTORY = outputDirectory;
            LOG_OUTPUT_FILENAME = outputFilename;
            programmaticallyBuild();
        }
    }

    /**
     * Set log level for console and global and build logger programmatically.
     *
     * @param console the log level for console
     * @param global  the log level for global
     */
    public static void build(Level console, Level global) {
        String LOG_OUTPUT_DIRECTORY_DEFAULT = "/tmp/hummingbird/log/";
        if (PROGRAMMATICALLY_BUILT && (!LOG_OUTPUT_DIRECTORY.equals(LOG_OUTPUT_DIRECTORY_DEFAULT)
                || (console != null && CONSOLE_LEVEL != console) || (global != null && GLOBAL_LEVEL != global))) {
            LOG_OUTPUT_DIRECTORY = LOG_OUTPUT_DIRECTORY_DEFAULT;
            CONSOLE_LEVEL = console != null ? console : CONSOLE_LEVEL;
            GLOBAL_LEVEL = global != null ? global : GLOBAL_LEVEL;
            programmaticallyBuild();
        }
    }

    /**
     * Set output directory of log file and log level for console and global. Finally, build logger programmatically.
     *
     * @param outputDirectory the output directory of log file
     * @param outputFilename  the output file name of log file
     * @param console         the log level for console
     * @param global          the log level for global
     */
    public static void build(@NotNull String outputDirectory, @NotNull String outputFilename, Level console, Level global) {
        if (PROGRAMMATICALLY_BUILT
                && (!LOG_OUTPUT_DIRECTORY.equals(outputDirectory)
                || (console != null && CONSOLE_LEVEL != console) || (global != null && GLOBAL_LEVEL != global))) {
            LOG_OUTPUT_DIRECTORY = outputDirectory;
            LOG_OUTPUT_FILENAME = outputFilename;
            CONSOLE_LEVEL = console != null ? console : CONSOLE_LEVEL;
            GLOBAL_LEVEL = global != null ? global : GLOBAL_LEVEL;
            programmaticallyBuild();
        }
    }

    private static void initialize() {
        List<String> configs = ImmutableList.of(
                "log4j2-test.json", "log4j2-test.jsn", "log4j2-test.xml",
                "log4j2.json", "log4j2.jsn", "log4j2.xml"
        );
        for (String config : configs) {
            File configFile = new File(Logger.class.getResource("/").getPath() + config);
            if (configFile.exists()) {
                logger = LogManager.getLogger(Logger.class);
                PROGRAMMATICALLY_BUILT = false;
                return;
            }
        }
        PROGRAMMATICALLY_BUILT = true;
    }

    private static void programmaticallyBuild() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(org.apache.logging.log4j.Level.OFF);
        builder.setMonitorInterval("600");
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%p] - %m%n");
        ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
                .addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"));
        String consoleAppenderName = NameManager.uniqueName("Stdout");
        String singleFileAppenderName = NameManager.uniqueName("SingleRuntime");
        String rollingFileAppenderName = NameManager.uniqueName("RollingFile");
        String asyncAppenderName = NameManager.uniqueName("Async");
        AppenderComponentBuilder consoleAppender = builder.newAppender(consoleAppenderName, "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
                .add(layoutBuilder)
                .add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.DENY).addAttribute("level", CONSOLE_LEVEL.log4j()));
        AppenderComponentBuilder singleFileAppender = builder.newAppender(singleFileAppenderName, "File")
                .addAttribute("fileName", LOG_OUTPUT_DIRECTORY + "/log.out")
                .addAttribute("append", false)
                .add(layoutBuilder);
        AppenderComponentBuilder rollingFileAppender = builder.newAppender(rollingFileAppenderName, "RollingRandomAccessFile")
                .addAttribute("fileName", LOG_OUTPUT_DIRECTORY + "/" + LOG_OUTPUT_FILENAME + ".log")
                .addAttribute("filePattern", LOG_OUTPUT_DIRECTORY + "/" + LOG_OUTPUT_FILENAME + "-%d{yyyy-MM-dd}_%i.log")
                .add(layoutBuilder)
                .addComponent(triggeringPolicy)
                .addComponent(builder.newComponent("DefaultRolloverStrategy").addAttribute("max", 20));
        AppenderComponentBuilder asyncAppender = builder.newAppender(asyncAppenderName, "Async")
                .addComponent(builder.newAppenderRef(singleFileAppenderName))
                .addComponent(builder.newAppenderRef(rollingFileAppenderName));

        builder.add(consoleAppender).add(singleFileAppender).add(rollingFileAppender).add(asyncAppender);
        builder.add(builder.newRootLogger(GLOBAL_LEVEL.log4j())
                .add(builder.newAppenderRef(consoleAppenderName))
                .add(builder.newAppenderRef(singleFileAppenderName))
                .add(builder.newAppenderRef(rollingFileAppenderName))
        );
        Configurator.initialize(builder.build());
        logger = LogManager.getLogger(Logger.class);
    }

    private static String attachInvoker(Object msg) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int tracePosition = 1; tracePosition < elements.length; tracePosition++) {
            if (!LOGGER_CLASSNAME.equals(elements[tracePosition].getClassName())) {
                StackTraceElement invoker = elements[tracePosition];
                String invokeClassName = StringUtils.substringAfterLast(invoker.getClassName(), ".");
                String invokeMethodName = invoker.getMethodName();
                int invokeLineNumber = invoker.getLineNumber();
                return String.format("[%s.%s:%d] %s", invokeClassName, invokeMethodName, invokeLineNumber, msg);
            }
        }
        return msg.toString();
    }

    private static List<String> prettify(Object msg) {
        return Arrays.asList(msg.toString().replaceAll("\r\n", "\n").split("\n"));
    }

    /**
     * Logs a message object with the given level.
     *
     * @param level the logging level
     * @param msg   the message to log
     */
    public static void log(Level level, Object msg) {
        List<String> messages = prettify(msg);
        for (String message : messages) {
            logger.log(level.log4j(), attachInvoker(message));
        }
    }

    /**
     * Logs the specified format string and arguments with the given level.
     *
     * @param level the logging level
     * @param msg   the message to log
     * @param args  arguments referenced by the format specifiers in the format string
     */
    public static void log(Level level, Object msg, Object... args) {
        List<String> messages = prettify(String.format(msg.toString(), args));
        for (String message : messages) {
            logger.log(level.log4j(), attachInvoker(message));
        }
    }

    /**
     * Logs a message object with {@link Level#TRACE} level.
     *
     * @param msg the message to log
     */
    public static void trace(Object msg) {
        log(Level.TRACE, msg);
    }

    /**
     * Logs the specified format string and arguments with {@link Level#TRACE} level.
     *
     * @param msg  the message to log
     * @param args arguments referenced by the format specifiers in the format string
     */
    public static void trace(@NotNull Object msg, Object... args) {
        log(Level.TRACE, msg, args);
    }

    /**
     * Logs a message object with {@link Level#DEBUG} level.
     *
     * @param msg the message to log
     */
    public static void debug(Object msg) {
        log(Level.DEBUG, msg);
    }

    /**
     * Logs the specified format string and arguments with {@link Level#DEBUG} level.
     *
     * @param msg  the message to log
     * @param args arguments referenced by the format specifiers in the format string
     */
    public static void debug(@NotNull Object msg, Object... args) {
        log(Level.DEBUG, msg, args);
    }

    /**
     * Logs a message object with {@link Level#INFO} level.
     *
     * @param msg the message to log
     */
    public static void info(Object msg) {
        log(Level.INFO, msg);
    }

    /**
     * Logs the specified format string and arguments with {@link Level#INFO} level.
     *
     * @param msg  the message to log
     * @param args arguments referenced by the format specifiers in the format string
     */
    public static void info(@NotNull Object msg, Object... args) {
        log(Level.INFO, msg, args);
    }

    /**
     * Logs a message object with {@link Level#WARN} level.
     *
     * @param msg the message to log
     */
    public static void warn(Object msg) {
        log(Level.WARN, msg);
    }

    /**
     * Logs the specified format string and arguments with {@link Level#WARN} level.
     *
     * @param msg  the message to log
     * @param args arguments referenced by the format specifiers in the format string
     */
    public static void warn(@NotNull Object msg, Object... args) {
        log(Level.WARN, msg, args);
    }

    /**
     * Logs a message object with {@link Level#ERROR} level.
     *
     * @param msg the message to log
     */
    public static void error(Object msg) {
        log(Level.ERROR, msg);
    }

    /**
     * Logs the specified format string and arguments with {@link Level#ERROR} level.
     *
     * @param msg  the message to log
     * @param args arguments referenced by the format specifiers in the format string
     */
    public static void error(@NotNull Object msg, Object... args) {
        log(Level.ERROR, msg, args);
    }

    /**
     * Logs a message object with {@link Level#FATAL} level.
     *
     * @param msg the message to log
     */
    public static void fatal(Object msg) {
        log(Level.FATAL, msg);
    }

    /**
     * Logs the specified format string and arguments with {@link Level#FATAL} level.
     *
     * @param msg  the message to log
     * @param args arguments referenced by the format specifiers in the format string
     */
    public static void fatal(@NotNull Object msg, Object... args) {
        log(Level.FATAL, msg, args);
    }
}
