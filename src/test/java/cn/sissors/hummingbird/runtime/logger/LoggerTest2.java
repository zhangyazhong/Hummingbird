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
public class LoggerTest2 {
    @Before
    public void init() {
        Logger.build();
    }

    @Test
    public void log() {
        Logger.log(Logger.Level.DEBUG, "test2 debug");
        assertTrue(true);
    }
}