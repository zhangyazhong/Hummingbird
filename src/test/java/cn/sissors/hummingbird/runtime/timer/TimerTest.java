package cn.sissors.hummingbird.runtime.timer;

import org.junit.Test;

/**
 * @author zyz
 * @version 2018-10-29
 */
public class TimerTest {
    @Test
    public void testTimer() throws InterruptedException {
        Timer.create("test");
        Thread.sleep(2000);
        Timer.stop("test");
        System.out.println(Timer.toString("test"));
    }

    @Test
    public void testFormat() throws InterruptedException {
        System.out.println(Timer.format(8285232L));
    }

}