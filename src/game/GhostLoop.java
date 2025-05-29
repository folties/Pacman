package game;

import model.entities.Ghost;
import model.map.BlockType;

import javax.swing.*;

public class GhostLoop extends Thread {
    private final Ghost ghost;
    private final BlockType[][] map;
    private final JLabel ghostLabel;
    private volatile boolean running = true;

    public GhostLoop(Ghost ghost, BlockType[][] map, JLabel ghostLabel) {
        this.ghost = ghost;
        this.map = map;
        this.ghostLabel = ghostLabel;
    }

    @Override
    public void run() {
        while (running){
            ghost.stepMove(map);
            SwingUtilities.invokeLater(() -> {
                ghostLabel.setLocation(ghost.getX(), ghost.getY());
            });
            try {
                Thread.sleep(200); // speed control
            } catch (InterruptedException ignored) {}
        }
    }
    public void stopLoop() {
        running = false;
    }
}
