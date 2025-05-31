package game;

import model.entities.Pacman;

import javax.swing.*;

public class GameLoop extends Thread {
    private final Logic logic;
    private final Runnable onFrameUpdate;
    private volatile boolean running = true;

    public GameLoop(Logic logic, Runnable onFrameUpdate) {
        this.logic = logic;
        this.onFrameUpdate = onFrameUpdate; // що буде виконуватись на кожному кадрі (наприклад: оновлення UI)
    }

    public void stopLoop() {
        running = false;
    }

    @Override
    public void run() {
        Pacman pacman = logic.getPacman();
        while (running && pacman.getLives() >= 0) {
            logic.update();
            onFrameUpdate.run();

            try {
                Thread.sleep(16); // або 16 — для 60 fps
            } catch (InterruptedException ignored) {}
        }
    }
}
