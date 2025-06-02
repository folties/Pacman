package game;

import gui.GamingWindow;
import model.entities.Ghost;
import model.entities.Pacman;
import model.map.BlockType;
import model.upgrades.ExtraLifeUpgrade;
import model.upgrades.ProtectionUpgrade;
import model.upgrades.SpeedUpgrade;

import javax.swing.*;
import java.awt.*;

/**
 * GhostLoop controls the movement and collision logic for a single ghost.
 * Each ghost runs in its own thread and updates its position independently.
 */
public class GhostLoop extends Thread {
    private final GamingWindow gamingWindow;

    // === Core Game References ===
    private final Ghost ghost;
    private final BlockType[][] map;
    private final JLabel ghostLabel;
    private final Pacman pacman;
    private final Logic logic;

    // === Control Flags ===
    private volatile boolean running = true;
    private volatile boolean paused = false;

    // === Constructor ===
    public GhostLoop(Ghost ghost, BlockType[][] map, JLabel ghostLabel, Pacman pacman, Logic logic, GamingWindow gamingWindow) {
        this.ghost = ghost;
        this.map = map;
        this.ghostLabel = ghostLabel;
        this.pacman = pacman;
        this.logic = logic;
        this.gamingWindow = gamingWindow;
    }

    // === Main Loop ===
    @Override
    public void run() {
        startUpgradeThread();

        while (running) {
            if (paused) {
                sleepSafely(50);
                continue;
            }

            // Move the ghost
            ghost.stepMove(map);

            logic.checkGhostCollision(ghost, gamingWindow);

            // Update UI
            SwingUtilities.invokeLater(() ->
                    ghostLabel.setLocation(ghost.getX(), ghost.getY())
            );

            sleepSafely(16);
        }
    }


    private void startUpgradeThread() {
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(5000); // wait 5 seconds
                } catch (InterruptedException ignored) {}

                if (!paused && Math.random() < 0.25) {
                    int row = ghost.getRow();
                    int col = ghost.getCol();
                    BlockType current = map[row][col];

                    synchronized (logic) { // 🔒 thread-safe check
                        boolean validSpot = (current == BlockType.EMPTY)
                                && !logic.hasUpgradeAt(col, row);

                        if (validSpot) {
                            double chance = Math.random();

                            if (chance < 0.33 && !logic.hasUncollectedSpeedUpgrade()) {
                                logic.getUpgrades().add(new SpeedUpgrade(col, row));
                                System.out.println("💎 Dropped SpeedUpgrade at: " + col + "," + row);
                            } else if (chance < 0.66 && !logic.hasUncollectedExtraLifeUpgrade()) {
                                logic.getUpgrades().add(new ExtraLifeUpgrade(col, row));
                                System.out.println("❤️ Dropped ExtraLifeUpgrade at: " + col + "," + row);
                            } else if (!logic.hasUncollectedProtectionUpgrade()) {
                                logic.getUpgrades().add(new ProtectionUpgrade(col, row));
                                System.out.println("🛡️ Dropped ProtectionUpgrade at: " + col + "," + row);
                            }
                        }
                    }
                }
            }
        }).start();
    }




    // === External Control Methods ===
    public void stopLoop() {
        running = false;
    }

    public void pause() {
        paused = true;
    }

    public void resumeLoop() {
        paused = false;
    }

    // === Utility ===
    private void sleepSafely(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
