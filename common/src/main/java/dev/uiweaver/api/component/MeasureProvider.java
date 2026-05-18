package dev.uiweaver.api.component;

public class MeasureProvider {

    private static TextMeasurer measurer = text -> text.length() * 6; // fallback: ~6px per char

    public static void register(TextMeasurer m) {
        measurer = m;
    }

    public static int textWidth(String text) {
        return measurer.measure(text);
    }

    public static int lineHeight() {
        return 9;
    }

    @FunctionalInterface
    public interface TextMeasurer {
        int measure(String text);
    }
}