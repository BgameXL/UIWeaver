package dev.uiweaver.api.component;

import java.util.function.Predicate;

public class TextInputBuilder extends ComponentBuilder<TextInputBuilder> {

    private String placeholder = "";
    private int maxLength = 64;
    private Predicate<String> filter = null;
    private String onChangeAction = null;

    public TextInputBuilder placeholder(String p) { this.placeholder = p; return this; }
    public TextInputBuilder maxLength(int n) { this.maxLength = n; return this; }
    public TextInputBuilder filter(Predicate<String> f) { this.filter = f; return this; }
    public TextInputBuilder onChangeAction(String id) { this.onChangeAction = id; return this; }

    @Override
    public TextInputComponent build() {
        return new TextInputComponent(id, visible, enabled, preferredSize,
                placeholder, maxLength, filter, onChangeAction);
    }
}