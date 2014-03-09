package by.bsu.fpmi.arimax.model;

public class Moment {
    public static class Property {
        public static final String TIME = "time";
        public static final String VALUE = "value";
    }

    private double time;
    private double value;

    public Moment() {
    }

    public Moment(double value) {
        this.value = value;
    }

    public Moment(double time, double value) {
        this.time = time;
        this.value = value;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
