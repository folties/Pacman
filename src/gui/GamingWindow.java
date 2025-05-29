package gui;

import controller.PacmanKeyController;
import model.BlockType;
import model.Map;
import model.MapType;
import model.Pacman;
import util.Resources;

import javax.swing.*;
import java.awt.*;

public class GamingWindow extends JPanel {
    private static final int blockSize = 45;
    private Pacman pacman;
    private BlockType[][] logicMap;
    private JLabel[][] cells;
    private JLabel pacmanLabel;


    public GamingWindow(int rows, int cols) {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout()); // Center the grid panel

        MapType mapType;

        if (rows == 17 && cols == 15) {
            mapType = MapType.SMALL;
        } else if (rows == 19 && cols == 17) {
            mapType = MapType.MEDIUM;
        } else {
            mapType = MapType.LARGE;
        }

        String[] currentBlockMap = Resources.loadMapType(mapType);
        logicMap = Map.loadLogicMap(currentBlockMap);

        // 2. Create Pacman
        pacman = new Pacman(15, 9); // change to your start coordinates

        // 3. Setup controller
        PacmanKeyController controller = new PacmanKeyController(pacman);
        addKeyListener(controller);
        setFocusable(true);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        cells = new JLabel[rows][cols];
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        gridPanel.setPreferredSize(new Dimension(cols * blockSize, rows * blockSize));

        buildGridFromLogicMap(logicMap, gridPanel);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(cols * blockSize, rows * blockSize));
        layeredPane.setLayout(null); // allows absolute positioning inside

        gridPanel.setBounds(0, 0, cols * blockSize, rows * blockSize);
        layeredPane.add(gridPanel, Integer.valueOf(0)); // background layer

        pacmanLabel = new JLabel(new ImageIcon(Resources.pacmanImage));
        pacmanLabel.setBounds(pacman.getX(), pacman.getY(), blockSize, blockSize);
        layeredPane.add(pacmanLabel, Integer.valueOf(1)); // foreground layer

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(layeredPane, gbc);

        startGameLoop();
    }

    private void startGameLoop() {
        new Thread(() -> {
            while (pacman.getLives() > 0) {
                pacman.stepMove(logicMap);
                updateGrid();
                SwingUtilities.invokeLater(() -> {
                    pacmanLabel.setLocation(pacman.getX(), pacman.getY());
                });
                try {
                    Thread.sleep(5); // smooth frame rate
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }



    private void updateGrid() {
        for (int r = 0; r < logicMap.length; r++) {
            for (int c = 0; c < logicMap[0].length; c++) {
                Image image = switch (logicMap[r][c]) {
                    case WALL -> Resources.wallImage;
                    case FOOD -> Resources.foodImage;
                    default -> null;
                };
                cells[r][c].setIcon(image != null ? new ImageIcon(image) : null);
            }
        }
    }

    private void buildGridFromLogicMap(BlockType[][] logicMap, JPanel gridPanel) {
        int rows = logicMap.length;
        int cols = logicMap[0].length;
        cells = new JLabel[rows][cols]; // ensure this is initialized here too

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(blockSize, blockSize));

                cells[r][c] = label;

                BlockType type = logicMap[r][c];
                Image image = switch (type) {
                    case WALL -> Resources.wallImage;
                    case LEFT_PORTAL -> Resources.leftPortalImage;
                    case RIGHT_PORTAL -> Resources.rightPortalImage;
                    case GHOST_BLUE -> Resources.blueGhostImage;
                    case GHOST_ORANGE -> Resources.orangeGhostImage;
                    case GHOST_PINK -> Resources.pinkGhostImage;
                    case GHOST_RED -> Resources.redGhostImage;
                    case FOOD -> Resources.foodImage;
                    default -> null;
                };

                if (image != null) {
                    label.setIcon(new ImageIcon(image));
                }

                JPanel cell = new JPanel(new BorderLayout());
                cell.setPreferredSize(new Dimension(blockSize, blockSize));
                cell.setBackground(Color.BLACK);
                cell.add(label, BorderLayout.CENTER);

                gridPanel.add(cell);
            }
        }
    }

}
