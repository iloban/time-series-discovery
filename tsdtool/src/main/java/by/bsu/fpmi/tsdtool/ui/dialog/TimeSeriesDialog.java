package by.bsu.fpmi.tsdtool.ui.dialog;

import by.bsu.fpmi.arimax.model.Moment;
import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TimeSeriesDialog extends JFrame implements Dialog {
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;

    protected final int id;
    protected final TimeSeries timeSeries;

    protected JPanel rootPanel;
    protected ChartPanel chartPanel;
    protected JFreeChart timeSeriesChart;
    protected XYDataset timeSeriesDataset;

    public TimeSeriesDialog(int id, TimeSeries timeSeries) {
        this.id = id;
        this.timeSeries = timeSeries;

        configure();
        initializeElements();
        arrangeElements();
    }

    protected void configure() {
        setTitle(timeSeries.getTitle());
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationByPlatform(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                DialogManager.release(id);
            }

            @Override
            public void windowActivated(WindowEvent e) {
                DialogManager.setCurrentDialogId(id);
            }
        });
    }

    protected void initializeElements() {
        rootPanel = new JPanel(new BorderLayout());
        timeSeriesDataset = createTimeSeriesDataset();
        timeSeriesChart = createTimeSeriesChart(timeSeriesDataset);
        chartPanel = new ChartPanel(timeSeriesChart);
    }

    protected void arrangeElements() {
        rootPanel.add(chartPanel);
        add(rootPanel);
    }

    protected XYDataset createTimeSeriesDataset() {
        XYSeries series = new XYSeries(StringUtils.EMPTY, false, true);
        for (Moment moment : timeSeries.getMoments()) {
            series.add(moment.getTime(), moment.getValue());
        }
        return new XYSeriesCollection(series);
    }

    protected JFreeChart createTimeSeriesChart(XYDataset dataset) {
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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public TimeSeries getTimeSeries() {
        return timeSeries;
    }
}
