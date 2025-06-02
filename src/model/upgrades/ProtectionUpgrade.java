package model.upgrades;

import model.entities.Pacman;
import util.Resources;

public class ProtectionUpgrade extends Upgrade {

    public ProtectionUpgrade(int x, int y) {
        super(x, y);
    }

    @Override
    protected void applyEffect(Pacman pacman) {
        pacman.setProtected(true);
        pacman.setFrames(Resources.pacmanFramesGrey); // 🔄 set grey frames
    }

    @Override
    protected void revertEffect(Pacman pacman) {
        pacman.setProtected(false);
        pacman.setFrames(Resources.pacmanFrames); // 🔙 revert to yellow
    }
}
