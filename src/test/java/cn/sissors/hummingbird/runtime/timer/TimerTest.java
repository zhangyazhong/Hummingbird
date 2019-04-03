package cn.sissors.hummingbird.runtime.timer;

import cn.sissors.hummingbird.runtime.namespace.NameManager;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author zyz
 * @version 2018-10-29
 */
public class TimerTest {
    @Test
    public void testTimer() throws InterruptedException {
        Timer timer = new Timer("test");
        timer.start();
        Thread.sleep(2000);
        timer.stop();
        System.out.println(timer.toString());
        assertTrue(timer.time() >= 2000);
    }

    @Test
    public void testFormat() {
        System.out.println(TimerManager.format(8285232L));
        assertEquals(Timer.format(8285232L), "2:18:05.232");
    }

    @Test
    public void testFormat2() {
        System.out.println(TimerManager.format(285232L));
        assertEquals(Timer.format(285232L), "4:45.232");
    }

    @Test
    public void testTimer0() throws InterruptedException {
        Timer timer = new Timer(NameManager.uniqueName());
        Thread.sleep(1000);
        timer.start();
        Thread.sleep(1000);
        timer.stop();
        System.out.println(Timer.format(timer.time()));
        assertTrue(timer.time() >= 1000 && timer.time() <= 2000);
    }
}