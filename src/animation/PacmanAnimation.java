package animation;

import model.entities.Pacman;
import util.Direction;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PacmanAnimation extends Thread {
    private final JLabel pacmanLabel;
    private final Pacman pacman;
    private final Map<Direction, Image[]> directionFrames;
    private int frameIndex = 0;
    private boolean running = true;

    public PacmanAnimation(JLabel pacmanLabel, Pacman pacman, Map<Direction, Image[]> directionFrames) {
        this.pacmanLabel = pacmanLabel;
        this.pacman = pacman;
        this.directionFrames = directionFrames;
    }

    public void stopAnimation() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            Direction direction = pacman.getDirection();
            Image[] frames = directionFrames.get(direction);
            if (frames != null && frames.length > 0) {
                SwingUtilities.invokeLater(() ->
                        pacmanLabel.setIcon(new ImageIcon(frames[frameIndex]))
                );

                frameIndex = (frameIndex + 1) % frames.length;
            }

            try {
                Thread.sleep(100); // adjust speed as needed
            } catch (InterruptedException ignored) {}
        }
    }
}
