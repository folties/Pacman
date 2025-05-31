package game;

public class TimerLoop extends Thread {
    private int seconds = 0;
    private volatile boolean running = true;
    private final Runnable onTick;

    public TimerLoop(Runnable onTick) {
        this.onTick = onTick;
    }

    public int getSeconds() {
        return seconds;
    }

    public void stopLoop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000); // 1 second
            } catch (InterruptedException ignored) {}
            if (!running) break; // ✅ double-check
            seconds++;
            onTick.run();
        }
    }
}
