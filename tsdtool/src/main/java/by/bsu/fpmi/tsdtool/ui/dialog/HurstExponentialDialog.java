package by.bsu.fpmi.tsdtool.ui.dialog;

import by.bsu.fpmi.arimax.StatUtils;
import by.bsu.fpmi.arimax.model.Moment;
import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HurstExponentialDialog extends JFrame implements Dialog {
    private static final int DEFAULT_WIDTH = 480;
    private static final int DEFAULT_HEIGHT = 580;

    protected final int id;
    protected final TimeSeries timeSeries;

    protected JPanel rootPanel;
    protected JLabel segmentsLabel;
    protected JTextField segmentsTextField;
    protected JButton hurstCalcButton;
    protected JLabel hurstChartLabel;
    protected ChartPanel chartPanel;
    protected JFreeChart hurstChart;
    protected XYDataset hurstDataset;
    protected XYSeries hurstSeries;
    protected XYSeries olsHurstSeries;
    protected JLabel hurstLabel;
    protected JLabel hurstLabelOutput;
    protected JLabel fractalDimensionLabel;
    protected JLabel fractalDimensionLabelOutput;
    protected JLabel correlationParameterLabel;
    protected JLabel correlationParameterLabelOutput;
    protected JLabel spectralIndexLabel;
    protected JLabel spectralIndexLabelOutput;
    protected JLabel fractalMeasureLabel;
    protected JLabel fractalMeasureLabelOutput;

    public HurstExponentialDialog(int id, TimeSeries timeSeries) {
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
        StringBuilder taus = new StringBuilder();
        final int count = 20;
        final int delta = timeSeries.getMoments().size() / count;
        taus.append(delta);
        for (int i = 2; i <= count; i++) {
            taus.append(",").append(i * delta);
        }

        rootPanel = new JPanel();
        segmentsLabel = new JLabel(DialogUtils.getLabel("ui.dialog.hurstExponentialDialog.label.segments"));
        segmentsTextField = new JTextField(taus.toString());
        hurstCalcButton = new JButton(MessageUtils.getMessage("ui.dialog.hurstExponentialDialog.button.calcHurst"));
        hurstChartLabel = new JLabel(DialogUtils.getLabel("ui.dialog.hurstExponentialDialog.label.hurstChart"));
        hurstDataset = createTimeSeriesDataset();
        hurstChart = createTimeSeriesChart(hurstDataset);
        chartPanel = new ChartPanel(hurstChart);
        hurstLabel = new JLabel(DialogUtils.getLabel("ui.dialog.hurstExponentialDialog.label.hurst"));
        hurstLabelOutput = new JLabel();
        fractalDimensionLabel =
                new JLabel(DialogUtils.getLabel("ui.dialog.hurstExponentialDialog.label.fractalDimension"));
        fractalDimensionLabelOutput = new JLabel();
        correlationParameterLabel =
                new JLabel(DialogUtils.getLabel("ui.dialog.hurstExponentialDialog.label.correlationParameter"));
        correlationParameterLabelOutput = new JLabel();
        spectralIndexLabel = new JLabel(DialogUtils.getLabel("ui.dialog.hurstExponentialDialog.label.spectralIndex"));
        spectralIndexLabelOutput = new JLabel();
        fractalMeasureLabel = new JLabel(DialogUtils.getLabel("ui.dialog.hurstExponentialDialog.label.fractalMeasure"));
        fractalMeasureLabelOutput = new JLabel();

        hurstCalcButton.addActionListener(new HurstCalculator());
    }

    protected void arrangeElements() {
        rootPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Insets rightBottomInsets = new Insets(3, 3, 10, 10);
        Insets leftTopInsets = new Insets(10, 10, 3, 3);
        Insets leftBottomInsets = new Insets(3, 10, 10, 3);
        Insets leftInsets = new Insets(3, 10, 3, 3);
        Insets rightInsets = new Insets(3, 3, 3, 10);
        Insets allInsets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 3;
        gbc.insets = leftTopInsets;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        rootPanel.add(segmentsLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = leftInsets;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        rootPanel.add(segmentsTextField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.insets = rightInsets;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        rootPanel.add(hurstCalcButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 3;
        gbc.insets = allInsets;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(chartPanel, gbc);

        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.insets = leftInsets;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        rootPanel.add(hurstLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.insets = rightInsets;
        gbc.anchor = GridBagConstraints.EAST;
        rootPanel.add(hurstLabelOutput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.insets = leftInsets;
        gbc.anchor = GridBagConstraints.WEST;
        rootPanel.add(fractalDimensionLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.insets = rightInsets;
        gbc.anchor = GridBagConstraints.EAST;
        rootPanel.add(fractalDimensionLabelOutput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.insets = leftInsets;
        gbc.anchor = GridBagConstraints.WEST;
        rootPanel.add(correlationParameterLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.insets = rightInsets;
        gbc.anchor = GridBagConstraints.EAST;
        rootPanel.add(correlationParameterLabelOutput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.insets = leftInsets;
        gbc.anchor = GridBagConstraints.WEST;
        rootPanel.add(spectralIndexLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.insets = rightInsets;
        gbc.anchor = GridBagConstraints.EAST;
        rootPanel.add(spectralIndexLabelOutput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.insets = leftBottomInsets;
        gbc.anchor = GridBagConstraints.WEST;
        rootPanel.add(fractalMeasureLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.insets = rightBottomInsets;
        gbc.anchor = GridBagConstraints.EAST;
        rootPanel.add(fractalMeasureLabelOutput, gbc);

        add(rootPanel);
    }

    protected XYDataset createTimeSeriesDataset() {
        hurstSeries = new XYSeries(0, false, true);
        olsHurstSeries = new XYSeries(1, false, true);
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        seriesCollection.addSeries(hurstSeries);
        seriesCollection.addSeries(olsHurstSeries);
        return seriesCollection;
    }

    protected JFreeChart createTimeSeriesChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory
                .createXYLineChart(null, MessageUtils.getMessage("ui.dialog.hurstExponentialDialog.chart.x"),
                        MessageUtils.getMessage("ui.dialog.hurstExponentialDialog.chart.y"), dataset,
                        PlotOrientation.VERTICAL, false, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(true);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(true);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesLinesVisible(1, true);

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

    private final class HurstCalculator implements ActionListener {
        private static final String COMMA = ",";
        private static final int SCALE = 3;

        @Override
        public void actionPerformed(ActionEvent e) {
            List<Integer> taus = getInputTaus();
            if (taus == null || taus.isEmpty()) {
                // TODO: show error
                return;
            }

            calc(taus);
        }

        private void calc(List<Integer> taus) {
            List<Moment> moments = timeSeries.getMoments();
            List<Double> hurstNumerators = new ArrayList<>();
            List<Double> hurstDenominators = new ArrayList<>();
            for (Integer tau : taus) {
                List<Moment> segment = moments.subList(0, tau);
                hurstNumerators.add(StatUtils.calcNumerator(segment));
                hurstDenominators.add(StatUtils.calcDenominator(segment));
            }
            Pair<Double, Double> lineRegressionCoefficients =
                    calcAndViewSeriesParameters(hurstDenominators, hurstNumerators);
            viewHurstSeries(hurstDenominators, hurstNumerators, lineRegressionCoefficients);
        }

        private Pair<Double, Double> calcAndViewSeriesParameters(List<Double> hurstDenominators,
                                                                 List<Double> hurstNumerators) {
            Pair<Double, Double> lineRegressionCoefficients =
                    StatUtils.calcLineRegressionCoefficientsByOLS(hurstDenominators, hurstNumerators);
            double hurst = lineRegressionCoefficients.getLeft();
            double fractalDimension = StatUtils.calcFractalDimension(hurst);
            double correlationParameter = StatUtils.calcCorrelationParameter(hurst);
            double spectralIndex = StatUtils.calcSpectralIndex(hurst);
            double fractalMeasure = StatUtils.calcFractalMeasure(hurst);
            hurstLabelOutput.setText(getString(hurst));
            fractalDimensionLabelOutput.setText(getString(fractalDimension));
            correlationParameterLabelOutput.setText(getString(correlationParameter));
            spectralIndexLabelOutput.setText(getString(spectralIndex));
            fractalMeasureLabelOutput.setText(getString(fractalMeasure));
            return lineRegressionCoefficients;
        }

        private void viewHurstSeries(List<Double> hurstDenominators, List<Double> hurstNumerators,
                                     Pair<Double, Double> lineRegressionCoefficients) {
            hurstSeries.clear();
            for (int i = 0, count = hurstNumerators.size(); i < count; i++) {
                hurstSeries.add(hurstDenominators.get(i), hurstNumerators.get(i));
            }
            olsHurstSeries.clear();
            double x = hurstDenominators.get(0);
            olsHurstSeries.add(x, lineRegressionCoefficients.getLeft() * x + lineRegressionCoefficients.getRight());
            x = hurstDenominators.get(hurstDenominators.size() - 1);
            olsHurstSeries.add(x, lineRegressionCoefficients.getLeft() * x + lineRegressionCoefficients.getRight());
            hurstSeries.fireSeriesChanged();
            olsHurstSeries.fireSeriesChanged();
        }

        private List<Integer> getInputTaus() {
            String inputText = segmentsTextField.getText();
            String[] tokens = inputText.split(COMMA);
            try {
                List<Integer> taus = new ArrayList<>();
                for (String token : tokens) {
                    taus.add(Integer.parseInt(token));
                }
                return taus;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        private String getString(double value) {
            return BigDecimal.valueOf(value).setScale(SCALE, RoundingMode.UP).toString();
        }
    }
}
