package dev.uiweaver.runtime.registry;

import dev.uiweaver.api.UIBuilder;
import dev.uiweaver.api.UIScreen;
import dev.uiweaver.api.spec.UIScreenSpec;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UIRegistry {

    private static final Map<String, UIScreen> FACTORIES = new HashMap<>();

    public static void register(String screenId, UIScreen factory) {
        FACTORIES.put(screenId, factory);
    }

    public static void registerFactory(String screenId, UIScreen factory) {
        FACTORIES.put(screenId, factory);
    }

    public static UIScreen getFactory(String screenId) {
        return FACTORIES.get(screenId);
    }

    public static UIScreenSpec get(String screenId) {
        UIScreen factory = FACTORIES.get(screenId);
        if (factory == null) return null;
        return factory.build(UIBuilder.screen(screenId));
    }

    public static boolean contains(String screenId) {
        return FACTORIES.containsKey(screenId);
    }

    public static Collection<String> allIds() {
        return Collections.unmodifiableCollection(FACTORIES.keySet());
    }
}