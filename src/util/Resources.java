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

    public static Map<Direction, Image[]> pacmanFrames;

    public static Image[] leftPortalFrames;
    public static Image[] rightPortalFrames;

    public static Map<Direction, Image[]> redGhostFrames;
    public static Map<Direction, Image[]> pinkGhostFrames;
    public static Map<Direction, Image[]> blueGhostFrames;
    public static Map<Direction, Image[]> orangeGhostFrames;


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

        redGhostFrames = new HashMap<>();

        redGhostFrames.put(Direction.RIGHT, new Image[]{
                loadImage("materials/ghosts/red/redGhostRight1.png"),
                loadImage("materials/ghosts/red/redGhostRight2.png")
        });

        redGhostFrames.put(Direction.LEFT, new Image[]{
                loadImage("materials/ghosts/red/redGhostLeft1.png"),
                loadImage("materials/ghosts/red/redGhostLeft2.png")
        });

        redGhostFrames.put(Direction.UP, new Image[]{
                loadImage("materials/ghosts/red/redGhostUp1.png"),
                loadImage("materials/ghosts/red/redGhostUp2.png")
        });

        redGhostFrames.put(Direction.DOWN, new Image[]{
                loadImage("materials/ghosts/red/redGhostDown1.png"),
                loadImage("materials/ghosts/red/redGhostDown2.png")
        });

        pinkGhostFrames = new HashMap<>();

        pinkGhostFrames.put(Direction.RIGHT, new Image[]{
                loadImage("materials/ghosts/pink/pinkGhostRight1.png"),
                loadImage("materials/ghosts/pink/pinkGhostRight2.png")
        });

        pinkGhostFrames.put(Direction.LEFT, new Image[]{
                loadImage("materials/ghosts/pink/pinkGhostLeft1.png"),
                loadImage("materials/ghosts/pink/pinkGhostLeft2.png")
        });

        pinkGhostFrames.put(Direction.UP, new Image[]{
                loadImage("materials/ghosts/pink/pinkGhostUp1.png"),
                loadImage("materials/ghosts/pink/pinkGhostUp2.png")
        });

        pinkGhostFrames.put(Direction.DOWN, new Image[]{
                loadImage("materials/ghosts/pink/pinkGhostDown1.png"),
                loadImage("materials/ghosts/pink/pinkGhostDown2.png")
        });

        blueGhostFrames = new HashMap<>();

        blueGhostFrames.put(Direction.RIGHT, new Image[]{
                loadImage("materials/ghosts/blue/blueGhostRight1.png"),
                loadImage("materials/ghosts/blue/blueGhostRight2.png")
        });

        blueGhostFrames.put(Direction.LEFT, new Image[]{
                loadImage("materials/ghosts/blue/blueGhostLeft1.png"),
                loadImage("materials/ghosts/blue/blueGhostLeft2.png")
        });

        blueGhostFrames.put(Direction.UP, new Image[]{
                loadImage("materials/ghosts/blue/blueGhostUp1.png"),
                loadImage("materials/ghosts/blue/blueGhostUp2.png")
        });

        blueGhostFrames.put(Direction.DOWN, new Image[]{
                loadImage("materials/ghosts/blue/blueGhostDown1.png"),
                loadImage("materials/ghosts/blue/blueGhostDown2.png")
        });

        orangeGhostFrames = new HashMap<>();

        orangeGhostFrames.put(Direction.RIGHT, new Image[]{
                loadImage("materials/ghosts/orange/orangeGhostRight1.png"),
                loadImage("materials/ghosts/orange/orangeGhostRight2.png")
        });

        orangeGhostFrames.put(Direction.LEFT, new Image[]{
                loadImage("materials/ghosts/orange/orangeGhostLeft1.png"),
                loadImage("materials/ghosts/orange/orangeGhostLeft2.png")
        });

        orangeGhostFrames.put(Direction.UP, new Image[]{
                loadImage("materials/ghosts/orange/orangeGhostUp1.png"),
                loadImage("materials/ghosts/orange/orangeGhostUp2.png")
        });

        orangeGhostFrames.put(Direction.DOWN, new Image[]{
                loadImage("materials/ghosts/orange/orangeGhostDown1.png"),
                loadImage("materials/ghosts/orange/orangeGhostDown2.png")
        });

        leftPortalFrames = new Image[] {
                loadImage("materials/portals/left/leftPortal1.png"),
                loadImage("materials/portals/left/leftPortal2.png"),
                loadImage("materials/portals/left/leftPortal3.png"),
                loadImage("materials/portals/left/leftPortal4.png"),
                loadImage("materials/portals/left/leftPortal5.png"),
                loadImage("materials/portals/left/leftPortal6.png"),
                loadImage("materials/portals/left/leftPortal7.png"),
                loadImage("materials/portals/left/leftPortal8.png"),
                loadImage("materials/portals/left/leftPortal9.png")
        };

        rightPortalFrames = new Image[] {
                loadImage("materials/portals/right/rightPortal1.png"),
                loadImage("materials/portals/right/rightPortal2.png"),
                loadImage("materials/portals/right/rightPortal3.png"),
                loadImage("materials/portals/right/rightPortal4.png"),
                loadImage("materials/portals/right/rightPortal5.png"),
                loadImage("materials/portals/right/rightPortal6.png"),
                loadImage("materials/portals/right/rightPortal7.png"),
                loadImage("materials/portals/right/rightPortal8.png"),
                loadImage("materials/portals/right/rightPortal9.png")
        };



        menuBackgroundImage = loadImage("materials/menuBackground.png");
        optionBackgroundImage = loadImage("materials/optionBackground.png");
        wallImage = loadImage("materials/stone.png");
        foodImage = loadImage("materials/food.png");
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
            default -> null;
        };
    }
}
