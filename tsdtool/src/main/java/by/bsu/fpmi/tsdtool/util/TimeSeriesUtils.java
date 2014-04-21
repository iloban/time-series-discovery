package by.bsu.fpmi.tsdtool.util;

import by.bsu.fpmi.arimax.model.Moment;
import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public final class TimeSeriesUtils {
    public static XYDataset createTimeSeriesDataset(TimeSeries timeSeries) {
        XYSeries series = new XYSeries(StringUtils.EMPTY, false, true);
        for (Moment moment : timeSeries.getMoments()) {
            series.add(moment.getTime(), moment.getValue());
        }
        return new XYSeriesCollection(series);
    }

    public static JFreeChart createTimeSeriesChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory
                .createXYLineChart(null, MessageUtils.getMessage("ui.dialog.timeSeriesDialog.chart.x"),
                        MessageUtils.getMessage("ui.dialog.timeSeriesDialog.chart.y"), dataset,
                        PlotOrientation.VERTICAL, false, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(true);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(true);
        return chart;
    }

    private TimeSeriesUtils() {
        throw new AssertionError();
    }
}
