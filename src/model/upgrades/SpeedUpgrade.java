package model.upgrades;

import game.Logic;
import model.entities.Pacman;

public class SpeedUpgrade extends Upgrade {

    private float previousSpeed;

    public SpeedUpgrade(int x, int y) {
        super(x, y);
    }

    @Override
    protected void applyEffect(Pacman pacman) {
        previousSpeed = pacman.getSpeed();
        pacman.setSpeed(previousSpeed * 1.5f);

        Logic logic = pacman.getLogic();
        if (logic != null) {
            logic.setSpeedEffectActive(true);
        }
    }

    @Override
    protected void revertEffect(Pacman pacman) {
        pacman.revertSpeed(previousSpeed);

        Logic logic = pacman.getLogic();
        if (logic != null) {
            logic.setSpeedEffectActive(false);
        }
    }
}
