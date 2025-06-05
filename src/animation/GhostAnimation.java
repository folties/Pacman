package animation;

import model.entities.Ghost;
import util.Direction;
import javax.swing.*;
import java.awt.*;

public class GhostAnimation extends AbstractAnimation {
    private final Ghost ghost;

    public GhostAnimation(JLabel label, Ghost ghost) {
        super(label);
        this.ghost = ghost;
    }

    @Override
    protected void updateSprite() {
        Direction dir = ghost.getDirection();
        Image[] frames = ghost.getFramesForDirection(dir);

        if (frames != null && frames.length > 0) {
            SwingUtilities.invokeLater(() ->
                    label.setIcon(new ImageIcon(frames[frameIndex]))
            );
            frameIndex = (frameIndex + 1) % frames.length;
        }
    }

    @Override
    protected int getFrameDelay() {
        return 120;
    }
}
