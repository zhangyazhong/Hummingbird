package cn.sissors.hummingbird.runtime.timer;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author zyz
 * @version 2019-04-03
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TimerManagerTest {
    @Test
    public void create() throws InterruptedException {
        TimerManager.create();
        TimerManager.start();
        Thread.sleep(100);
        TimerManager.stop();
        System.out.println(TimerManager.format());
        assertTrue(TimerManager.time() >= 100);
    }

    @Test
    public void create1() throws InterruptedException {
        TimerManager.create("create");
        Thread.sleep(100);
        TimerManager.stop("create");
        System.out.println(TimerManager.format("create"));
        assertTrue(TimerManager.time("create") >= 100);
    }

    @Test
    public void createWithoutStart() throws InterruptedException {
        TimerManager.createWithoutStart("create");
        TimerManager.start("create");
        Thread.sleep(100);
        TimerManager.stop("create");
        System.out.println(TimerManager.format("create"));
        assertTrue(TimerManager.time("create") >= 100);
    }

    @Test
    public void getLastTimer() throws InterruptedException {
        TimerManager.create("last");
        Thread.sleep(100);
        TimerManager.stop("last");
        TimerManager.create("last");
        Thread.sleep(200);
        TimerManager.stop("last");
        assertTrue(TimerManager.getLastTimer("last").time() >= 200);
    }

    @Test
    public void getLastTimer1() throws InterruptedException {
        TimerManager.create();
        Thread.sleep(100);
        TimerManager.stop();
        TimerManager.create();
        Thread.sleep(200);
        TimerManager.stop();
        assertTrue(TimerManager.getLastTimer().time() >= 200);
    }

    @Test
    public void getFirstTimer() throws InterruptedException {
        TimerManager.create("first");
        Thread.sleep(200);
        TimerManager.stop("first");
        TimerManager.create("first");
        Thread.sleep(100);
        TimerManager.stop("first");
        assertTrue(TimerManager.getFirstTimer("first").time() >= 200);
    }

    @Test
    public void getFirstTimer1() throws InterruptedException {
        TimerManager.create();
        Thread.sleep(200);
        TimerManager.stop();
        TimerManager.create();
        Thread.sleep(100);
        TimerManager.stop();
        assertTrue(TimerManager.getFirstTimer().time() >= 200);
    }

    @Test
    public void getTimerGroup() throws InterruptedException {
        TimerManager.create();
        Thread.sleep(200);
        TimerManager.stop();
        TimerManager.create();
        Thread.sleep(100);
        TimerManager.stop();
        assertEquals(9, TimerManager.getTimerGroup().size());
    }

    @Test
    public void getTimerGroup1() throws InterruptedException {
        TimerManager.create("group");
        Thread.sleep(200);
        TimerManager.stop("group");
        TimerManager.create("group");
        Thread.sleep(100);
        TimerManager.stop("group");
        assertEquals(2, TimerManager.getTimerGroup("group").size());
    }

    @Test
    public void agg() throws InterruptedException {
        TimerManager.create("agg");
        Thread.sleep(100);
        TimerManager.stop("agg");
        TimerManager.create("agg");
        Thread.sleep(200);
        TimerManager.stop("agg");
        assertTrue(TimerManager.agg("agg", TimerAggregation.avg) < 200
                && TimerManager.agg("agg", TimerAggregation.avg) > 100);
        assertTrue(TimerManager.agg("agg", TimerAggregation.max) >= 200);
        assertTrue(TimerManager.agg("agg", TimerAggregation.single) >= 200);
        assertTrue(TimerManager.agg("agg", TimerAggregation.sum) >= 300);
        assertTrue(TimerManager.agg("agg", TimerAggregation.min) < 200
                && TimerManager.agg("agg", TimerAggregation.min) >= 100);
    }

    @Test
    public void agg1() throws InterruptedException {
        TimerManager.create();
        Thread.sleep(200);
        TimerManager.stop();
        TimerManager.create();
        Thread.sleep(100);
        TimerManager.stop();
        assertTrue(TimerManager.agg(TimerAggregation.avg) < 200
                && TimerManager.agg(TimerAggregation.avg) > 100);
        assertTrue(TimerManager.agg(TimerAggregation.max) >= 200);
        assertTrue(TimerManager.agg(TimerAggregation.single) >= 100
                && TimerManager.agg(TimerAggregation.min) < 200);
        assertTrue(TimerManager.agg(TimerAggregation.sum) >= 300);
        assertTrue(TimerManager.agg(TimerAggregation.min) < 200
                && TimerManager.agg(TimerAggregation.min) >= 100);
    }
}