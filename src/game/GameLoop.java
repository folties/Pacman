package game;

import gui.GamingWindow;
import model.entities.Pacman;

import javax.swing.*;

public class GameLoop extends Thread {
    private final Logic logic;
    private final Runnable onFrameUpdate;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final GamingWindow gamingWindow;



    public GameLoop(Logic logic, GamingWindow gamingWindow, Runnable onFrameUpdate) {
        this.gamingWindow = gamingWindow;
        this.logic = logic;
        this.onFrameUpdate = onFrameUpdate; // що буде виконуватись на кожному кадрі (наприклад: оновлення UI)
    }


    @Override
    public void run() {
        Pacman pacman = logic.getPacman();

        while (running && pacman.getLives() > 0) {
            if (paused) {
                try {
                    Thread.sleep(50);
                    continue;
                } catch (InterruptedException ignored) {}
            }

            logic.update();
            onFrameUpdate.run();

            try {
                Thread.sleep(16);
            } catch (InterruptedException ignored) {}
        }

        // ✅ End game only once, safely:
        if (running && !paused && pacman.getLives() <= 0) {
            SwingUtilities.invokeLater(() -> gamingWindow.endGame());
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
}
