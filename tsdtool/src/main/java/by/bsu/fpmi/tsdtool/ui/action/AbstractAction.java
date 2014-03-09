package by.bsu.fpmi.tsdtool.ui.action;

import by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils;

import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public abstract class AbstractAction extends javax.swing.AbstractAction {
    protected static final String ACTION_PREFIX = "ui.action.";
    protected static final String NAME_SUFFIX = ".name";
    protected static final String MNEMONIC_CHAR_SUFFIX = ".mnemonic";
    protected static final String ACCELERATOR_SUFFIX = ".accelerator";

    public AbstractAction() {
        setNMAValues();
    }

    public void setNMAValues() {
        String name = getName();
        if (name != null) {
            putValue(Action.NAME, name);
        }

        Integer mnemonicKey = getMnemonicKey();
        if (mnemonicKey != null) {
            putValue(Action.MNEMONIC_KEY, mnemonicKey);
        }

        KeyStroke acceleratorKey = getAcceleratorKey();
        if (acceleratorKey != null) {
            putValue(Action.ACCELERATOR_KEY, acceleratorKey);
        }
    }

    public String getName() {
        String nameKey = getBaseKey() + NAME_SUFFIX;
        return MessageUtils.contain(nameKey) ? MessageUtils.getMessage(nameKey) : null;
    }

    public Integer getMnemonicKey() {
        String mnemonicKey = getBaseKey() + MNEMONIC_CHAR_SUFFIX;
        if (MessageUtils.contain(mnemonicKey)) {
            String mnemonicValue = MessageUtils.getMessage(mnemonicKey);
            return KeyEvent.getExtendedKeyCodeForChar(mnemonicValue.charAt(0));
        } else {
            return null;
        }
    }

    public KeyStroke getAcceleratorKey() {
        String keyStrokeKey = getBaseKey() + ACCELERATOR_SUFFIX;
        if (MessageUtils.contain(keyStrokeKey)) {
            String keyStrokeValue = MessageUtils.getMessage(keyStrokeKey);
            return KeyStroke.getKeyStroke(keyStrokeValue);
        } else {
            return null;
        }
    }

    protected String getBaseKey() {
        String className = getClass().getName();
        int startIndex = className.indexOf(ACTION_PREFIX);
        return className.substring(startIndex);
    }
}
