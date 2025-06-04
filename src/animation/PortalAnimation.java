package animation;

import javax.swing.*;
import java.awt.*;

public class PortalAnimation extends AbstractAnimation {
    private final Image[] frames;

    public PortalAnimation(JLabel label, Image[] frames) {
        super(label);
        this.frames = frames;
    }

    @Override
    protected void updateSprite() {
        SwingUtilities.invokeLater(() ->
                label.setIcon(new ImageIcon(frames[frameIndex]))
        );
        frameIndex = (frameIndex + 1) % frames.length;
    }

    @Override
    protected int getFrameDelay() {
        return 150;
    }
}
