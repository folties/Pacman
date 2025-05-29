package game;

import model.map.BlockType;
import model.map.MapDesign;
import model.map.MapType;
import model.entities.Pacman;
import util.Resources;

public class Logic {
    private final BlockType[][] logicMap;
    private final Pacman pacman;

    public Logic(int rows, int cols) {
        MapType mapType = (rows == 17 && cols == 15) ? MapType.SMALL :
                (rows == 19 && cols == 17) ? MapType.MEDIUM : MapType.LARGE;

        String[] currentBlockMap = Resources.loadMapType(mapType);
        logicMap = MapDesign.loadLogicMap(currentBlockMap);

        // 🔍 Знайти символ 'P' у карті
        int startRow = 0;
        int startCol = 0;

        for (int r = 0; r < currentBlockMap.length; r++) {
            String line = currentBlockMap[r];
            for (int c = 0; c < line.length(); c++) {
                if (line.charAt(c) == 'I') {
                    startRow = r;
                    startCol = c;
                }
            }
        }

        pacman = new Pacman(startRow, startCol);
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
}
