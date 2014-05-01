package by.bsu.fpmi.tsdtool.ui.dialog.arima;

import by.bsu.fpmi.arimax.StatUtils;
import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.util.TimeSeriesUtils;
import org.jfree.chart.ChartPanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import static by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils.getMessage;

public final class IntegrationOrderView implements View {
    private final ArimaDialog dialog;
    private final JLabel integrationParamLabel = new JLabel(getMessage("ui.dialog.arimaDialog.label.integrationParam"));
    private final JSpinner integrationParamSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    private final JButton nextButton = new JButton(getMessage("ui.dialog.arimaDialog.button.next"));
    private final JButton backButton = new JButton(getMessage("ui.dialog.arimaDialog.button.back"));

    private final List<TimeSeries> timeSerieses = new ArrayList<>();

    private ChartPanel acfChartPanel;
    private ChartPanel pacfChartPanel;

    public IntegrationOrderView(ArimaDialog dialog) {
        this.dialog = dialog;

        initComponents();
        arrangeComponents();
    }

    private void initComponents() {
        TimeSeries timeSeries = dialog.getRegionTimeSeries();
        StatUtils.calcAcfAndPacf(timeSeries);
        timeSerieses.add(0, timeSeries);

        acfChartPanel = TimeSeriesUtils.createAcfChartPanel(timeSeries);
        pacfChartPanel = TimeSeriesUtils.createPacfChartPanel(timeSeries);

        integrationParamSpinner.addChangeListener((e) -> updateCharts());

        backButton.addActionListener((e) -> dialog.setView(new SelectRegionView(dialog)));
        nextButton.addActionListener((e) -> dialog
                .setView(new ArmaOrderView(dialog, timeSerieses.get(getIntegrationOrder()), getIntegrationOrder())));
    }

    private void updateCharts() {
        int integrationOrder = getIntegrationOrder();
        for (int i = timeSerieses.size() - 1; i < integrationOrder; i++) {
            TimeSeries difference = StatUtils.getDifference(timeSerieses.get(i), 1);
            StatUtils.calcAcfAndPacf(difference);
            timeSerieses.add(i + 1, difference);
        }

        TimeSeries timeSeries = timeSerieses.get(integrationOrder);
        TimeSeriesUtils.updateChartPanel(acfChartPanel, TimeSeriesUtils.createAcfDataset(timeSeries));
        TimeSeriesUtils.updateChartPanel(pacfChartPanel, TimeSeriesUtils.createPacfDataset(timeSeries));

        acfChartPanel.updateUI();
        pacfChartPanel.updateUI();
    }

    private int getIntegrationOrder() {return (int) integrationParamSpinner.getValue();}

    private void arrangeComponents() {
        JPanel contentPanel = dialog.getContentPanel();
        contentPanel.removeAll();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(integrationParamLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(integrationParamSpinner, gbc);

        JPanel chartPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 0);
        chartPanel.add(acfChartPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 0);
        chartPanel.add(pacfChartPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);
        contentPanel.add(chartPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(backButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(nextButton, gbc);
    }
}
