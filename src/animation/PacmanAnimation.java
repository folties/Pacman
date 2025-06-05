package animation;

import model.entities.Pacman;
import util.Direction;
import javax.swing.*;
import java.awt.*;

public class PacmanAnimation extends AbstractAnimation {
    private final Pacman pacman;

    public PacmanAnimation(JLabel label, Pacman pacman) {
        super(label);
        this.pacman = pacman;
    }

    @Override
    protected void updateSprite() {
        Direction dir = pacman.getDirection();
        Image[] frames = pacman.getFrames().get(dir);

        if (frames != null && frames.length > 0) {
            SwingUtilities.invokeLater(() ->
                    label.setIcon(new ImageIcon(frames[frameIndex]))
            );
            frameIndex = (frameIndex + 1) % frames.length;
        }
    }

    @Override
    protected int getFrameDelay() {
        return 100;
    }
}
