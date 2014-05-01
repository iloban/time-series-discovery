package by.bsu.fpmi.tsdtool.util;

import by.bsu.fpmi.arimax.model.Moment;
import by.bsu.fpmi.arimax.model.TimeSeries;
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

import java.awt.Color;
import java.util.List;

import static by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils.getMessage;
import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createTitledBorder;

public final class TimeSeriesUtils {
    private static final Color RANGE_COLOR = new Color(128, 128, 255);

    private TimeSeriesUtils() {
        throw new AssertionError();
    }

    public static ChartPanel createAcfChartPanel(TimeSeries timeSeries) {
        XYDataset dataset = TimeSeriesUtils.createAcfDataset(timeSeries);
        return createAcfPacfChartPanel(timeSeries, dataset, "Autocorrelation Function");
    }

    public static ChartPanel createPacfChartPanel(TimeSeries timeSeries) {
        XYDataset dataset = TimeSeriesUtils.createPacfDataset(timeSeries);
        return createAcfPacfChartPanel(timeSeries, dataset, "Partial Autocorrelation Function");
    }

    private static ChartPanel createAcfPacfChartPanel(TimeSeries timeSeries, XYDataset dataset, String title) {
        JFreeChart chart = TimeSeriesUtils.createTimeSeriesChart(dataset, new YIntervalRenderer());
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(createTitledBorder(createEtchedBorder(), title));

        double intervalValue = 1.96 / Math.sqrt(timeSeries.getMoments().size());
        IntervalMarker marker = new IntervalMarker(-intervalValue, intervalValue, RANGE_COLOR);
        chartPanel.getChart().getXYPlot().addRangeMarker(marker);
        return chartPanel;
    }

    public static XYDataset createTimeSeriesDataset(TimeSeries timeSeries) {
        XYSeries series = new XYSeries(StringUtils.EMPTY, false, true);
        for (Moment moment : timeSeries.getMoments()) {
            series.add(moment.getTime(), moment.getValue());
        }
        return new XYSeriesCollection(series);
    }

    public static XYDataset createAcfDataset(TimeSeries timeSeries) {
        return createDataset(timeSeries.getAcf(), 0);
    }

    public static XYDataset createPacfDataset(TimeSeries timeSeries) {
        return createDataset(timeSeries.getPacf(), 1);
    }

    private static XYDataset createDataset(List<Double> values, int initialLag) {
        YIntervalSeries series = new YIntervalSeries(StringUtils.EMPTY, false, true);
        for (int lag = initialLag; lag < values.size(); lag++) {
            double value = values.get(lag);
            series.add(lag, value, Math.min(0, value), Math.max(0, value));
        }
        YIntervalSeriesCollection collection = new YIntervalSeriesCollection();
        collection.addSeries(series);
        return collection;
    }

    public static JFreeChart createTimeSeriesChart(XYDataset dataset, XYItemRenderer renderer) {
        JFreeChart chart = ChartFactory.createXYLineChart(null, getMessage("ui.dialog.timeSeriesDialog.chart.x"),
                getMessage("ui.dialog.timeSeriesDialog.chart.y"), dataset, PlotOrientation.VERTICAL, false, false,
                false);
        XYPlot plot = (XYPlot) chart.getPlot();
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(true);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(true);
        if (renderer != null) {
            plot.setRenderer(renderer);
        }
        return chart;
    }

    public static void updateChartPanel(ChartPanel chartPanel, XYDataset dataset) {
        chartPanel.getChart().getXYPlot().setDataset(dataset);
    }
}
