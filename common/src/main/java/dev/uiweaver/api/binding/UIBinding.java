package dev.uiweaver.api.binding;

public record UIBinding(String componentId, String property, String syncKey) {

    public static UIBinding of(String componentId, String property, String syncKey) {
        return new UIBinding(componentId, property, syncKey);
    }
}
