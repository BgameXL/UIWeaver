package dev.uiweaver.api.sync;

import java.util.function.Supplier;

public class SyncDeclaration<T> {

    private final String key;
    private final SyncType type;
    private final Supplier<T> serverSource;
    private final int rateMs;

    private SyncDeclaration(String key, SyncType type, Supplier<T> serverSource, int rateMs) {
        this.key = key;
        this.type = type;
        this.serverSource = serverSource;
        this.rateMs = rateMs;
    }

    public static SyncDeclaration<Integer> ofInt(String key, Supplier<Integer> source) {
        return new SyncDeclaration<>(key, SyncType.INT, source, 0);
    }

    public static SyncDeclaration<Long> ofLong(String key, Supplier<Long> source) {
        return new SyncDeclaration<>(key, SyncType.LONG, source, 0);
    }

    public static SyncDeclaration<Boolean> ofBoolean(String key, Supplier<Boolean> source) {
        return new SyncDeclaration<>(key, SyncType.BOOLEAN, source, 0);
    }

    public static SyncDeclaration<Float> ofFloat(String key, Supplier<Float> source) {
        return new SyncDeclaration<>(key, SyncType.FLOAT, source, 0);
    }

    public static SyncDeclaration<String> ofString(String key, Supplier<String> source) {
        return new SyncDeclaration<>(key, SyncType.STRING, source, 0);
    }

    public SyncDeclaration<T> debounce(int ms) {
        return new SyncDeclaration<>(key, type, serverSource, ms);
    }

    public String getKey() { return key; }
    public SyncType getType() { return type; }
    public Supplier<T> getServerSource() { return serverSource; }
    public int getRateMs() { return rateMs; }
}
