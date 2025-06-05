package game;

import gui.GamingWindow;
import model.entities.Ghost;
import model.entities.Pacman;
import model.map.BlockType;
import model.map.MapDesign;
import model.map.MapType;
import model.upgrades.*;
import util.Resources;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Logic {

    private final BlockType[][] logicMap;
    private final List<Ghost> ghosts = new ArrayList<>();
    private final List<Upgrade> upgrades = new ArrayList<>();
    private final String[] originalMapLayout;

    private Pacman pacman;
    private Point leftPortalPos;
    private Point rightPortalPos;

    private boolean speedEffectActive = false;
    private boolean protectionEffectActive = false;

    public Logic(int rows, int cols) {
        MapType mapType = determineMapType(rows, cols);
        this.originalMapLayout = Resources.loadMapType(mapType);
        this.logicMap = MapDesign.loadLogicMap(originalMapLayout);

        initializeEntities(originalMapLayout);


        if (pacman != null) {
            pacman.setLogic(this);
            pacman.setPortalPositions(leftPortalPos, rightPortalPos);
        }
    }

    public void update(GamingWindow window) {
        pacman.stepMove(logicMap);

        if (isAllFoodEaten()) {
            startNextLevel();
            window.showCountdown();
        }

        for (Upgrade upgrade : upgrades) {
            if (upgrade.PacmanCollected(pacman.getCol(), pacman.getRow())) {
                upgrade.apply(pacman);
            }
        }
    }

    private void initializeEntities(String[] map) {
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[r].length(); c++) {
                char cell = map[r].charAt(c);
                switch (cell) {
                    case 'I' -> pacman = new Pacman(r, c);
                    case 'L' -> leftPortalPos = new Point(c, r);
                    case 'R' -> rightPortalPos = new Point(c, r);
                    case 'r' -> {
                        Ghost ghost = new Ghost(r, c);
                        ghost.setAnimationFrames(Resources.redGhostFrames);
                        ghosts.add(ghost);
                    }
                    case 'p' -> {
                        Ghost ghost = new Ghost(r, c);
                        ghost.setAnimationFrames(Resources.pinkGhostFrames);
                        ghosts.add(ghost);
                    }
                    case 'o' -> {
                        Ghost ghost = new Ghost(r, c);
                        ghost.setAnimationFrames(Resources.orangeGhostFrames);
                        ghosts.add(ghost);
                    }
                    case 'b' -> {
                        Ghost ghost = new Ghost(r, c);
                        ghost.setAnimationFrames(Resources.blueGhostFrames);
                        ghosts.add(ghost);
                    }
                }
            }
        }

        if (pacman != null) {
            pacman.setLogic(this);
            pacman.setPortalPositions(leftPortalPos, rightPortalPos);
        }

        for (Ghost ghost : ghosts) {
            ghost.setPortalPositions(leftPortalPos, rightPortalPos);
        }
    }

    public void startNextLevel() {
        for (int r = 0; r < logicMap.length; r++) {
            for (int c = 0; c < logicMap[0].length; c++) {
                if (originalMapLayout[r].charAt(c) == ' ') {
                    logicMap[r][c] = BlockType.FOOD;
                }
            }
        }

        pacman.resetPosition();
        pacman.setDirection(pacman.getDirection());

        for (Ghost ghost : ghosts) {
            ghost.resetPosition();
            ghost.levelUpSpeed();
        }

        upgrades.clear();
    }

    public void checkGhostCollision(Ghost ghost, GamingWindow window) {
        if (pacman.isProtected()) return;

        Rectangle ghostRect = new Rectangle(ghost.getX(), ghost.getY(), 45, 45);
        Rectangle pacmanRect = new Rectangle(pacman.getX(), pacman.getY(), 45, 45);

        if (ghostRect.intersects(pacmanRect)) {
            window.updateHeartsUI();
            pacman.loseLife();
            pacman.resetPosition();
            resetAllGhosts();

            if (pacman.getLives() > 0) {
                window.showCountdown();
            }
        }
    }
    public void resetAllGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.resetPosition();
        }
    }

    public boolean isAllFoodEaten() {
        for (BlockType[] row : logicMap) {
            for (BlockType cell : row) {
                if (cell == BlockType.FOOD) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean blockHasUpgrade(int col, int row) {
        for (Upgrade upgrade : upgrades) {
            if (!upgrade.isCollected() && upgrade.getX() == col && upgrade.getY() == row) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUncollectedUpgrade(Class<? extends Upgrade> type) {
        for (Upgrade upgrade : upgrades) {
            if (!upgrade.isCollected() && type.isInstance(upgrade)) {
                return true;
            }
        }
        return false;
    }

    private MapType determineMapType(int rows, int cols) {
        return (rows == 17 && cols == 15) ? MapType.SMALL :
                (rows == 19 && cols == 17) ? MapType.MEDIUM :
                        MapType.LARGE;
    }

    public boolean isSpeedEffectActive() {
        return speedEffectActive;
    }

    public boolean isProtectionEffectActive() {
        return protectionEffectActive;
    }

    public void setSpeedEffectActive(boolean active) {
        this.speedEffectActive = active;
    }

    public void setProtectionEffectActive(boolean active) {
        this.protectionEffectActive = active;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public BlockType[][] getLogicMap() {
        return logicMap;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public Point getLeftPortalPos() {
        return leftPortalPos;
    }

    public Point getRightPortalPos() {
        return rightPortalPos;
    }

    public int getScore() {
        return pacman.getScore();
    }
}
