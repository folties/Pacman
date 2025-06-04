package game;

public class TimerLoop extends Thread {

    private int seconds = 0;
    private final Runnable onTick;

    private volatile boolean running = true;
    private volatile boolean paused = false;

    public TimerLoop(Runnable onTick) {
        this.onTick = onTick;
    }

    @Override
    public void run() {
        while (running) {
            if (paused) {
                sleepSafely(50);
                continue;
            }

            sleepSafely(1000); // Wait 1 second

            if (!paused && running) {
                seconds++;
                onTick.run();
            }
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

    public int getSeconds() {
        return seconds;
    }

    private void sleepSafely(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
