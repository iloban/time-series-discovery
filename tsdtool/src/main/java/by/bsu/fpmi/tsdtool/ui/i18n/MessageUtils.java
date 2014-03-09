package by.bsu.fpmi.tsdtool.ui.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provides utility methods for internationalization.
 */
public final class MessageUtils {
    private static final String MESSAGES_BASE_NAME = "by.bsu.fpmi.tsdtool.resources.i18n.messages";

    private MessageUtils() {
    }

    /**
     * Returns message by key, locale.
     *
     * @param key target key
     * @param locale target locale
     * @param params specified param for message
     * @return message
     */
    public static String getMessage(String key, Locale locale, Object[] params) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGES_BASE_NAME, locale);
        String value = resourceBundle.getString(key);
        if (params == null) {
            return value;
        } else {
            return String.format(locale, value, params);
        }
    }

    /**
     * Returns message by key, locale.
     *
     * @param key target key
     * @param locale target locale
     * @return message
     */
    public static String getMessage(String key, Locale locale) {
        return getMessage(key, locale, null);
    }

    /**
     * Returns message by key and default locale.
     *
     * @param key target key
     * @return message
     */
    public static String getMessage(String key) {
        return getMessage(key, null, null);
    }

    /**
     * Returns true if message file contains key.
     *
     * @param key target key
     * @param locale target locale
     * @return true if contained
     */
    public static boolean contain(String key, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGES_BASE_NAME, locale);
        return resourceBundle.containsKey(key);
    }

    /**
     * Returns true if message file contains key.
     *
     * @param key target key
     * @return true if contained
     */
    public static boolean contain(String key) {
        return contain(key, null);
    }
}
