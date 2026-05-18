package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;

import java.util.function.Predicate;

public class TextInputComponent extends AbstractComponent {

    private String value = "";
    private final String placeholder;
    private final int maxLength;
    private final Predicate<String> filter;
    private final String onChangeAction;

    public TextInputComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                              String placeholder, int maxLength,
                              Predicate<String> filter, String onChangeAction) {
        super(id, visible, enabled, preferredSize);
        this.placeholder = placeholder;
        this.maxLength = maxLength;
        this.filter = filter;
        this.onChangeAction = onChangeAction;
    }

    @Override public ComponentType getType() { return ComponentType.TEXT_INPUT; }

    public boolean tryInsert(String text) {
        String next = value + text;
        if (next.length() > maxLength) return false;
        if (filter != null && !filter.test(next)) return false;
        value = next;
        return true;
    }

    public boolean tryDelete() {
        if (value.isEmpty()) return false;
        value = value.substring(0, value.length() - 1);
        return true;
    }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getPlaceholder() { return placeholder; }
    public int getMaxLength() { return maxLength; }
    public String getOnChangeAction() { return onChangeAction; }
}