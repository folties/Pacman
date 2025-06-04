package game;

import gui.GamingWindow;
import model.entities.Ghost;
import model.map.BlockType;
import model.upgrades.ExtraLifeUpgrade;
import model.upgrades.ProtectionUpgrade;
import model.upgrades.SpeedUpgrade;
import javax.swing.*;

public class GhostLoop extends Thread {

    private final GamingWindow gamingWindow;
    private final Ghost ghost;
    private final BlockType[][] map;
    private final JLabel ghostLabel;
    private final Logic logic;

    private volatile boolean running = true;
    private volatile boolean paused = false;

    public GhostLoop(Ghost ghost, BlockType[][] map, JLabel ghostLabel, Logic logic, GamingWindow gamingWindow) {
        this.ghost = ghost;
        this.map = map;
        this.ghostLabel = ghostLabel;
        this.logic = logic;
        this.gamingWindow = gamingWindow;
    }

    @Override
    public void run() {
        startUpgradeSpawner();

        while (running) {
            if (paused) {
                sleepSafely(50);
                continue;
            }

            ghost.stepMove(map);
            logic.checkGhostCollision(ghost, gamingWindow);

            SwingUtilities.invokeLater(() ->
                    ghostLabel.setLocation(ghost.getX(), ghost.getY())
            );

            sleepSafely(16);
        }
    }

    private void startUpgradeSpawner() {
        new Thread(() -> {
            while (running) {
                sleepSafely(5000);

                if (paused || logic.isSpeedEffectActive() || logic.isProtectionEffectActive()) continue;

                if (Math.random() < 0.25) {
                    int row = ghost.getRow();
                    int col = ghost.getCol();
                    BlockType current = map[row][col];

                    synchronized (logic) {
                        boolean validSpot = (current == BlockType.EMPTY || current == BlockType.FOOD) && !logic.hasUpgradeAt(col, row);

                        if (validSpot) {
                            double chance = Math.random();

                            if (chance < 0.33 && !logic.hasUncollectedUpgradeOfType(SpeedUpgrade.class)) {
                                logic.getUpgrades().add(new SpeedUpgrade(col, row));
                            } else if (chance < 0.66 && !logic.hasUncollectedUpgradeOfType(ExtraLifeUpgrade.class)) {
                                logic.getUpgrades().add(new ExtraLifeUpgrade(col, row));
                            } else if (!logic.hasUncollectedUpgradeOfType(ProtectionUpgrade.class)) {
                                logic.getUpgrades().add(new ProtectionUpgrade(col, row));
                            }
                        }
                    }
                }
            }
        }).start();
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

    private void sleepSafely(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
