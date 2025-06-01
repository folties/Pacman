package model.upgrades;

import model.entities.Pacman;

public class SpeedUpgrade extends Upgrade {

    public SpeedUpgrade(int x, int y) {
        super(x, y);
    }

    @Override
    public void apply(Pacman pacman) {
        if (collected) return;

        float currentSpeed = pacman.getSpeed();
        pacman.setSpeed(currentSpeed * 3);  // +20% speed boost
        collected = true;
    }
}
