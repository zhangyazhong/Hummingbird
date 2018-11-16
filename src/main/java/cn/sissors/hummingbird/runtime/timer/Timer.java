package cn.sissors.hummingbird.runtime.timer;

import cn.sissors.hummingbird.annotions.CanIgnoreReturnValue;
import cn.sissors.hummingbird.runtime.namespace.NameManager;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Timer is a tool to record time cost for a piece of code.
 *
 * @author zyz
 * @version 2018-10-23
 */
public class Timer {
    private static Map<String, Timer> timerMap;
    private String name;
    private Long startTime;
    private Long stopTime;

    static {
        timerMap = Maps.newConcurrentMap();
    }

    private Timer(String name) {
        this.name = name;
    }

    /**
     * Create a timer with a randomly generated name.
     *
     * @return a timer
     */
    @CanIgnoreReturnValue
    public static Timer create() {
        return create(NameManager.uniqueName());
    }

    /**
     * Create a timer with a given name.
     *
     * @param name the name of timer
     * @return a timer
     */
    @CanIgnoreReturnValue
    public static Timer create(String name) {
        Timer timer = new Timer(name);
        timerMap.put(timer.name, timer);

        Timer.start(timer.name);
        return timer;
    }

    /**
     * Start a timer with the specified name.
     *
     * <p>Actually, a timer will be started automatically once it has been created.
     *
     * @param name the specified name
     */
    public static void start(String name) {
        Timer timer = timerMap.get(name);
        timer.startTime = System.currentTimeMillis();
    }

    /**
     * Stop a timer with the specified name.
     *
     * @param name the specified name
     * @return the recorded time of the timer
     */
    @CanIgnoreReturnValue
    public static Long stop(String name) {
        Timer timer = timerMap.get(name);
        timer.stopTime = System.currentTimeMillis();
        return timer.stopTime - timer.startTime;
    }

    /**
     * Get recorded time of a timer with the specified name.
     *
     * @param name the timer's name
     * @return the time accurate to milliseconds
     */
    public static Long getTime(String name) {
        Timer timer = timerMap.get(name);
        return timer.getTime();
    }

    /**
     * Format the time with the specified name into human readable style.
     *
     * <p>The default format is <i>HH:mm:ss.SSS</i>
     *
     * @param name the timer's name
     * @return the string in human readable style
     */
    public static String toString(String name) {
        Timer timer = timerMap.get(name);
        return timer.toString();
    }

    /**
     * Get recorded time of a timer.
     *
     * @return the time accurate to milliseconds
     */
    public Long getTime() {
        if (this.stopTime == null || this.stopTime <= 0) {
            stop(this.name);
        }
        return this.stopTime - this.startTime;
    }

    /**
     * Get the name of timer.
     *
     * @return the name of timer
     */
    public String getName() {
        return name;
    }

    /**
     * Format the time into human readable style.
     *
     * <p>The default format is <i>HH:mm:ss.SSS</i>
     *
     * @param time the time
     * @return the string in human readable style
     */
    public static String format(long time) {
        long HH = time / 3600000;
        long mm = (time % 3600000) / 60000;
        long ss = (time % 60000) / 1000;
        long SSS = time % 1000;
        if (HH > 0) {
            return String.format("%d:%02d:%02d.%03d", HH, mm, ss, SSS);
        } else if (mm > 0) {
            return String.format("%d:%02d.%03d", mm, ss, SSS);
        } else {
            return String.format("%d.%03d", ss, SSS);
        }
    }

    public String toString() {
        return Timer.format(getTime());
    }
}
