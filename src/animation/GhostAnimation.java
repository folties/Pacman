package animation;

import model.entities.Ghost;
import util.Direction;

import javax.swing.*;
import java.awt.*;

public class GhostAnimation extends Thread {
    private final JLabel ghostLabel;
    private final Ghost ghost;
    private int frameIndex = 0;
    private boolean running = true;

    public GhostAnimation(JLabel ghostLabel, Ghost ghost) {
        this.ghostLabel = ghostLabel;
        this.ghost = ghost;
    }

    @Override
    public void run() {
        while (running) {
            Direction direction = ghost.getDirection();
            Image[] frames = ghost.getFramesForDirection(direction);

            if (frames != null && frames.length > 0) {
                ghostLabel.setIcon(new ImageIcon(frames[frameIndex]));
                frameIndex = (frameIndex + 1) % frames.length;
            }

            ghostLabel.setLocation(ghost.getX(), ghost.getY());

            try {
                Thread.sleep(120); // adjust for animation speed
            } catch (InterruptedException ignored) {}
        }
    }

    public void stopAnimation() {
        running = false;
    }
}

