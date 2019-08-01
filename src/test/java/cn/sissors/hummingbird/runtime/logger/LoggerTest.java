package cn.sissors.hummingbird.runtime.logger;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * @author zyz
 * @version 2018-11-28
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoggerTest {
    @Test
    public void log() {
        Logger.log(Logger.Level.DEBUG, "test debug");
        assertTrue(true);
    }

    @Test
    public void log1() {
        Logger.log(Logger.Level.DEBUG, "test debug %d", 2);
        assertTrue(true);
    }

    @Test
    public void trace() {
        Logger.trace("test trace");
        assertTrue(true);
    }

    @Test
    public void trace1() {
        Logger.trace("test trace %d", 2);
        assertTrue(true);
    }

    @Test
    public void debug() {
        Logger.debug("test debug");
        assertTrue(true);
    }

    @Test
    public void debug1() {
        Logger.debug("test debug %d", 2);
        assertTrue(true);
    }

    @Test
    public void info() {
        Logger.info("test info");
        assertTrue(true);
    }

    @Test
    public void info1() {
        Logger.info("test info %d", 2);
        assertTrue(true);
    }

    @Test
    public void warn() {
        Logger.warn("test warn");
        assertTrue(true);
    }

    @Test
    public void warn1() {
        Logger.warn("test warn %d", 2);
        assertTrue(true);
    }

    @Test
    public void error() {
        Logger.error("test error");
        assertTrue(true);
    }

    @Test
    public void error1() {
        Logger.error("test error %d", 2);
        assertTrue(true);
    }

    @Test
    public void fatal() {
        Logger.fatal("test fatal");
        assertTrue(true);
    }

    @Test
    public void fatal1() {
        Logger.fatal("test fatal %d", 2);
        assertTrue(true);
    }

    @Test
    public void testNewLine() {
        Logger.error("test error\ntest error2");
        assertTrue(true);
    }
}