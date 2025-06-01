package model.map;

public class MapDesign {

    public static BlockType[][] loadLogicMap(String[] mapLines) {
        int rows = mapLines.length;
        int cols = mapLines[0].length();
        BlockType[][] logicMap = new BlockType[rows][cols];

        for (int r = 0; r < rows; r++) {
            String line = mapLines[r];
            for (int c = 0; c < cols; c++) {
                char ch = line.charAt(c);
                logicMap[r][c] = switch (ch) {
                    case '#' -> BlockType.WALL;
                    case ' ' -> BlockType.FOOD;
                    case 'I' -> BlockType.PLAYER;
                    case 'r' -> BlockType.GHOST_RED;
                    case 'p' -> BlockType.GHOST_PINK;
                    case 'o' -> BlockType.GHOST_ORANGE;
                    case 'b' -> BlockType.GHOST_BLUE;
                    case 'L' -> BlockType.LEFT_PORTAL;
                    case 'R' -> BlockType.RIGHT_PORTAL;
                    default -> BlockType.EMPTY;
                };
            }
        }
        return logicMap;
    }
}
