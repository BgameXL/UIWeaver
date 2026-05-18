package dev.uiweaver.api.layout;

public record Bounds(int x, int y, int width, int height) {

    public static final Bounds ZERO = new Bounds(0, 0, 0, 0);

    public static Bounds of(int x, int y, int width, int height) {
        return new Bounds(x, y, width, height);
    }

    public int right() { return x + width; }
    public int bottom() { return y + height; }

    public boolean contains(int px, int py) {
        return px >= x && px < right() && py >= y && py < bottom();
    }

    public Bounds translate(int dx, int dy) {
        return new Bounds(x + dx, y + dy, width, height);
    }

    public Bounds withSize(int w, int h) {
        return new Bounds(x, y, w, h);
    }
}
