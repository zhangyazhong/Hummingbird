package cn.sissors.hummingbird.runtime.timer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author zyz
 * @version 2018-11-18
 */
public class TimerRecordTest {
    @Test
    public void testReport() {
        sleep(2000L);
        System.out.println(Timer.getTime("sleep_period"));
        assertTrue(Timer.getTime("sleep_period") >= 2000);
    }

    @TimerRecord("sleep_period")
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
