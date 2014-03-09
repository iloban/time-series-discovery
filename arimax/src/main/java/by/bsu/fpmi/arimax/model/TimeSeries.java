package by.bsu.fpmi.arimax.model;

import java.util.List;

public class TimeSeries {
    private final List<Moment> moments;
    private String title;

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

    public void setTitle(String title) {
        this.title = title;
    }
}
