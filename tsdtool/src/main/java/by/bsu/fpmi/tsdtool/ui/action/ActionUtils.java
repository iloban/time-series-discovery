package by.bsu.fpmi.tsdtool.ui.action;

import javax.swing.Action;
import java.util.HashMap;
import java.util.Map;

public final class ActionUtils {
    private static Map<Class<? extends Action>, Action> actionPull;

    private ActionUtils() {
    }

    public static synchronized <T extends Action> T getAction(Class<T> actionClass)
            throws IllegalArgumentException {
        if (actionPull == null) {
            actionPull = new HashMap<>();
        }

        if (!actionPull.containsKey(actionClass)) {
            try {
                actionPull.put(actionClass, actionClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("Can't create instance of " + actionClass.getName() + " class", e);
            }
        }

        return actionClass.cast(actionPull.get(actionClass));
    }
}
