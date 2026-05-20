package com.aragamitrainer;

/**
 * Represents a single toggleable trainer feature (e.g., infinite health, one-hit kill).
 * Each feature knows its memory address and can be enabled/disabled.
 */
public class TrainerFeature {

    private final String name;
    private final long address;
    private final int enableValue;
    private final int disableValue;
    private boolean enabled;
    private final MemoryManager memoryManager;

    /**
     * Creates a new trainer feature.
     *
     * @param name          display name of the feature.
     * @param address       memory address of the value to modify.
     * @param enableValue   value to write when enabling.
     * @param disableValue  value to write when disabling.
     * @param memoryManager the memory manager instance.
     */
    public TrainerFeature(String name, long address, int enableValue, int disableValue, MemoryManager memoryManager) {
        this.name = name;
        this.address = address;
        this.enableValue = enableValue;
        this.disableValue = disableValue;
        this.enabled = false;
        this.memoryManager = memoryManager;
    }

    /**
     * Enables the feature by writing the enable value to memory.
     */
    public void enable() {
        memoryManager.writeInt(address, enableValue);
        enabled = true;
        System.out.println("[TrainerFeature] " + name + " enabled.");
    }

    /**
     * Disables the feature by writing the disable value to memory.
     */
    public void disable() {
        memoryManager.writeInt(address, disableValue);
        enabled = false;
        System.out.println("[TrainerFeature] " + name + " disabled.");
    }

    /**
     * Toggles the feature state.
     */
    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    /**
     * Returns whether the feature is currently enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns the name of the feature.
     */
    public String getName() {
        return name;
    }
}
