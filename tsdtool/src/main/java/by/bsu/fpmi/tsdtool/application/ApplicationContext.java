package by.bsu.fpmi.tsdtool.application;

import by.bsu.fpmi.tsdtool.ui.frame.MainFrame;

public final class ApplicationContext {
    private static MainFrame mainFrame;

    private ApplicationContext() {
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }

    public static void setMainFrame(MainFrame mainFrame) {
        ApplicationContext.mainFrame = mainFrame;
    }
}
