package dev.uiweaver.api.layout;

public record Insets(int top, int right, int bottom, int left) {

    public static final Insets NONE = new Insets(0, 0, 0, 0);

    public static Insets all(int v) {
        return new Insets(v, v, v, v);
    }

    public static Insets of(int vertical, int horizontal) {
        return new Insets(vertical, horizontal, vertical, horizontal);
    }

    public int horizontal() { return left + right; }
    public int vertical() { return top + bottom; }
}
