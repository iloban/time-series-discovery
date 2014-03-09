package by.bsu.fpmi.tsdtool.ui.action.file;

import by.bsu.fpmi.tsdtool.ui.action.AbstractAction;

import java.awt.event.ActionEvent;

public class ExitAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        exit(0);
    }

    public static void exit(int status) {
        System.exit(status);
    }
}
