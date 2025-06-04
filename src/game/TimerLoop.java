package game;

public class TimerLoop extends Thread {

    private int seconds = 0;
    private final Runnable timeUpdater;

    private volatile boolean running = true;
    private volatile boolean paused = false;

    public TimerLoop(Runnable timeUpdater) {
        this.timeUpdater = timeUpdater;
    }

    @Override
    public void run() {
        while (running) {
            if (paused) {
                sleep(50);
                continue;
            }

            sleep(1000);

            if (!paused && running) {
                seconds++;
                timeUpdater.run();
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

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("timer thread goes wrong: " + e.getMessage());
        }
    }
}
