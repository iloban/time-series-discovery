package by.bsu.fpmi.tsdtool.application;

import java.util.ResourceBundle;

public final class Settings {
    private static final String CONTEXT_NAME = "by.bsu.fpmi.tsdtool.resources.context.settings";

    private Settings() {
    }

    public static String getProperty(String key) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(CONTEXT_NAME);
        return resourceBundle.containsKey(key) ? resourceBundle.getString(key) : null;
    }
}
