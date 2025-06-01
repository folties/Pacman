package game;

import model.entities.Ghost;
import model.entities.Pacman;
import model.map.BlockType;

import javax.swing.*;
import java.awt.*;

public class GhostLoop extends Thread {
    private final Ghost ghost;
    private final BlockType[][] map;
    private final JLabel ghostLabel;
    private final Pacman pacman;
    private final Logic logic;
    private volatile boolean running = true;
    private volatile boolean paused = false;

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
            if (paused) {
                try {
                    Thread.sleep(50);
                    continue;
                } catch (InterruptedException ignored) {}
            }
            ghost.stepMove(map);

            // ⛔ Collision detection
            Rectangle ghostRect = new Rectangle(ghost.getX(), ghost.getY(), 45, 45);
            Rectangle pacmanRect = new Rectangle(pacman.getX(), pacman.getY(), 45, 45);

            if (ghostRect.intersects(pacmanRect)) {
                pacman.loseLife();
                System.out.println("🟥 Pacman hit! Lives left: " + pacman.getLives());
                System.out.println("GhostLoop checking: " + this + " | Pacman: " + pacman + " | Lives: " + pacman.getLives());
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

    public void pause() {
        paused = true;
    }

    public void resumeLoop() {
        paused = false;
    }
}
