package dev.uiweaver.api.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UIViewModel {

    private final Map<String, Object> values = new HashMap<>();
    private final Map<String, Long> lastUpdated = new HashMap<>();

    public void put(String key, Object value) {
        values.put(key, value);
        lastUpdated.put(key, System.currentTimeMillis());
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key) {
        return Optional.ofNullable((T) values.get(key));
    }

    public int getInt(String key, int fallback) {
        return this.<Integer>get(key).orElse(fallback);
    }

    public long getLong(String key, long fallback) {
        return this.<Long>get(key).orElse(fallback);
    }

    public boolean getBoolean(String key, boolean fallback) {
        return this.<Boolean>get(key).orElse(fallback);
    }

    public float getFloat(String key, float fallback) {
        return this.<Float>get(key).orElse(fallback);
    }

    public String getString(String key, String fallback) {
        return this.<String>get(key).orElse(fallback);
    }

    public boolean hasKey(String key) {
        return values.containsKey(key);
    }

    public long getLastUpdated(String key) {
        return lastUpdated.getOrDefault(key, 0L);
    }
}
