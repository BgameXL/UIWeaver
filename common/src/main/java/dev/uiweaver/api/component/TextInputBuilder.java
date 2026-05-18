package dev.uiweaver.api.component;

import java.util.function.Predicate;

public class TextInputBuilder extends ComponentBuilder<TextInputBuilder> {

    private String placeholder    = "";
    private int maxLength         = 64;
    private Predicate<String> filter = null;
    private String onChangeAction = null;
    private String onSubmitAction = null;

    public TextInputBuilder placeholder(String p)      { this.placeholder    = p; return this; }
    public TextInputBuilder maxLength(int n)           { this.maxLength      = n; return this; }
    public TextInputBuilder filter(Predicate<String> f){ this.filter         = f; return this; }
    public TextInputBuilder onChange(String actionId)  { this.onChangeAction = actionId; return this; }
    public TextInputBuilder onSubmit(String actionId)  { this.onSubmitAction = actionId; return this; }

    @Override
    public TextInputComponent build() {
        return new TextInputComponent(id, visible, enabled, preferredSize,
                placeholder, maxLength, filter, onChangeAction, onSubmitAction);
    }
}