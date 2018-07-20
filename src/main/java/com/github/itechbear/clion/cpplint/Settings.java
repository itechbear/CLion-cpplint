package com.github.itechbear.clion.cpplint;

import com.intellij.ide.util.PropertiesComponent;

/**
 * Created by HD on 2015/1/2.
 */
public class Settings {
    private static final PropertiesComponent INSTANCE = PropertiesComponent.getInstance();

    public static void set(String key, String value) {
        INSTANCE.setValue(key, value);
    }

    public static String get(String key) {
        return INSTANCE.getValue(key);
    }
}
