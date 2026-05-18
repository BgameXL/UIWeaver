package dev.uiweaver.api.layout;

public final class Size {

    public enum Mode { FIXED, WRAP, FILL }

    public static final Size WRAP = new Size(0, 0, Mode.WRAP, Mode.WRAP, 0, 0, 0, 0, 0);
    public static final Size FILL = new Size(0, 0, Mode.FILL, Mode.FILL, 0, 0, 0, 0, 0);

    private final int width;
    private final int height;
    private final Mode widthMode;
    private final Mode heightMode;
    private final int minWidth;
    private final int minHeight;
    private final int maxWidth;
    private final int maxHeight;
    private final int grow;

    private Size(int width, int height, Mode widthMode, Mode heightMode,
                 int minWidth, int minHeight, int maxWidth, int maxHeight, int grow) {
        this.width      = width;
        this.height     = height;
        this.widthMode  = widthMode;
        this.heightMode = heightMode;
        this.minWidth   = minWidth;
        this.minHeight  = minHeight;
        this.maxWidth   = maxWidth;
        this.maxHeight  = maxHeight;
        this.grow       = grow;
    }

    public static Size fixed(int width, int height) {
        return new Size(width, height, Mode.FIXED, Mode.FIXED, 0, 0, 0, 0, 0);
    }

    public static Size fillWidth(int height) {
        return new Size(0, height, Mode.FILL, Mode.FIXED, 0, 0, 0, 0, 0);
    }

    public static Size fillHeight(int width) {
        return new Size(width, 0, Mode.FIXED, Mode.FILL, 0, 0, 0, 0, 0);
    }

    public static Size wrapWidth(int height) {
        return new Size(0, height, Mode.WRAP, Mode.FIXED, 0, 0, 0, 0, 0);
    }

    public static Size wrapHeight(int width) {
        return new Size(width, 0, Mode.FIXED, Mode.WRAP, 0, 0, 0, 0, 0);
    }

    public Size minWidth(int min)  { return new Size(width, height, widthMode, heightMode, min, minHeight, maxWidth, maxHeight, grow); }
    public Size minHeight(int min) { return new Size(width, height, widthMode, heightMode, minWidth, min, maxWidth, maxHeight, grow); }
    public Size maxWidth(int max)  { return new Size(width, height, widthMode, heightMode, minWidth, minHeight, max, maxHeight, grow); }
    public Size maxHeight(int max) { return new Size(width, height, widthMode, heightMode, minWidth, minHeight, maxWidth, max, grow); }
    public Size grow(int weight)   { return new Size(width, height, widthMode, heightMode, minWidth, minHeight, maxWidth, maxHeight, weight); }
    public Size grow()             { return grow(1); }

    public int width()  { return width; }
    public int height() { return height; }

    public boolean isFixedWidth()  { return widthMode  == Mode.FIXED; }
    public boolean isFixedHeight() { return heightMode == Mode.FIXED; }
    public boolean fillsWidth()    { return widthMode  == Mode.FILL; }
    public boolean fillsHeight()   { return heightMode == Mode.FILL; }
    public boolean wrapsWidth()    { return widthMode  == Mode.WRAP; }
    public boolean wrapsHeight()   { return heightMode == Mode.WRAP; }
    public boolean grows()         { return grow > 0; }

    public int getMinWidth()  { return minWidth; }
    public int getMinHeight() { return minHeight; }
    public int getMaxWidth()  { return maxWidth > 0 ? maxWidth : Integer.MAX_VALUE; }
    public int getMaxHeight() { return maxHeight > 0 ? maxHeight : Integer.MAX_VALUE; }
    public int getGrow()      { return grow; }

    public int clampWidth(int w)  {
        int lo = minWidth > 0 ? minWidth : 0;
        int hi = maxWidth > 0 ? maxWidth : Integer.MAX_VALUE;
        return Math.max(lo, Math.min(hi, w));
    }

    public int clampHeight(int h) {
        int lo = minHeight > 0 ? minHeight : 0;
        int hi = maxHeight > 0 ? maxHeight : Integer.MAX_VALUE;
        return Math.max(lo, Math.min(hi, h));
    }
}