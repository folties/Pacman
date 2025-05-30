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

        ghosts = new ArrayList<>(); // ✅ ВАЖЛИВО!

        for (int r = 0; r < currentBlockMap.length; r++) {
            for (int c = 0; c < currentBlockMap[r].length(); c++) {
                char cell = currentBlockMap[r].charAt(c);
                if (cell == 'I') {
                    pacman = new Pacman(r, c);
                } else if (cell == 'r') {
                    ghosts.add(new Ghost(r, c, Resources.redGhostImage));
                } else if (cell == 'p') {
                    ghosts.add(new Ghost(r, c, Resources.pinkGhostImage));
                } else if (cell == 'o') {
                    ghosts.add(new Ghost(r, c, Resources.orangeGhostImage));
                } else if (cell == 'b') {
                    ghosts.add(new Ghost(r, c, Resources.blueGhostImage));
                } else if (cell == 'L') {
                    leftPortalPos = new Point(c, r);
                } else if (cell == 'R') {
                    rightPortalPos = new Point(c, r);
                }
            }
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

    public Point getLeftPortalPos() { return leftPortalPos; }
    public Point getRightPortalPos() { return rightPortalPos; }

}
