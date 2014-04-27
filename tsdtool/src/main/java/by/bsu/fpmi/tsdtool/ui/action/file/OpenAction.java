package by.bsu.fpmi.tsdtool.ui.action.file;

import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.arimax.model.TimeSeriesFactory;
import by.bsu.fpmi.tsdtool.ui.action.AbstractAction;
import by.bsu.fpmi.tsdtool.ui.dialog.DialogManager;
import by.bsu.fpmi.tsdtool.ui.dialog.DialogUtils;
import by.bsu.fpmi.tsdtool.ui.dialog.TimeSeriesDialog;

import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.io.File;

public class OpenAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = DialogUtils.getFileChooser();
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File file : files) {
                TimeSeries timeSeries = TimeSeriesFactory.createTimeSeries(file);
                TimeSeriesDialog timeSeriesDialog = DialogManager.createTimeSeriesView(timeSeries);
                timeSeriesDialog.setVisible(true);
            }
        }
    }
}
