package game;

import gui.GamingWindow;
import model.entities.Ghost;
import model.entities.Pacman;
import model.map.BlockType;

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
        while (running) {
            if (paused) {
                sleepSafely(50);
                continue;
            }
            // Move the ghost
            ghost.stepMove(map);
            logic.checkGhostCollision(ghost, gamingWindow);
            // Update ghost position in the UI
            SwingUtilities.invokeLater(() ->
                    ghostLabel.setLocation(ghost.getX(), ghost.getY())
            );

            sleepSafely(16); // speed control (can be modified based on upgrades)
        }
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
