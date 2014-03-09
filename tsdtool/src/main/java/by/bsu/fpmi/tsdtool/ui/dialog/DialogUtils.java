package by.bsu.fpmi.tsdtool.ui.dialog;

import by.bsu.fpmi.tsdtool.ui.dialog.filter.ManagedFileFilter;
import by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils;

import javax.swing.JFileChooser;

public final class DialogUtils {
    private static final JFileChooser FILE_CHOOSER;

    static {
        FILE_CHOOSER = new JFileChooser(System.getProperty("user.dir"));
        FILE_CHOOSER.setAcceptAllFileFilterUsed(false);
        FILE_CHOOSER.addChoosableFileFilter(new ManagedFileFilter());
        FILE_CHOOSER.setMultiSelectionEnabled(true);
        // TODO: internationalize File Chooser
    }

    private DialogUtils() {
    }

    public static JFileChooser getFileChooser() {
        return FILE_CHOOSER;
    }

    public static String getLabel(String key) {
        return MessageUtils.getMessage(key) + ":";
    }
}
