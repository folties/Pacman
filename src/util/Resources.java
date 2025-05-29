package util;

import model.map.BlockType;
import model.map.MapDesign;
import model.map.MapType;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resources {
    public static Image menuBackgroundImage;
    public static Image optionBackgroundImage;
    public static Image wallImage;
    public static Image foodImage;
    public static Image rightPortalImage;
    public static Image leftPortalImage;
    public static Image redGhostImage;
    public static Image pinkGhostImage;
    public static Image orangeGhostImage;
    public static Image blueGhostImage;

    public static Map<Direction, Image[]> pacmanFrames;

    public static Image[] pacmanImages;

    public static void loadResources() {

        pacmanFrames = new HashMap<>();

        pacmanFrames.put(Direction.RIGHT, new Image[]{
                loadImage("materials/pacman/pacmanRight1.png"),
                loadImage("materials/pacman/pacmanRight2.png"),
                loadImage("materials/pacman/pacmanRight3.png")
        });

        pacmanFrames.put(Direction.LEFT, new Image[]{
                loadImage("materials/pacman/pacmanLeft1.png"),
                loadImage("materials/pacman/pacmanLeft2.png"),
                loadImage("materials/pacman/pacmanLeft3.png")
        });

        pacmanFrames.put(Direction.UP, new Image[]{
                loadImage("materials/pacman/pacmanUp1.png"),
                loadImage("materials/pacman/pacmanUp2.png"),
                loadImage("materials/pacman/pacmanUp3.png")
        });

        pacmanFrames.put(Direction.DOWN, new Image[]{
                loadImage("materials/pacman/pacmanDown1.png"),
                loadImage("materials/pacman/pacmanDown2.png"),
                loadImage("materials/pacman/pacmanDown3.png")
        });


        menuBackgroundImage = loadImage("materials/menuBackground.png");
        optionBackgroundImage = loadImage("materials/optionBackground.png");
        wallImage = loadImage("materials/stone.png");
        foodImage = loadImage("materials/food.png");
        rightPortalImage = loadImage("materials/portals/right/rightPortal1.png");
        leftPortalImage = loadImage("materials/portals/left/leftPortal1.jpg");
        redGhostImage = loadImage("materials/ghosts/redGhost1.png");
        pinkGhostImage = loadImage("materials/ghosts/pinkGhost1.png");
        orangeGhostImage = loadImage("materials/ghosts/orangeGhost1.png");
        blueGhostImage = loadImage("materials/ghosts/greenGhost1.png");
    }

    public static String[] loadMapFromFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try (InputStream in = MapDesign.class.getClassLoader().getResourceAsStream(filePath)) {
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

    public static Image getImageForBlockType(BlockType type) {
        return switch (type) {
            case WALL -> wallImage;
            case FOOD -> foodImage;
            case LEFT_PORTAL -> leftPortalImage;
            case RIGHT_PORTAL -> rightPortalImage;
            default -> null;
        };
    }
}
