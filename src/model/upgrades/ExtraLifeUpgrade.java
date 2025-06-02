package model.upgrades;

import model.entities.Pacman;

public class ExtraLifeUpgrade extends Upgrade {
    public ExtraLifeUpgrade(int x, int y) {
        super(x, y);
    }

    @Override
    protected void applyEffect(Pacman pacman) {
        if (pacman.getLives() < 3) {
            pacman.gainLife();
        }
        setCollected(true); // mark as collected
    }

    @Override
    protected void revertEffect(Pacman pacman) {
        // No revert needed for life gain
    }
}
