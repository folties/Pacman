package util;

import model.Map;
import model.MapType;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Resources {
    public static Image menuBackgroundImage;
    public static Image optionBackgroundImage;
    public static Image wallImage;
    public static Image foodImage;
    public static Image rightPortalImage;
    public static Image leftPortalImage;
    public static Image pacmanImage;
    public static Image redGhostImage;
    public static Image pinkGhostImage;
    public static Image orangeGhostImage;
    public static Image blueGhostImage;

    public static void loadResources() {
        menuBackgroundImage = loadImage("materials/menuBackground.png");
        optionBackgroundImage = loadImage("materials/optionBackground.png");
        wallImage = loadImage("materials/stone.png");
        foodImage = loadImage("materials/food.png");
        rightPortalImage = loadImage("materials/portals/right/rightPortal1.png");
        leftPortalImage = loadImage("materials/portals/left/leftPortal1.jpg");
        pacmanImage = loadImage("materials/pacman/pacmanRight1.png");
        redGhostImage = loadImage("materials/ghosts/redGhost1.png");
        pinkGhostImage = loadImage("materials/ghosts/pinkGhost1.png");
        orangeGhostImage = loadImage("materials/ghosts/orangeGhost1.png");
        blueGhostImage = loadImage("materials/ghosts/greenGhost1.png");
    }

    public static String[] loadMapFromFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try (InputStream in = Map.class.getClassLoader().getResourceAsStream(filePath)) {
            if (in == null) {
                throw new RuntimeException("Map file not found: " + filePath);
            }
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
        URL url = Resources.class.getClassLoader().getResource(path);
        if (url == null) {
            throw new RuntimeException("Image resource not found: " + path);
        }
        return new ImageIcon(url).getImage();
    }
}
