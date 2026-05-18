package dev.uiweaver.api.component;

public class EnergyBarBuilder extends ComponentBuilder<EnergyBarBuilder> {

    private String energyKey;
    private String maxEnergyKey;

    public EnergyBarBuilder bind(String energyKey, String maxEnergyKey) {
        this.energyKey = energyKey;
        this.maxEnergyKey = maxEnergyKey;
        return this;
    }

    @Override
    public EnergyBarComponent build() {
        return new EnergyBarComponent(id, visible, enabled, preferredSize, energyKey, maxEnergyKey);
    }
}