package by.bsu.fpmi.tsdtool.ui.dialog.arima;

import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.util.TimeSeriesUtils;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import static by.bsu.fpmi.arimax.StatUtils.getRegion;
import static by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils.getMessage;

public final class SelectRegionView implements View {
    private final ARIMADialog dialog;
    private final JButton nextButton = new JButton(getMessage("ui.dialog.arimaDialog.button.next"));
    private final JLabel leftBoundLabel = new JLabel(getMessage("ui.dialog.arimaDialog.label.leftBound"));
    private final JLabel rightBoundLabel = new JLabel(getMessage("ui.dialog.arimaDialog.label.rightBound"));
    private final JLabel regionLengthLabel = new JLabel(getMessage("ui.dialog.arimaDialog.label.regionLength"));
    private final JLabel regionLengthValueLabel = new JLabel();
    private final JSlider leftBoundSlider = new JSlider();
    private final JSlider rightBoundSlider = new JSlider();

    private ChartPanel chartPanel;

    public SelectRegionView(ARIMADialog dialog) {
        this.dialog = dialog;

        initComponents();
        arrangeComponents();

        recalcRegionLength();
    }

    private void initComponents() {
        TimeSeries timeSeries = dialog.getTimeSeries();

        XYDataset timeSeriesDataset = TimeSeriesUtils.createTimeSeriesDataset(timeSeries);
        JFreeChart timeSeriesChart = TimeSeriesUtils.createTimeSeriesChart(timeSeriesDataset);
        chartPanel = new ChartPanel(timeSeriesChart);

        BoundChangedListener boundChangedListener = new BoundChangedListener();
        leftBoundSlider.setMinimum(0);
        leftBoundSlider.setMaximum(timeSeries.getMoments().size() - 1);
        leftBoundSlider.setValue(0);
        leftBoundSlider.addChangeListener(boundChangedListener);
        rightBoundSlider.setMinimum(0);
        rightBoundSlider.setMaximum(timeSeries.getMoments().size() - 1);
        rightBoundSlider.setValue(timeSeries.getMoments().size() - 1);
        rightBoundSlider.addChangeListener(boundChangedListener);

        nextButton.addActionListener(e -> {
            int leftBound = leftBoundSlider.getValue();
            int rightBound = rightBoundSlider.getValue();
            TimeSeries region = getRegion(timeSeries, leftBound, rightBound);
            dialog.setRegionTimeSeries(region);
            dialog.setView(new IntegrationOrderView(dialog));
        });
    }

    private void arrangeComponents() {
        JPanel contentPanel = dialog.getContentPanel();
        contentPanel.removeAll();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(chartPanel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(leftBoundLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(leftBoundSlider, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(rightBoundLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(rightBoundSlider, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(regionLengthLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(regionLengthValueLabel, gbc);

        gbc.gridy = 4;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(nextButton, gbc);
    }

    private void recalcRegionLength() {
        int leftBound = leftBoundSlider.getValue();
        int rightBound = rightBoundSlider.getValue();
        regionLengthValueLabel.setText(String.valueOf(rightBound - leftBound));
    }

    private final class BoundChangedListener implements ChangeListener {
        @Override public void stateChanged(ChangeEvent e) {
            recalcRegionLength();
        }
    }
}
