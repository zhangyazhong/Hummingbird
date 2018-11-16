package cn.sissors.hummingbird.bean;

import cn.sissors.hummingbird.collect.feature.Parsable;

/**
 * @author zyz
 * @version 2018-10-14
 */
public class ResultUnit implements Parsable<ResultUnit> {
    private double result;
    private double error;

    public ResultUnit() {
    }

    public ResultUnit(double result, double error) {
        this.result = result;
        this.error = error;
    }

    @Override
    public ResultUnit parse(String text) {
        if (text.length() < 1) {
            return null;
        }
        String[] units = text.split(",");
        for (String unit : units) {
            String key = unit.split("=")[0].trim();
            String value = unit.split("=")[1].trim();
            if (key.equals("r") || key.equals("result")) {
                this.result = Double.valueOf(value);
            }
            if (key.equals("e") || key.equals("error")) {
                this.error = Double.valueOf(value);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("r=%.1f, e=%.1f", result, error);
    }
}
