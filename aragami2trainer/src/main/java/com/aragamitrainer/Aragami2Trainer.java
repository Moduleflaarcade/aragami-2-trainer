package com.aragamitrainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Main trainer application for Aragami 2.
 * Provides a simple console interface to toggle cheats.
 * Memory addresses are placeholders — real offsets would be found via reverse engineering.
 */
public class Aragami2Trainer {

    private final MemoryManager memoryManager;
    private final Map<String, TrainerFeature> features;

    public Aragami2Trainer() {
        System.out.println("Initializing Aragami 2 Trainer...");
        memoryManager = new MemoryManager();
        features = new HashMap<>();

        // Register features with placeholder addresses (0x12345678 etc.)
        // In a real trainer, these would be dynamic offsets resolved from base address + patterns.
        features.put("infinite_health", new TrainerFeature("Infinite Health", 0x12345678L, 9999, 100, memoryManager));
        features.put("infinite_essence", new TrainerFeature("Infinite Essence", 0x1234567CL, 9999, 0, memoryManager));
        features.put("one_hit_kill", new TrainerFeature("One-Hit Kill", 0x12345680L, 1, 0, memoryManager));
        features.put("no_stealth_cooldown", new TrainerFeature("No Stealth Cooldown", 0x12345684L, 0, 100, memoryManager));
    }

    /**
     * Starts the interactive console loop for toggling features.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Aragami 2 Trainer ready. Commands: list, toggle <name>, exit");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("exit")) {
                break;
            } else if (input.equals("list")) {
                System.out.println("Available features:");
                for (TrainerFeature feature : features.values()) {
                    System.out.println("  " + feature.getName() + " (" + (feature.isEnabled() ? "ENABLED" : "DISABLED") + ")");
                }
            } else if (input.startsWith("toggle ")) {
                String featureName = input.substring(7).trim();
                TrainerFeature feature = features.get(featureName);
                if (feature != null) {
                    feature.toggle();
                } else {
                    System.out.println("Unknown feature: " + featureName);
                }
            } else {
                System.out.println("Unknown command. Use: list, toggle <name>, exit");
            }
        }

        scanner.close();
        memoryManager.close();
        System.out.println("Trainer closed.");
    }

    public static void main(String[] args) {
        try {
            Aragami2Trainer trainer = new Aragami2Trainer();
            trainer.run();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
