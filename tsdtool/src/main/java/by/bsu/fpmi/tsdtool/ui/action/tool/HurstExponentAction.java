package by.bsu.fpmi.tsdtool.ui.action.tool;

import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.ui.action.AbstractAction;
import by.bsu.fpmi.tsdtool.ui.dialog.Dialog;
import by.bsu.fpmi.tsdtool.ui.dialog.DialogManager;
import by.bsu.fpmi.tsdtool.ui.dialog.HurstExponentialDialog;

import java.awt.event.ActionEvent;

public class HurstExponentAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        Dialog dialog = DialogManager.getCurrentDialog();
        if (dialog == null) {
            // TODO: show error
        } else {
            TimeSeries timeSeries = dialog.getTimeSeries();
            HurstExponentialDialog hurstDialog = DialogManager.createHurstExponentialDialog(timeSeries);
            hurstDialog.setVisible(true);
        }
    }
}
