package cn.sissors.hummingbird.runtime.timer;

import cn.sissors.hummingbird.annotions.CanIgnoreReturnValue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * TimerManager is a convenient tool to manage {@link cn.sissors.hummingbird.runtime.timer.Timer}.
 *
 * <p>In TimerManager, timers are identified by name.
 * Timers with the same name are merged into the same group to support aggregation function.
 * Besides, it contains a series of methods to manage the lifecycle of timer.
 *
 * @author zyz
 * @version 2019-04-02
 */
public class TimerManager {
    final static String EMPTY_NAME = "";

    private static Map<String, List<Timer>> timerMap;

    static {
        timerMap = Maps.newConcurrentMap();
    }

    /**
     * Create a timer with an empty name and start after creation.
     *
     * @return a timer
     */
    @CanIgnoreReturnValue
    public static Timer create() {
        return create(EMPTY_NAME);
    }

    /**
     * Create a timer with a given name and start after creation.
     *
     * @param name the name of timer
     * @return a timer
     */
    @CanIgnoreReturnValue
    public static Timer create(String name) {
        Timer timer = createWithoutStart(name);
        timer.start();
        return timer;
    }

    /**
     * Create a timer with a given name.
     *
     * @param name the name of timer
     * @return a timer
     */
    @CanIgnoreReturnValue
    public static Timer createWithoutStart(String name) {
        Timer timer = new Timer(name);
        if (!timerMap.containsKey(name)) {
            timerMap.put(name, Lists.newArrayList());
        }
        timerMap.get(name).add(timer);
        return timer;
    }

    /**
     * Start a timer with the default name {@code EMPTY_NAME}.
     *
     * @return the recorded time of the timer
     */
    @CanIgnoreReturnValue
    public static Timer start() {
        return start(EMPTY_NAME);
    }

    /**
     * Start a timer with the specified name.
     *
     * @param name the specified name
     * @return the recorded time of the timer
     */
    @CanIgnoreReturnValue
    public static Timer start(String name) {
        Timer timer = getLastTimer(name);
        timer.start();
        return timer;
    }

    /**
     * Stop a timer with the default name {@code EMPTY_NAME}.
     *
     * @return the recorded time of the timer
     */
    @CanIgnoreReturnValue
    public static Timer stop() {
        return stop(EMPTY_NAME);
    }

    /**
     * Stop a timer with the specified name.
     *
     * @param name the specified name
     * @return the recorded time of the timer
     */
    @CanIgnoreReturnValue
    public static Timer stop(String name) {
        Timer timer = getLastTimer(name);
        timer.stop();
        return timer;
    }

    /**
     * Get recorded time of a timer with the default name.
     *
     * @return the recorded time of the timer
     */
    @CanIgnoreReturnValue
    public static Long time() {
        return time(EMPTY_NAME);
    }

    /**
     * Get recorded time of a timer with the specified name.
     *
     * @param name the specified name
     * @return the recorded time of the timer
     */
    @CanIgnoreReturnValue
    public static Long time(String name) {
        Timer timer = getLastTimer(name);
        return timer.time();
    }

    /**
     * Format the time with the default name into human readable style .
     *
     * <p>The default format is <i>HH:mm:ss.SSS</i>
     *
     * @return the string in human readable style
     */
    @CanIgnoreReturnValue
    public static String format() {
        return format(EMPTY_NAME);
    }

    /**
     * Format the time with the specified name into human readable style .
     *
     * <p>The default format is <i>HH:mm:ss.SSS</i>
     *
     * @param name the specified name
     * @return the string in human readable style
     */
    @CanIgnoreReturnValue
    public static String format(String name) {
        Timer timer = getLastTimer(name);
        return timer.toString();
    }

    /**
     * Format the time with the specified name into human readable style .
     *
     * <p>The default format is <i>HH:mm:ss.SSS</i>
     *
     * @param time the time
     * @return the string in human readable style
     */
    @CanIgnoreReturnValue
    public static String format(long time) {
        return Timer.format(time);
    }

    /**
     * Get the newest created timer with the default name.
     *
     * @return the newest created timer
     */
    public static Timer getLastTimer() {
        return getLastTimer(EMPTY_NAME);
    }

    /**
     * Get the newest created timer with the specified name.
     *
     * @param name the specified name
     * @return the newest created timer
     */
    public static Timer getLastTimer(String name) {
        List<Timer> timers = timerMap.get(name);
        return timers.get(timers.size() - 1);
    }

    /**
     * Get the oldest created timer with the default name.
     *
     * @return the oldest created timer
     */
    public static Timer getFirstTimer() {
        return getFirstTimer(EMPTY_NAME);
    }

    /**
     * Get the oldest created timer with the specified name.
     *
     * @param name the specified name
     * @return the oldest created timer
     */
    public static Timer getFirstTimer(String name) {
        List<Timer> timers = timerMap.get(name);
        return timers.get(0);
    }

    /**
     * Get the group of timers with the default name.
     *
     * @return the group of timers with the same name
     */
    public static List<Timer> getTimerGroup() {
        return getTimerGroup(EMPTY_NAME);
    }

    /**
     * Get the group of timers with the specified name.
     *
     * @param name the specified name
     * @return the group of timers with the same name
     */
    public static List<Timer> getTimerGroup(String name) {
        return timerMap.get(name);
    }

    /**
     * Get the aggregation value of the default group of timers.
     *
     * @param function aggregation function defined in {@link cn.sissors.hummingbird.runtime.timer.TimerAggregation}
     * @return the aggregation result
     */
    public static Long agg(TimerAggregation function) {
        return agg(EMPTY_NAME, function);
    }

    /**
     * Get the aggregation value of a group of timers.
     *
     * @param name     the specified name of group
     * @param function aggregation function defined in {@link cn.sissors.hummingbird.runtime.timer.TimerAggregation}
     * @return the aggregation result
     */
    public static Long agg(String name, TimerAggregation function) {
        List<Timer> timers = timerMap.get(name);
        switch (function) {
            case single:
                return timers.stream().filter(timer -> timer != null && timer.time() >= 0).mapToLong(Timer::time).reduce((time1, time2) -> time2).orElse(0);
            case avg:
                return (long) timers.stream().filter(timer -> timer != null && timer.time() >= 0).mapToLong(Timer::time).average().orElse(0);
            case sum:
                return timers.stream().filter(timer -> timer != null && timer.time() >= 0).mapToLong(Timer::time).sum();
            case max:
                return timers.stream().filter(timer -> timer != null && timer.time() >= 0).mapToLong(Timer::time).max().orElse(0);
            case min:
                return timers.stream().filter(timer -> timer != null && timer.time() >= 0).mapToLong(Timer::time).min().orElse(0);
        }
        return -1L;
    }
}
