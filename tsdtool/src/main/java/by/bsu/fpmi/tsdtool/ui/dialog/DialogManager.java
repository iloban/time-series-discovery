package by.bsu.fpmi.tsdtool.ui.dialog;

import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.tsdtool.ui.dialog.arima.ArimaDialog1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class DialogManager {
    private static final int MAX_DIALOG_COUNT = 8;
    private static final Map<Integer, Dialog> DIALOG_MAP = new HashMap<>();

    private static int currentDialogId;

    private DialogManager() {
    }

    public static TimeSeriesDialog createTimeSeriesView(TimeSeries timeSeries) {
        if (DIALOG_MAP.size() >= MAX_DIALOG_COUNT) {
            // TODO: if more than max then show error message and break method
        }
        int id = getFreeId();
        TimeSeriesDialog view = new TimeSeriesDialog(id, timeSeries);
        DIALOG_MAP.put(id, view);
        return view;
    }

    public static HurstExponentialDialog createHurstExponentialDialog(TimeSeries timeSeries) {
        if (DIALOG_MAP.size() >= MAX_DIALOG_COUNT) {
            // TODO: if more than max then show error message and break method
        }
        int id = getFreeId();
        HurstExponentialDialog dialog = new HurstExponentialDialog(id, timeSeries);
        DIALOG_MAP.put(id, dialog);
        return dialog;
    }

    public static AutocorrelationDialog createAutocorrelationDialog(TimeSeries timeSeries) {
        if (DIALOG_MAP.size() >= MAX_DIALOG_COUNT) {
            // TODO: if more than max then show error message and break method
        }
        int id = getFreeId();
        AutocorrelationDialog dialog = new AutocorrelationDialog(id, timeSeries);
        DIALOG_MAP.put(id, dialog);
        return dialog;
    }

    public static ArimaDialog1 createARIMADialog(TimeSeries timeSeries) {
        if (DIALOG_MAP.size() >= MAX_DIALOG_COUNT) {
            // TODO: if more than max then show error message and break method
        }
        int id = getFreeId();
        ArimaDialog1 dialog = new ArimaDialog1(id, timeSeries);
        DIALOG_MAP.put(id, dialog);
        return dialog;
    }

    public static void release(int id) {
        DIALOG_MAP.remove(id);
    }

    public static Dialog getCurrentDialog() {
        return DIALOG_MAP.get(currentDialogId);
    }

    public static void setCurrentDialogId(int currentDialogId) {
        DialogManager.currentDialogId = currentDialogId;
    }

    private static int getFreeId() {
        Set<Integer> ids = DIALOG_MAP.keySet();
        for (int i = 1; i <= MAX_DIALOG_COUNT; i++) {
            if (!ids.contains(i)) {
                return i;
            }
        }
        return 0;
    }
}
