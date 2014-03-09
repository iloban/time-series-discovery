package by.bsu.fpmi.tsdtool.launcher;

import by.bsu.fpmi.tsdtool.application.ApplicationContext;
import by.bsu.fpmi.tsdtool.ui.frame.MainFrame;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class Launcher {
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                ApplicationContext.setMainFrame(mainFrame);
                mainFrame.setVisible(true);
            }
        });
    }
}
