package dev.uiweaver.client.theme;

import java.util.HashMap;
import java.util.Map;

public class ThemeRegistry {

    private static final Map<String, UITheme> THEMES = new HashMap<>();
    private static String activeThemeId = "default";

    static {
        THEMES.put("default", UITheme.DEFAULT);
    }

    public static void register(String id, UITheme theme) {
        THEMES.put(id, theme);
    }

    public static UITheme get(String id) {
        return THEMES.getOrDefault(id, UITheme.DEFAULT);
    }

    public static UITheme getActive() {
        return get(activeThemeId);
    }

    public static void setActive(String id) {
        activeThemeId = id;
    }
}