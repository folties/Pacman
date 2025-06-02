package model.upgrades;

import model.entities.Pacman;

public abstract class Upgrade {
    protected final int x, y;
    protected boolean collected = false;

    public Upgrade(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isCollected() {
        return collected;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isCollected(int col, int row) {
        return !collected && this.x == col && this.y == row;
    }

    public final void apply(Pacman pacman) {
        if (!collected) {
            collected = true;
            applyEffect(pacman);

            // Revert after 10 seconds (10000 ms)
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
}
