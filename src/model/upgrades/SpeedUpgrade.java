package model.upgrades;

import model.entities.Pacman;

public class SpeedUpgrade extends Upgrade {
    private float previousSpeed;

    public SpeedUpgrade(int x, int y) {
        super(x, y);
    }

    @Override
    protected void applyEffect(Pacman pacman) {
        previousSpeed = pacman.getSpeed();
        pacman.setSpeed(previousSpeed * 1.5f); // +20%
    }

    @Override
    protected void revertEffect(Pacman pacman) {
        pacman.setSpeed(previousSpeed);
    }
}
