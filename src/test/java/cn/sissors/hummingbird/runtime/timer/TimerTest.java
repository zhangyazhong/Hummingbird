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
        Timer.create("test");
        Thread.sleep(2000);
        Timer.stop("test");
        System.out.println(Timer.toString("test"));
        assertTrue(Timer.getTime("test") >= 2000);
    }

    @Test
    public void testFormat() {
        System.out.println(Timer.format(8285232L));
        assertEquals(Timer.format(8285232L), "2:18:05.232");
    }

    @Test
    public void testTimer0() throws InterruptedException {
        Timer timer = Timer.create(NameManager.uniqueName());
        Thread.sleep(1000);
        Timer.start(timer.name());
        Thread.sleep(1000);
        System.out.println(Timer.format(timer.time()));
        assertTrue(timer.time() >= 1000 && Timer.getTime() <= 2000);

    }

    @Test
    public void testTimer1() throws InterruptedException {
        Timer.create();
        Thread.sleep(1000);
        Timer.start();
        Thread.sleep(1000);
        System.out.println(Timer.format(Timer.getTime()));
        assertTrue(Timer.getTime() >= 1000 && Timer.getTime() <= 2000);
    }
}