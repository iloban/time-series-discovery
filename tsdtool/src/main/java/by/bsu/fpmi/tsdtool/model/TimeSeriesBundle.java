package by.bsu.fpmi.tsdtool.model;

import by.bsu.fpmi.arimax.model.TimeSeries;

public final class TimeSeriesBundle {
    private final TimeSeries sourceTimeSeries;
    private final TimeSeries acfSeries;
    private final TimeSeries pacfSeries;

    public TimeSeriesBundle(TimeSeries sourceTimeSeries, TimeSeries acfSeries, TimeSeries pacfSeries) {
        this.sourceTimeSeries = sourceTimeSeries;
        this.acfSeries = acfSeries;
        this.pacfSeries = pacfSeries;
    }

    public TimeSeries getSourceTimeSeries() {
        return sourceTimeSeries;
    }

    public TimeSeries getAcfSeries() {
        return acfSeries;
    }

    public TimeSeries getPacfSeries() {
        return pacfSeries;
    }
}
