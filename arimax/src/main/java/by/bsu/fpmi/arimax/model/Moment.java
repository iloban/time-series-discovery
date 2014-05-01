package by.bsu.fpmi.arimax.model;

public final class Moment {
    public static final String TIME_FIELD = "time";
    public static final String VALUE_FIELD = "value";

    private double time;
    private double value;

    public Moment() {
    }

    public Moment(double time, double value) {
        this.time = time;
        this.value = value;
    }

    public static Moment valueOf(Moment moment) {
        return new Moment(moment.getTime(), moment.getValue());
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
