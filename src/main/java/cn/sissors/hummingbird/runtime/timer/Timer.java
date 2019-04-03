package cn.sissors.hummingbird.runtime.timer;

/**
 * Timer is a basic class to record time.
 * Its main attributes are {@code startTime} and {@code stopTime}, to record when started and stopped respectively.
 *
 * @author zyz
 * @version 2018-10-23
 */
public class Timer {
    private String name;
    private Long startTime;
    private Long stopTime;

    protected Timer(String name) {
        this.name = name;
    }

    /**
     * Start the timer.
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Stop the timer.
     */
    public void stop() {
        this.stopTime = System.currentTimeMillis();
    }

    /**
     * Get recorded time of a timer.
     *
     * @return the time accurate to milliseconds
     */
    public Long time() {
        if (this.stopTime == null || this.stopTime <= 0) {
            return -1L;
        }
        return this.stopTime - this.startTime;
    }

    /**
     * Get the name of timer.
     *
     * @return the name of timer
     */
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return format(time());
    }

    /**
     * Format the time into human readable style.
     *
     * <p>The default format is <i>HH:mm:ss.SSS</i>
     *
     * @param time the time
     * @return the string in human readable style
     */
    public static String format(long time) {
        long HH = time / 3600000;
        long mm = (time % 3600000) / 60000;
        long ss = (time % 60000) / 1000;
        long SSS = time % 1000;
        if (HH > 0) {
            return String.format("%d:%02d:%02d.%03d", HH, mm, ss, SSS);
        } else if (mm > 0) {
            return String.format("%d:%02d.%03d", mm, ss, SSS);
        } else {
            return String.format("%d.%03d", ss, SSS);
        }
    }
}
