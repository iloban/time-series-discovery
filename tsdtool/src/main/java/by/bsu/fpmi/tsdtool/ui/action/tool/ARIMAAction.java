package by.bsu.fpmi.tsdtool.ui.action.tool;

import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.ui.action.AbstractAction;
import by.bsu.fpmi.tsdtool.ui.dialog.arima.ARIMADialog;
import by.bsu.fpmi.tsdtool.ui.dialog.Dialog;
import by.bsu.fpmi.tsdtool.ui.dialog.DialogManager;
import by.bsu.fpmi.tsdtool.ui.dialog.arima.SelectRegionView;

import java.awt.event.ActionEvent;

public class ARIMAAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        Dialog dialog = DialogManager.getCurrentDialog();
        if (dialog == null) {
            // TODO: show error
        } else {
            TimeSeries timeSeries = dialog.getTimeSeries();
            ARIMADialog arimaDialog = DialogManager.createARIMADialog(timeSeries);
            arimaDialog.setView(new SelectRegionView(arimaDialog));
            arimaDialog.setVisible(true);
        }
    }
}
