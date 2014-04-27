package by.bsu.fpmi.tsdtool.ui.dialog;

import by.bsu.fpmi.arimax.StatUtils;
import by.bsu.fpmi.arimax.model.Moment;
import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class AutocorrelationDialog extends JFrame implements Dialog {
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 300;

    protected final int id;
    protected final TimeSeries timeSeries;

    protected JPanel rootPanel;
    protected JLabel autocorrelationChartLabel;
    protected ChartPanel chartPanel;
    protected JFreeChart autocorrelationChart;
    protected XYDataset autocorrelationDataset;
    protected XYSeries acfSeries;

    public AutocorrelationDialog(int id, TimeSeries timeSeries) {
        this.id = id;
        this.timeSeries = timeSeries;

        configure();
        initializeElements();
        arrangeElements();

        calcAutocorrelationFunction();
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
        rootPanel = new JPanel();
        autocorrelationChartLabel =
                new JLabel(DialogUtils.getLabel("ui.dialog.autocorrelationDialog.label.autocorrelationChart"));
        autocorrelationDataset = createTimeSeriesDataset();
        autocorrelationChart = createTimeSeriesChart(autocorrelationDataset);
        chartPanel = new ChartPanel(autocorrelationChart);
    }

    protected void arrangeElements() {
        rootPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 3, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(autocorrelationChartLabel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(3, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(chartPanel, gbc);

        add(rootPanel);
    }

    protected void calcAutocorrelationFunction() {
        List<Moment> moments = timeSeries.getMoments();
        double mean = StatUtils.getMean(timeSeries);
        double dispersion = StatUtils.getVariance(timeSeries, mean);
        for (int k = 0; k < moments.size(); k++) {
            acfSeries.add(k, StatUtils.getACF(moments, k, mean, dispersion));
        }
        acfSeries.fireSeriesChanged();
    }

    protected XYDataset createTimeSeriesDataset() {
        acfSeries = new XYSeries(0, false, true);
        XYSeriesCollection series = new XYSeriesCollection();
        series.addSeries(acfSeries);
        return series;
    }

    protected JFreeChart createTimeSeriesChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory
                .createXYLineChart(null, MessageUtils.getMessage("ui.dialog.autocorrelationDialog.chart.x"),
                        MessageUtils.getMessage("ui.dialog.autocorrelationDialog.chart.y"), dataset,
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
