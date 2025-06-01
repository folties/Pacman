package game;

/**
 * TimerLoop tracks elapsed game time in seconds.
 * It runs on a separate thread, invoking a callback every second.
 */
public class TimerLoop extends Thread {

    // === Internal State ===
    private int seconds = 0;

    // === Callback ===
    private final Runnable onTick;

    // === Control Flags ===
    private volatile boolean running = true;
    private volatile boolean paused = false;

    // === Constructor ===
    public TimerLoop(Runnable onTick) {
        this.onTick = onTick;
    }

    // === Main Loop ===
    @Override
    public void run() {
        while (running) {
            if (paused) {
                sleepSafely(50);
                continue;
            }

            sleepSafely(1000); // 1 second

            if (!paused && running) {
                seconds++;
                onTick.run();
            }
        }
    }

    // === External Control ===
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

    // === Utility ===
    private void sleepSafely(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
