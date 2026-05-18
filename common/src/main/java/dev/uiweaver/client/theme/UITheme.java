package dev.uiweaver.client.theme;

import net.minecraft.resources.ResourceLocation;

public class UITheme {

    public static final UITheme DEFAULT = new UITheme.Builder().build();

    private final int backgroundColor;
    private final int borderColor;
    private final int textColor;
    private final int textColorDisabled;
    private final int buttonColor;
    private final int buttonHoverColor;
    private final int energyBarColor;
    private final int fluidBarBackgroundColor;
    private final ResourceLocation panelTexture;

    private UITheme(Builder builder) {
        this.backgroundColor = builder.backgroundColor;
        this.borderColor = builder.borderColor;
        this.textColor = builder.textColor;
        this.textColorDisabled = builder.textColorDisabled;
        this.buttonColor = builder.buttonColor;
        this.buttonHoverColor = builder.buttonHoverColor;
        this.energyBarColor = builder.energyBarColor;
        this.fluidBarBackgroundColor = builder.fluidBarBackgroundColor;
        this.panelTexture = builder.panelTexture;
    }

    public int getBackgroundColor() { return backgroundColor; }
    public int getBorderColor() { return borderColor; }
    public int getTextColor() { return textColor; }
    public int getTextColorDisabled() { return textColorDisabled; }
    public int getButtonColor() { return buttonColor; }
    public int getButtonHoverColor() { return buttonHoverColor; }
    public int getEnergyBarColor() { return energyBarColor; }
    public int getFluidBarBackgroundColor() { return fluidBarBackgroundColor; }
    public ResourceLocation getPanelTexture() { return panelTexture; }

    public static class Builder {
        private int backgroundColor = 0xFF2B2B2B;
        private int borderColor = 0xFF555555;
        private int textColor = 0xFFFFFFFF;
        private int textColorDisabled = 0xFF888888;
        private int buttonColor = 0xFF444444;
        private int buttonHoverColor = 0xFF666666;
        private int energyBarColor = 0xFFFFAA00;
        private int fluidBarBackgroundColor = 0xFF1A1A1A;
        private ResourceLocation panelTexture = null;

        public Builder backgroundColor(int color) { this.backgroundColor = color; return this; }
        public Builder borderColor(int color) { this.borderColor = color; return this; }
        public Builder textColor(int color) { this.textColor = color; return this; }
        public Builder buttonColor(int color) { this.buttonColor = color; return this; }
        public Builder buttonHoverColor(int color) { this.buttonHoverColor = color; return this; }
        public Builder energyBarColor(int color) { this.energyBarColor = color; return this; }
        public Builder panelTexture(ResourceLocation texture) { this.panelTexture = texture; return this; }

        public UITheme build() { return new UITheme(this); }
    }
}