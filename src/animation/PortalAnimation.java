package animation;

import javax.swing.*;
import java.awt.*;

public class PortalAnimation extends Thread {
    private final JLabel label;
    private final Image[] frames;
    private int index = 0;
    private boolean running = true;

    public PortalAnimation(JLabel label, Image[] frames) {
        this.label = label;
        this.frames = frames;
    }

    public void stopAnimation() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            SwingUtilities.invokeLater(() ->
                    label.setIcon(new ImageIcon(frames[index]))
            );
            index = (index + 1) % frames.length;
            try {
                Thread.sleep(150);
            } catch (InterruptedException ignored) {}
        }
    }
}
