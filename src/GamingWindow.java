import javax.swing.*;
import java.awt.*;

public class GamingWindow extends JPanel {
    private static final int blockSize = 45;

    public GamingWindow(int rows, int cols) {

        setPreferredSize(new Dimension(19 * blockSize, 21 * blockSize));
        setBackground(Color.BLACK);

        // Load assets and map
        Resources.loadResources();
        MapType mapType;

        if (rows == 17 && cols == 15) {
            mapType = MapType.SMALL;
        } else if (rows == 19 && cols == 17) {
            mapType = MapType.MEDIUM;
        } else {
            mapType = MapType.LARGE;
        }

        String[] currentBlockMap = Resources.loadMapType(mapType);
        Map.loadMapEntities(currentBlockMap, blockSize);

        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int offsetX = (getWidth() - Map.mapWidth * Map.blockSize) / 2;
        int offsetY = (getHeight() - Map.mapHeight * Map.blockSize) / 2;

        if (Map.pacman != null) {
            g.drawImage(Map.pacman.image, offsetX + Map.pacman.x, offsetY + Map.pacman.y,
                    Map.pacman.width, Map.pacman.height, null);
        }

        for (Block wall : Map.walls) {
            g.drawImage(wall.image, offsetX + wall.x, offsetY + wall.y, wall.width, wall.height, null);
        }

        for (Block ghost : Map.ghosts) {
            g.drawImage(ghost.image, offsetX + ghost.x, offsetY + ghost.y, ghost.width, ghost.height, null);
        }

        g.setColor(Color.WHITE);
        for (Block food : Map.foods) {
            g.fillRect(offsetX + food.x, offsetY + food.y, food.width, food.height);
        }
    }

}


