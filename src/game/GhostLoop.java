package game;

import model.entities.Ghost;
import model.entities.Pacman;
import model.map.BlockType;

import javax.swing.*;

public class GhostLoop extends Thread {
    private final Ghost ghost;
    private final BlockType[][] map;
    private final JLabel ghostLabel;
    private volatile boolean running = true;
    private final Pacman pacman;
    private final Logic logic;

    public GhostLoop(Ghost ghost, BlockType[][] map, JLabel ghostLabel, Pacman pacman, Logic logic) {

        this.ghost = ghost;
        this.map = map;
        this.ghostLabel = ghostLabel;
        this.pacman = pacman;
        this.logic = logic;
    }

    @Override
    public void run() {
        while (running){
            ghost.stepMove(map);

            // ⛔ Collision detection
            if (Math.abs(ghost.getX() - pacman.getX()) < 30 &&
                    Math.abs(ghost.getY() - pacman.getY()) < 30) {
                pacman.loseLife();
                pacman.resetPosition();
                logic.resetAllGhosts();

            }

            SwingUtilities.invokeLater(() -> {
                ghostLabel.setLocation(ghost.getX(), ghost.getY());
            });
            try {
                Thread.sleep(100); // speed control
            } catch (InterruptedException ignored) {}
        }
    }
    public void stopLoop() {
        running = false;
    }
}
