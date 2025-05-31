package game;

import model.entities.Ghost;
import model.map.BlockType;
import model.map.MapDesign;
import model.map.MapType;
import model.entities.Pacman;
import util.Direction;
import util.Resources;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Logic {
    private final BlockType[][] logicMap;
    private Pacman pacman;
    private List<Ghost> ghosts;

    private Point leftPortalPos;
    private Point rightPortalPos;

    public Logic(int rows, int cols) {
        MapType mapType = (rows == 17 && cols == 15) ? MapType.SMALL :
                (rows == 19 && cols == 17) ? MapType.MEDIUM : MapType.LARGE;

        String[] currentBlockMap = Resources.loadMapType(mapType);
        logicMap = MapDesign.loadLogicMap(currentBlockMap);
        ghosts = new ArrayList<>();

        // 🔁 Перше проходження — знайти портали і Pacman
        for (int r = 0; r < currentBlockMap.length; r++) {
            for (int c = 0; c < currentBlockMap[r].length(); c++) {
                char cell = currentBlockMap[r].charAt(c);
                if (cell == 'I') {
                    pacman = new Pacman(r, c);
                } else if (cell == 'L') {
                    leftPortalPos = new Point(c, r);
                } else if (cell == 'R') {
                    rightPortalPos = new Point(c, r);
                }
            }
        }

        // 🔁 Друге проходження — створити привидів після того, як координати порталів відомі
        for (int r = 0; r < currentBlockMap.length; r++) {
            for (int c = 0; c < currentBlockMap[r].length(); c++) {
                char cell = currentBlockMap[r].charAt(c);
                Ghost ghost = null;

                if (cell == 'r') {
                    ghost = new Ghost(r, c);
                    ghost.setAnimationFrames(Resources.redGhostFrames); // the map
                } else if (cell == 'p') {
                    ghost = new Ghost(r, c);
                    ghost.setAnimationFrames(Resources.pinkGhostFrames); // the map
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

        // ✅ тільки тепер Pacman отримує портали
        if (pacman != null) {
            pacman.setPortalPositions(leftPortalPos, rightPortalPos);
        }
    }

    public void update() {
        pacman.stepMove(logicMap);
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

    public Point getLeftPortalPos() {
        return leftPortalPos;
    }

    public Point getRightPortalPos() {
        return rightPortalPos;
    }
    public int getScore() {
        return pacman.getScore();
    }
    public void resetAllGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.resetPosition();
        }
    }


}

