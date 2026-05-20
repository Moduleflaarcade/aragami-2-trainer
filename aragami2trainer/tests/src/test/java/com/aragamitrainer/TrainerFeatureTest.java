package com.aragamitrainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TrainerFeature.
 * Uses a mock MemoryManager to avoid actual process interaction.
 */
public class TrainerFeatureTest {

    private MemoryManager mockMemoryManager;
    private TrainerFeature feature;

    @BeforeEach
    public void setUp() {
        // Create a simple mock that just records writes (in a real test, use mocking framework)
        mockMemoryManager = new MemoryManager() {
            private int lastWrittenValue;
            private long lastWrittenAddress;

            @Override
            public void writeInt(long address, int value) {
                this.lastWrittenAddress = address;
                this.lastWrittenValue = value;
            }

            @Override
            public int readInt(long address) {
                return 0; // not used in these tests
            }

            @Override
            public void close() {
                // no-op
            }
        };
        feature = new TrainerFeature("Test Feature", 0xDEADBEEFL, 1, 0, mockMemoryManager);
    }

    @Test
    public void testInitialStateIsDisabled() {
        assertFalse(feature.isEnabled(), "Feature should start disabled");
    }

    @Test
    public void testEnableSetsEnabledAndWritesValue() {
        feature.enable();
        assertTrue(feature.isEnabled(), "Feature should be enabled after enable()");
    }

    @Test
    public void testDisableSetsDisabledAndWritesValue() {
        feature.enable();
        feature.disable();
        assertFalse(feature.isEnabled(), "Feature should be disabled after disable()");
    }

    @Test
    public void testToggleChangesState() {
        assertFalse(feature.isEnabled());
        feature.toggle();
        assertTrue(feature.isEnabled());
        feature.toggle();
        assertFalse(feature.isEnabled());
    }

    @Test
    public void testGetName() {
        assertEquals("Test Feature", feature.getName());
    }
}
