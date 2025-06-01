package model.upgrades;

import model.entities.Pacman;

public abstract class Upgrade {
    protected final int x, y;
    protected boolean collected = false;

    public Upgrade(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isCollected(int px, int py) {
        return px == x && py == y && !collected;
    }

    public boolean isCollected() {
        return collected;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract void apply(Pacman pacman);
}
