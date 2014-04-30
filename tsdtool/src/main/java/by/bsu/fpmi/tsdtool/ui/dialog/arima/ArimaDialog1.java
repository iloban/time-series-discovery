package by.bsu.fpmi.tsdtool.ui.dialog.arima;

import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.ui.dialog.Dialog;
import by.bsu.fpmi.tsdtool.ui.dialog.DialogManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class ArimaDialog1 extends JFrame implements Dialog {
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;

    private final int id;
    private final TimeSeries timeSeries;
    private final JPanel contentPanel = new JPanel();

    private View currentView;
    private TimeSeries regionTimeSeries;

    public ArimaDialog1(int id, TimeSeries timeSeries) {
        this.id = id;
        this.timeSeries = timeSeries;

        configure();
        add(contentPanel);
    }

    protected void configure() {
        setTitle(timeSeries.getTitle());
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationByPlatform(true);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) {
                DialogManager.release(id);
            }

            @Override public void windowActivated(WindowEvent e) {
                DialogManager.setCurrentDialogId(id);
            }
        });
    }

    public void setView(View newView) {
        currentView = newView;
        contentPanel.updateUI();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public TimeSeries getTimeSeries() {
        return timeSeries;
    }

    public TimeSeries getRegionTimeSeries() {
        return regionTimeSeries;
    }

    public void setRegionTimeSeries(TimeSeries regionTimeSeries) {
        this.regionTimeSeries = regionTimeSeries;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}
