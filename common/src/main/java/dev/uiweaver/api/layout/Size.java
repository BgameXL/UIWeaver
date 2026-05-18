package dev.uiweaver.api.layout;

public record Size(int width, int height, SizeMode widthMode, SizeMode heightMode) {

    public static final Size WRAP = new Size(0, 0, SizeMode.WRAP, SizeMode.WRAP);
    public static final Size FILL = new Size(0, 0, SizeMode.FILL, SizeMode.FILL);

    public static Size fixed(int width, int height) {
        return new Size(width, height, SizeMode.FIXED, SizeMode.FIXED);
    }

    public static Size fillWidth(int height) {
        return new Size(0, height, SizeMode.FILL, SizeMode.FIXED);
    }

    public static Size wrapWidth(int height) {
        return new Size(0, height, SizeMode.WRAP, SizeMode.FIXED);
    }

    public boolean isFixedWidth() { return widthMode == SizeMode.FIXED; }
    public boolean isFixedHeight() { return heightMode == SizeMode.FIXED; }
    public boolean fillsWidth() { return widthMode == SizeMode.FILL; }
    public boolean fillsHeight() { return heightMode == SizeMode.FILL; }

    public enum SizeMode { FIXED, WRAP, FILL }
}
