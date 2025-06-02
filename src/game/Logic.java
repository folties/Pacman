package game;

import gui.GamingWindow;
import model.entities.Ghost;
import model.entities.Pacman;
import model.map.BlockType;
import model.map.MapDesign;
import model.map.MapType;
import model.upgrades.ExtraLifeUpgrade;
import model.upgrades.ProtectionUpgrade;
import model.upgrades.SpeedUpgrade;
import model.upgrades.Upgrade;
import util.Resources;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Logic handles core game logic such as initializing map elements,
 * controlling Pacman's movement, and resetting ghost states.
 */
public class Logic {

    // === Map and Entities ===
    private final BlockType[][] logicMap;
    private Pacman pacman;
    private final List<Ghost> ghosts = new ArrayList<>();
    private final String[] originalMapLayout;

    // === Portals ===
    private Point leftPortalPos;
    private Point rightPortalPos;

    private final List<Upgrade> upgrades = new ArrayList<>();


    // === Constructor ===
    public Logic(int rows, int cols) {
        MapType mapType = determineMapType(rows, cols);
        String[] currentBlockMap = Resources.loadMapType(mapType);
        this.originalMapLayout = currentBlockMap;
        logicMap = MapDesign.loadLogicMap(currentBlockMap);

        initializePacmanAndPortals(currentBlockMap);
        initializeGhosts(currentBlockMap);

        // Assign portal positions to Pacman
        if (pacman != null) {
            pacman.setPortalPositions(leftPortalPos, rightPortalPos);
        }
    }

    // === Public Methods ===

    /** Updates game state each frame (Pacman only; ghosts handled separately). */
    public void update(GamingWindow window) {
        pacman.stepMove(logicMap);

        if (isAllFoodEaten()) {
            startNextLevel();
            window.showCountdownThenResume();
        }

        for (Upgrade upgrade : upgrades) {
            if (upgrade.isCollected(pacman.getCol(), pacman.getRow())) {
                upgrade.apply(pacman);
            }
        }
    }

    public boolean hasUncollectedSpeedUpgrade() {
        for (Upgrade upgrade : upgrades) {
            if (!upgrade.isCollected() && upgrade instanceof SpeedUpgrade) {
                return true;
            }
        }
        return false;
    }
    public boolean hasUncollectedExtraLifeUpgrade() {
        for (Upgrade upgrade : upgrades) {
            if (!upgrade.isCollected() && upgrade instanceof ExtraLifeUpgrade) {
                return true;
            }
        }
        return false;
    }
    public boolean hasUncollectedProtectionUpgrade() {
        for (Upgrade upgrade : upgrades) {
            if (!upgrade.isCollected() && upgrade instanceof ProtectionUpgrade) {
                return true;
            }
        }
        return false;
    }





    public void startNextLevel() {
        // Refill all FOOD blocks
        for (int r = 0; r < logicMap.length; r++) {
            for (int c = 0; c < logicMap[0].length; c++) {
                if (originalMapLayout[r].charAt(c) == ' ') {
                    logicMap[r][c] = BlockType.FOOD;
                }
            }
        }


        // Reset Pacman and Ghosts
        pacman.resetPosition();
        pacman.setDirection(pacman.getDirection());
        for (Ghost ghost : ghosts) {
            ghost.resetPosition();
            ghost.levelUpSpeed();
        }
    }



    public void checkGhostCollision(Ghost ghost, GamingWindow window) {
        Pacman pacman = getPacman();

        if (pacman.isProtected()) return;

        Rectangle ghostRect = new Rectangle(ghost.getX(), ghost.getY(), 45, 45);
        Rectangle pacmanRect = new Rectangle(pacman.getX(), pacman.getY(), 45, 45);

        if (ghostRect.intersects(pacmanRect)) {
            pacman.loseLife();
            pacman.resetPosition();
            resetAllGhosts();

            if (pacman.getLives() > 0) {
                window.showCountdownThenResume();
            }
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


    public void resetAllGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.resetPosition();
        }
    }

    public boolean hasUpgradeAt(int col, int row) {
        for (Upgrade u : upgrades) {
            if (!u.isCollected() && u.getX() == col && u.getY() == row) {
                return true;
            }
        }
        return false;
    }


    // === Getters ===
    public Pacman getPacman() {
        return pacman;
    }

    public BlockType[][] getLogicMap() {
        return logicMap;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
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

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }


    // === Private Initialization ===

    private MapType determineMapType(int rows, int cols) {
        return (rows == 17 && cols == 15) ? MapType.SMALL :
                (rows == 19 && cols == 17) ? MapType.MEDIUM :
                        MapType.LARGE;
    }

    private void initializePacmanAndPortals(String[] map) {
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[r].length(); c++) {
                char cell = map[r].charAt(c);
                switch (cell) {
                    case 'I' -> pacman = new Pacman(r, c);
                    case 'L' -> leftPortalPos = new Point(c, r);
                    case 'R' -> rightPortalPos = new Point(c, r);
                }
            }
        }
    }

    private void initializeGhosts(String[] map) {
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[r].length(); c++) {
                char cell = map[r].charAt(c);
                Ghost ghost = null;
                if (cell == 'r') {
                    ghost = new Ghost(r, c);
                    ghost.setAnimationFrames(Resources.redGhostFrames);
                } else if (cell == 'p') {
                    ghost = new Ghost(r, c);
                    ghost.setAnimationFrames(Resources.pinkGhostFrames);
                } else if (cell == 'o') {
                    ghost = new Ghost(r, c);
                    ghost.setAnimationFrames(Resources.orangeGhostFrames);
                } else if (cell == 'b') {
                    ghost = new Ghost(r, c);
                    ghost.setAnimationFrames(Resources.blueGhostFrames);
                }
                if (ghost != null) {
                    ghost.setPortalPositions(leftPortalPos, rightPortalPos);
                    ghosts.add(ghost);
                }
            }
        }
    }
}


