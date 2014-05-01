package by.bsu.fpmi.tsdtool.ui.action.tool;

import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.ui.action.AbstractAction;
import by.bsu.fpmi.tsdtool.ui.dialog.Dialog;
import by.bsu.fpmi.tsdtool.ui.dialog.DialogManager;
import by.bsu.fpmi.tsdtool.ui.dialog.arima.ArimaDialog;
import by.bsu.fpmi.tsdtool.ui.dialog.arima.SelectRegionView;

import java.awt.event.ActionEvent;

public class ArimaAction2 extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        Dialog dialog = DialogManager.getCurrentDialog();
        if (dialog == null) {
            // TODO: show error
        } else {
            TimeSeries timeSeries = dialog.getTimeSeries();
            ArimaDialog arimaDialog = DialogManager.createARIMADialog(timeSeries);
            arimaDialog.setView(new SelectRegionView(arimaDialog));
            arimaDialog.setVisible(true);
        }
    }
}
