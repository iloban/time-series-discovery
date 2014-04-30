package by.bsu.fpmi.tsdtool.ui.frame;

import by.bsu.fpmi.tsdtool.ui.action.ActionUtils;
import by.bsu.fpmi.tsdtool.ui.action.file.ExitAction;
import by.bsu.fpmi.tsdtool.ui.action.file.FileAction;
import by.bsu.fpmi.tsdtool.ui.action.file.OpenAction;
import by.bsu.fpmi.tsdtool.ui.action.tool.ArimaAction1;
import by.bsu.fpmi.tsdtool.ui.action.tool.AutocorrelationAction;
import by.bsu.fpmi.tsdtool.ui.action.tool.HurstExponentAction;
import by.bsu.fpmi.tsdtool.ui.action.tool.ToolsAction;
import by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class MainFrame extends JFrame {
    protected Dimension maximumSize;
    protected JMenuBar mainMenu;

    public MainFrame() {
        configure();
        createMainMenu();
        arrange();
    }

    protected void configure() {
        setTitle(MessageUtils.getMessage("ui.mainFrame.title"));

        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == JFrame.MAXIMIZED_BOTH) {
                    setSize(maximumSize);
                    setLocation(0, 0);
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getHeight() > maximumSize.height) {
                    setSize(getWidth(), maximumSize.height);
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ExitAction.exit(0);
            }
        });
    }

    protected void createMainMenu() {
        mainMenu = new JMenuBar();

        JMenu fileMenu = new JMenu(ActionUtils.getAction(FileAction.class));
        fileMenu.add(ActionUtils.getAction(OpenAction.class));
        fileMenu.addSeparator();
        fileMenu.add(ActionUtils.getAction(ExitAction.class));
        mainMenu.add(fileMenu);

        JMenu toolsMenu = new JMenu(ActionUtils.getAction(ToolsAction.class));
        toolsMenu.add(ActionUtils.getAction(AutocorrelationAction.class));
        toolsMenu.add(ActionUtils.getAction(HurstExponentAction.class));
        toolsMenu.add(ActionUtils.getAction(ArimaAction1.class));
        mainMenu.add(toolsMenu);

        setJMenuBar(mainMenu);
    }

    protected void arrange() {
        pack();
        Dimension minimumSize = getSize();
        setMinimumSize(minimumSize);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        maximumSize = new Dimension(screenSize.width, minimumSize.height);

        setSize(maximumSize);
    }
}
