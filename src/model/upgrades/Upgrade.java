package model.upgrades;

import model.entities.Pacman;

public abstract class Upgrade {
    protected final int x, y;
    protected boolean collected = false;
    protected boolean blinking = false;

    public Upgrade(int x, int y) {
        this.x = x;
        this.y = y;

        new Thread(() -> {
            try {
                Thread.sleep(10000);
                blinking = true;

                Thread.sleep(5000);
                if (!collected) {
                    collected = true;
                    System.out.println("⏳ Upgrade at " + x + "," + y + " expired");
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }

    public final void apply(Pacman pacman) {
        if (!collected) {
            collected = true;
            applyEffect(pacman);

            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    revertEffect(pacman);
                } catch (InterruptedException ignored) {}
            }).start();
        }
    }

    protected abstract void applyEffect(Pacman pacman);
    protected abstract void revertEffect(Pacman pacman);

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isCollected() { return collected; }
    public boolean isBlinking() { return blinking; }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isCollected(int col, int row) {
        return !collected && this.x == col && this.y == row;
    }
}
