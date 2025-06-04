package gui;

import model.upgrades.Upgrade;
import util.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UpgradeRender {
    public static void render(List<Upgrade> upgrades, JLabel[][] cells) {
        for (Upgrade upgrade : upgrades) {
            if (!upgrade.isCollected()) {
                int x = upgrade.getX();
                int y = upgrade.getY();
                boolean visible = true;

                if (upgrade.isBlinking()) {
                    long time = System.currentTimeMillis();
                    visible = (time / 300) % 2 == 0;
                }

                if (visible) {
                    Image image = switch (upgrade.getClass().getSimpleName()) {
                        case "SpeedUpgrade" -> Resources.speedUpgradeImage;
                        case "ExtraLifeUpgrade" -> Resources.extraLifeUpgradeImage;
                        case "ProtectionUpgrade" -> Resources.protectionUpgradeImage;
                        default -> null;
                    };
                    if (image != null) {
                        cells[y][x].setIcon(new ImageIcon(image));
                    }
                } else {
                    cells[y][x].setIcon(null);
                }
            }
        }
    }
}

