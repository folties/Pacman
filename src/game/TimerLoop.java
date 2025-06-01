package game;

public class TimerLoop extends Thread {
    private int seconds = 0;
    private final Runnable onTick;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    public TimerLoop(Runnable onTick) {
        this.onTick = onTick;
    }

    public int getSeconds() {
        return seconds;
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

    @Override
    public void run() {
        while (running) {
            if (paused) {
                try {
                    Thread.sleep(50);
                    continue;
                } catch (InterruptedException ignored) {}
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}

            if (!paused && running) {
                seconds++;
                onTick.run();
            }
        }
    }

}
