package by.bsu.fpmi.arimax.model;

import java.util.ArrayList;
import java.util.List;

public final class TimeSeries {
    private final String title;
    private final List<Moment> moments;
    private final List<Double> acf = new ArrayList<>();
    private final List<Double> pacf = new ArrayList<>();

    public TimeSeries(List<Moment> moments, String title) {
        this.moments = moments;
        this.title = title;
    }

    public void add(double time, double value) {
        moments.add(new Moment(time, value));
    }

    public List<Moment> getMoments() {
        return moments;
    }

    public String getTitle() {
        return title;
    }

    public List<Double> getAcf() {
        return acf;
    }

    public List<Double> getPacf() {
        return pacf;
    }
}
