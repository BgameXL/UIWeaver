package dev.uiweaver.api.component;

import dev.uiweaver.api.binding.Bindable;
import dev.uiweaver.api.layout.Size;

public class EnergyBarComponent extends AbstractComponent implements Bindable {

    private long energy;
    private long maxEnergy;
    private final String energyKey;
    private final String maxEnergyKey;

    public EnergyBarComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                              String energyKey, String maxEnergyKey) {
        super(id, visible, enabled, preferredSize);
        this.energyKey = energyKey;
        this.maxEnergyKey = maxEnergyKey;
    }

    @Override public ComponentType getType() { return ComponentType.ENERGY_BAR; }

    @Override
    public void applyBinding(String property, Object value) {
        switch (property) {
            case "energy" -> energy = ((Number) value).longValue();
            case "maxEnergy" -> maxEnergy = ((Number) value).longValue();
        }
    }

    @Override
    public boolean supportsProperty(String property) {
        return property.equals("energy") || property.equals("maxEnergy");
    }

    public long getEnergy() { return energy; }
    public long getMaxEnergy() { return maxEnergy; }
    public String getEnergyKey() { return energyKey; }
    public String getMaxEnergyKey() { return maxEnergyKey; }

    public float getFillFraction() {
        if (maxEnergy <= 0) return 0f;
        return Math.min(1f, (float) energy / maxEnergy);
    }
}