package animation;

import javax.swing.*;

public abstract class AbstractAnimation extends Thread {
    protected final JLabel label;
    protected int frameIndex = 0;
    protected boolean running = true;

    public AbstractAnimation(JLabel label) {
        this.label = label;
    }

    @Override
    public void run() {
        while (running) {
            updateSprite();
            sleepThread();
        }
    }

    protected void sleepThread() {
        try {
            Thread.sleep(getFrameDelay());
        } catch (InterruptedException ignored) {}
    }

    protected abstract void updateSprite();
    protected abstract int getFrameDelay();
}
