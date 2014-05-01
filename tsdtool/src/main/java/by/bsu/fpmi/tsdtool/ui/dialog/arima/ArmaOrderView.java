package by.bsu.fpmi.tsdtool.ui.dialog.arima;

import by.bsu.fpmi.arimax.model.ArimaModel;
import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.util.TimeSeriesUtils;
import org.jfree.chart.ChartPanel;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import static by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils.getMessage;

public final class ArmaOrderView implements View {
    private final ArimaDialog dialog;
    private final JLabel arimaListLabel = new JLabel(getMessage("ui.dialog.arimaDialog.label.arimaList"));
    private final JList<String> arimaList = new JList<>();
    private final JLabel maOrderLabel = new JLabel(getMessage("ui.dialog.arimaDialog.label.maOrder"));
    private final JSpinner maOrderSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    private final JLabel arOrderLabel = new JLabel(getMessage("ui.dialog.arimaDialog.label.arOrder"));
    private final JSpinner arOrderSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    private final JButton nextButton = new JButton(getMessage("ui.dialog.arimaDialog.button.next"));
    private final JButton backButton = new JButton(getMessage("ui.dialog.arimaDialog.button.back"));
    private final JButton addButton = new JButton(getMessage("ui.dialog.arimaDialog.button.add"));
    private final JButton removeButton = new JButton(getMessage("ui.dialog.arimaDialog.button.remove"));

    private final ArimaListModel arimaListModel = new ArimaListModel();
    private final TimeSeries timeSeries;
    private final int d;

    private ChartPanel acfChartPanel;
    private ChartPanel pacfChartPanel;

    public ArmaOrderView(ArimaDialog dialog, TimeSeries timeSeries, int d) {
        this.dialog = dialog;
        this.timeSeries = timeSeries;
        this.d = d;

        initComponents();
        arrangeComponents();

        dialog.getContentPanel().updateUI();
    }

    private void initComponents() {
        arimaList.setModel(arimaListModel);
        arimaListModel.addArimaModel(new ArimaModel(1, d, 0));
        arimaListModel.addArimaModel(new ArimaModel(2, d, 0));
        arimaListModel.addArimaModel(new ArimaModel(0, d, 1));
        arimaListModel.addArimaModel(new ArimaModel(0, d, 2));
        arimaListModel.addArimaModel(new ArimaModel(1, d, 1));
        arimaListModel.addArimaModel(new ArimaModel(2, d, 2));

        acfChartPanel = TimeSeriesUtils.createAcfChartPanel(timeSeries);
        pacfChartPanel = TimeSeriesUtils.createPacfChartPanel(timeSeries);

        backButton.addActionListener((e) -> dialog.setView(new IntegrationOrderView(dialog)));
        addButton.addActionListener((e) -> arimaListModel
                .addArimaModel(new ArimaModel((int) arOrderSpinner.getValue(), d, (int) maOrderSpinner.getValue())));
        removeButton.addActionListener((e) -> {
            int index = arimaList.getSelectedIndex();
            if (index >= 0) {
                arimaListModel.removeArimaModelAt(index);
            }
        });
    }

    private void arrangeComponents() {
        JPanel contentPanel = dialog.getContentPanel();
        contentPanel.removeAll();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        arrangeArimaList(contentPanel, gbc);
        arrangeAcfChart(contentPanel, gbc);
        arrangePacfChart(contentPanel, gbc);
        arrangeButtons(contentPanel);
    }

    private void arrangeButtons(JPanel contentPanel) {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        buttonPanel.add(backButton, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(removeButton, gbc);

        gbc.gridx = 2;
        buttonPanel.add(addButton, gbc);

        gbc.gridx = 3;
        buttonPanel.add(nextButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(buttonPanel, gbc);
    }

    private void arrangeAcfChart(JPanel contentPanel, GridBagConstraints gbc) {
        JPanel chartPanel = getChartPanel(acfChartPanel, maOrderLabel, maOrderSpinner);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(chartPanel, gbc);
    }

    private void arrangePacfChart(JPanel contentPanel, GridBagConstraints gbc) {
        JPanel chartPanel = getChartPanel(pacfChartPanel, arOrderLabel, arOrderSpinner);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(chartPanel, gbc);
    }

    private JPanel getChartPanel(ChartPanel chart, JLabel label, JSpinner spinner) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(chart, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(spinner, gbc);

        return panel;
    }

    private void arrangeArimaList(JPanel contentPanel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(arimaListLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(arimaList, gbc);
    }

    private static final class ArimaListModel extends DefaultListModel<String> {
        private final List<ArimaModel> arimaModels = new ArrayList<>();

        public void addArimaModel(ArimaModel arimaModel) {
            arimaModels.add(arimaModel);
            addElement(arimaModel.toString());
        }

        public void removeArimaModelAt(int index) {
            arimaModels.remove(index);
            removeElementAt(index);
        }

        public List<ArimaModel> getArimaModels() {
            return arimaModels;
        }
    }
}
