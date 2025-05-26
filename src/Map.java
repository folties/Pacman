import java.util.HashSet;


public class Map {
    public static int mapWidth;
    public static int mapHeight;
    public static int blockSize;

    public static HashSet<Block> walls = new HashSet<>();
    public static HashSet<Block> ghosts = new HashSet<>();
    public static HashSet<Block> foods = new HashSet<>();
    public static Block pacman;
    public static Block leftPortal;
    public static Block rightPortal;


    public static void loadMapEntities(String[] map, int size) {
        blockSize = size;
        mapHeight = map.length;
        mapWidth = map[0].length();


        walls.clear();
        ghosts.clear();
        foods.clear();
        pacman = null;
        leftPortal = null;
        rightPortal = null;

        for (int r = 0; r < map.length; r++) {
            String row = map[r];
            for (int c = 0; c < row.length(); c++) {
                char blockMapChar = row.charAt(c);
                int x = c * blockSize;
                int y = r * blockSize;

                if (blockMapChar == '#') {
                    Block wall = new Block(x, y, blockSize, blockSize, Resources.wallImage);
                    walls.add(wall);
                } else if (blockMapChar == '\\') {
                    leftPortal = new Block(x, y, blockSize, blockSize, Resources.leftPortalImage);
                } else if (blockMapChar == '/') {
                    rightPortal = new Block(x, y, blockSize, blockSize, Resources.rightPortalImage);
                } else if (blockMapChar == 'b') {
                    Block ghost = new Block(x, y, blockSize, blockSize, Resources.blueGhostImage);
                    ghosts.add(ghost);
                } else if (blockMapChar == 'o') {
                    Block ghost = new Block(x, y, blockSize, blockSize, Resources.orangeGhostImage);
                    ghosts.add(ghost);
                } else if (blockMapChar == 'p') {
                    Block ghost = new Block(x, y, blockSize, blockSize, Resources.pinkGhostImage);
                    ghosts.add(ghost);
                } else if (blockMapChar == 'r') {
                    Block ghost = new Block(x, y, blockSize, blockSize, Resources.redGhostImage);
                    ghosts.add(ghost);
                } else if (blockMapChar == 'I') {
                    pacman = new Block(x, y, blockSize, blockSize, Resources.pacmanImage);
                } else if (blockMapChar == ' ') {
                    Block food = new Block(x + 21, y + 21, 4, 4, null);
                    foods.add(food);
                }
            }
        }
    }
}
