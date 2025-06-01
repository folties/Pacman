package game;

import gui.GamingWindow;
import model.entities.Pacman;

import javax.swing.*;

/**
 * GameLoop controls the main update cycle of the game,
 * including logic updates and UI frame refreshes.
 * It runs in a separate thread and supports pause/resume functionality.
 */
public class PacmanLoop extends Thread {

    // === Core References ===
    private final Logic logic;
    private final GamingWindow gamingWindow;
    private final Runnable onFrameUpdate;

    // === Control Flags ===
    private volatile boolean running = true;
    private volatile boolean paused = false;

    // === Constructor ===
    public PacmanLoop(Logic logic, GamingWindow gamingWindow, Runnable onFrameUpdate) {
        this.logic = logic;
        this.gamingWindow = gamingWindow;
        this.onFrameUpdate = onFrameUpdate;
    }

    // === Main Loop ===
    @Override
    public void run() {
        Pacman pacman = logic.getPacman();

        while (running && pacman.getLives() > 0) {
            if (paused) {
                sleepSafely(50);
                continue;
            }

            logic.update(gamingWindow);       // update game logic (movement, collision, etc.)
            onFrameUpdate.run();  // update UI on the EDT (through SwingUtilities in caller)

            sleepSafely(16); // ~60 FPS
        }

        // End game only if not paused and lives are exhausted
        if (running && !paused && pacman.getLives() <= 0) {
            SwingUtilities.invokeLater(gamingWindow::endGame);
        }
    }

    // === External Control Methods ===
    public void pause() {
        paused = true;
    }

    public void resumeLoop() {
        paused = false;
    }

    public void stopLoop() {
        running = false;
    }

    // === Utility ===
    private void sleepSafely(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
