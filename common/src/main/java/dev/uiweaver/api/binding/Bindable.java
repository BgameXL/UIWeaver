package dev.uiweaver.api.binding;

public interface Bindable {

    void applyBinding(String property, Object value);

    boolean supportsProperty(String property);
}
