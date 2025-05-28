import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Resources {
    public static Image wallImage;

    public static Image rightPortalImage;
    public static Image leftPortalImage;


    public static Image pacmanImage;

    public static Image redGhostImage;
    public static Image pinkGhostImage;
    public static Image orangeGhostImage;
    public static Image blueGhostImage;

    public static void loadResources() {
        wallImage = loadImage("./materials/stone.png");

        rightPortalImage = loadImage("materials/portals/right/rightPortal1.png");
        leftPortalImage = loadImage("materials/portals/left/leftPortal1.jpg");

        pacmanImage = loadImage("./materials/pacman/pacmanRight1.png");

        redGhostImage = loadImage("./materials/ghosts/redGhost1.png");
        pinkGhostImage = loadImage("./materials/ghosts/pinkGhost1.png");
        orangeGhostImage = loadImage("./materials/ghosts/orangeGhost1.png");
        blueGhostImage = loadImage("./materials/ghosts/greenGhost1.png");
    }

    public static String[] loadMapFromFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try {
            InputStream in = Map.class.getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines.toArray(new String[0]);
    }

    public static String[] loadMapType(MapType mapType) {
        return loadMapFromFile(mapType.getPath());
    }

    private static Image loadImage(String path) {
        return new ImageIcon(Resources.class.getResource(path)).getImage();
    }
}
