package cn.sissors.hummingbird.bean;

import cn.sissors.hummingbird.collect.feature.Parsable;

/**
 * @author zyz
 * @version 2018-10-14
 */
public class ResultTimeline implements Parsable<ResultTimeline> {
    private int year;
    private int month;
    private int day;

    public ResultTimeline() {
        this.year = 1900;
        this.month = 1;
        this.day = 1;
    }

    public ResultTimeline(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public ResultTimeline parse(String text) {
        if (text.length() < 1) {
            return this;
        }
        this.year = Integer.valueOf(text.split("-")[0].trim());
        this.month = Integer.valueOf(text.split("-")[1].trim());
        this.day = Integer.valueOf(text.split("-")[2].trim());
        return this;
    }

    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }
}
