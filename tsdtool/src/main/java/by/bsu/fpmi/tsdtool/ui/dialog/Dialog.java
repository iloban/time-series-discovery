package by.bsu.fpmi.tsdtool.ui.dialog;

import by.bsu.fpmi.arimax.model.TimeSeries;

public interface Dialog {
    int getId();

    TimeSeries getTimeSeries();
}
