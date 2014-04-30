package by.bsu.fpmi.tsdtool.util;

import by.bsu.fpmi.arimax.model.Moment;
import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.YIntervalRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.Layer;

import java.awt.Color;
import java.util.List;

import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createTitledBorder;

public final class TimeSeriesUtils {
    private static final Color RANGE_COLOR = new Color(128, 128, 255);
    public static final IntervalMarker INTERVAL_MARKER = new IntervalMarker(-1.0, 1.0, RANGE_COLOR);

    private TimeSeriesUtils() {
        throw new AssertionError();
    }

    public static ChartPanel createChartPanel(TimeSeries timeSeries) {
        XYDataset dataset = TimeSeriesUtils.createTimeSeriesDataset(timeSeries);
        return getChartPanel(timeSeries, dataset, null);
    }

    public static ChartPanel createYIntervalChartPanel(TimeSeries timeSeries) {
        XYDataset dataset = TimeSeriesUtils.createTimeSeriesYIntervalDataset(timeSeries);
        return getChartPanel(timeSeries, dataset, new YIntervalRenderer());
    }

    private static ChartPanel getChartPanel(TimeSeries timeSeries, XYDataset dataset, XYItemRenderer renderer) {
        JFreeChart chart = TimeSeriesUtils.createTimeSeriesChart(dataset, renderer);
        ChartPanel panel = new ChartPanel(chart);
        panel.setBorder(createTitledBorder(createEtchedBorder(), timeSeries.getTitle()));
        return panel;
    }

    public static XYDataset createTimeSeriesDataset(TimeSeries timeSeries) {
        XYSeries series = new XYSeries(StringUtils.EMPTY, false, true);
        for (Moment moment : timeSeries.getMoments()) {
            series.add(moment.getTime(), moment.getValue());
        }
        return new XYSeriesCollection(series);
    }

    public static XYDataset createTimeSeriesYIntervalDataset(TimeSeries timeSeries) {
        YIntervalSeries series = new YIntervalSeries(StringUtils.EMPTY, false, true);
        List<Moment> moments = timeSeries.getMoments();
        for (Moment moment : moments.subList(0, Math.min(moments.size(), 100))) {
            series.add(moment.getTime(), moment.getValue(), Math.min(0, moment.getValue()),
                    Math.max(0, moment.getValue()));
        }

        double intervalValue = 1.96 / Math.sqrt(moments.size());
        INTERVAL_MARKER.setStartValue(-intervalValue);
        INTERVAL_MARKER.setEndValue(intervalValue);

        YIntervalSeriesCollection collection = new YIntervalSeriesCollection();
        collection.addSeries(series);
        return collection;
    }

    public static JFreeChart createTimeSeriesChart(XYDataset dataset, XYItemRenderer renderer) {
        JFreeChart chart = ChartFactory
                .createXYLineChart(null, MessageUtils.getMessage("ui.dialog.timeSeriesDialog.chart.x"),
                        MessageUtils.getMessage("ui.dialog.timeSeriesDialog.chart.y"), dataset,
                        PlotOrientation.VERTICAL, false, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        if (renderer != null) {
            plot.setRenderer(renderer);
            plot.addRangeMarker(INTERVAL_MARKER, Layer.BACKGROUND);
        }
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(true);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(true);
        return chart;
    }

    public static void updateYIntervalChartPanel(ChartPanel chartPanel, TimeSeries timeSeries) {
        chartPanel.getChart().getXYPlot().setDataset(createTimeSeriesYIntervalDataset(timeSeries));
    }
}
