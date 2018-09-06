package com.horyu1234.kkutuweb;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by horyu on 2018-01-13
 */
public class Locale {
    private static final Logger LOGGER = Logger.getLogger(Locale.class);
    private static final String MESSAGE_FILE = "locale.messages";
    private static final ResourceBundle messageBundle = ResourceBundle.getBundle(MESSAGE_FILE);

    private Locale() {
        throw new IllegalStateException("Message class");
    }

    public static String getString(String key) {
        return getEncodedString(messageBundle.getString(key));
    }

    public static String getString(String key, Object... params) {
        return MessageFormat.format(getString(key), params);
    }

    public static Map<String, String> getWebLocales() {
        Map<String, String> webLocales = new HashMap<>();

        Enumeration<String> keys = messageBundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = getString(key);

            if (!key.startsWith("web.")) {
                continue;
            }

            key = key.replace("web.", "");

            webLocales.put(key, value);
        }

        return webLocales;
    }

    private static String getEncodedString(String original) {
        try {
            return new String(original.getBytes("8859_1"), StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e);
        }

        return "ERROR";
    }
}
