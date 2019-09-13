package cn.sissors.hummingbird.runtime.logger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author zyz
 * @version 2018-11-29
 */
@Ignore
public class LoggerTest4 {
    @Before
    public void init() {
        Logger.build("/tmp/hummingbird/log4/", "hummingbird", Logger.Level.WARN, null);
    }

    @Test
    public void log() {
        Logger.log(Logger.Level.DEBUG, "test4 debug");
        Logger.log(Logger.Level.WARN, "test4 warn");
        assertTrue(true);
    }
}