package cn.sissors.hummingbird.runtime.logger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author zyz
 * @version 2018-11-29
 */
@Ignore
public class LoggerTest3 {
    @Before
    public void init() {
        Logger.build("/tmp/hummingbird/log3/", "hummingbird");
    }

    @Test
    public void log() {
        Logger.log(Logger.Level.DEBUG, "test3 debug");
        assertTrue(true);
    }
}