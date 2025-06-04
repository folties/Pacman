package game;

import gui.GamingWindow;
import model.entities.Pacman;
import javax.swing.*;

public class PacmanLoop extends Thread {


    private final Logic logic;
    private final GamingWindow gamingWindow;
    private final Runnable onFrameUpdate;


    private volatile boolean running = true;
    private volatile boolean paused = false;


    public PacmanLoop(Logic logic, GamingWindow gamingWindow, Runnable onFrameUpdate) {
        this.logic = logic;
        this.gamingWindow = gamingWindow;
        this.onFrameUpdate = onFrameUpdate;
    }

    @Override
    public void run() {
        Pacman pacman = logic.getPacman();

        while (running && pacman.getLives() > 0) {
            if (paused) {
                sleepSafely(50);
                continue;
            }

            logic.update(gamingWindow);
            onFrameUpdate.run();

            sleepSafely(16);
        }
        if (running && !paused && pacman.getLives() <= 0) {
            SwingUtilities.invokeLater(gamingWindow::endGame);
        }
    }

    public void pause() {
        paused = true;
    }

    public void resumeLoop() {
        paused = false;
    }

    public void stopLoop() {
        running = false;
    }

    private void sleepSafely(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
