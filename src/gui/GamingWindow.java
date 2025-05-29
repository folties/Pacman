package gui;

import animation.PacmanAnimation;
import controller.PacmanKeyController;
import game.GameLoop;
import game.GhostLoop;
import game.Logic;
import model.entities.Ghost;
import model.map.BlockType;
import util.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GamingWindow extends JPanel {
    private static final int blockSize = 45;
    private final Logic gameLogic;
    private JLabel[][] cells;
    private JLabel pacmanLabel;
    private PacmanAnimation pacmanAnimation;
    private GameLoop gameLoop;
    private List<GhostLoop> ghostLoops = new ArrayList<>();


    public GamingWindow(int rows, int cols) {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        gameLogic = new Logic(rows, cols);
        initController();

        JPanel gridPanel = buildGridPanel(rows, cols);
        JLayeredPane layeredPane = buildLayeredPane(gridPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(layeredPane, gbc);

        gameLoop = new GameLoop(gameLogic, () -> {
            updateGrid();
            SwingUtilities.invokeLater(() ->
                    pacmanLabel.setLocation(gameLogic.getPacman().getX(), gameLogic.getPacman().getY()));
        });

        gameLoop.start();

    }

    private void initController() {
        PacmanKeyController controller = new PacmanKeyController(gameLogic.getPacman());
        addKeyListener(controller);
        setFocusable(true);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private JPanel buildGridPanel(int rows, int cols) {
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        gridPanel.setPreferredSize(new Dimension(cols * blockSize, rows * blockSize));

        cells = new JLabel[rows][cols];
        BlockType[][] logicMap = gameLogic.getLogicMap();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(blockSize, blockSize));
                cells[r][c] = label;

                Image image = Resources.getImageForBlockType(logicMap[r][c]);
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
        return gridPanel;
    }

    private JLayeredPane buildLayeredPane(JPanel gridPanel) {
        int width = gridPanel.getPreferredSize().width;
        int height = gridPanel.getPreferredSize().height;

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));
        layeredPane.setLayout(null);

        gridPanel.setBounds(0, 0, width, height);
        layeredPane.add(gridPanel, Integer.valueOf(0));

        pacmanLabel = new JLabel();
        pacmanLabel.setBounds(gameLogic.getPacman().getX(), gameLogic.getPacman().getY(), blockSize, blockSize);
        layeredPane.add(pacmanLabel, Integer.valueOf(1));

        for (Ghost ghost : gameLogic.getGhosts()) {
            JLabel ghostLabel = new JLabel(new ImageIcon(ghost.getImage())); // або обирай залежно від типу
            ghostLabel.setBounds(ghost.getX(), ghost.getY(), blockSize, blockSize);
            layeredPane.add(ghostLabel, Integer.valueOf(1));

            GhostLoop loop = new GhostLoop(ghost, gameLogic.getLogicMap(), ghostLabel);
            loop.start();
            ghostLoops.add(loop);

        }

        pacmanAnimation = new PacmanAnimation(
                pacmanLabel,
                gameLogic.getPacman(),
                Resources.pacmanFrames
        );
        pacmanAnimation.start();

        return layeredPane;
    }

    private void updateGrid() {
        BlockType[][] logicMap = gameLogic.getLogicMap();
        for (int r = 0; r < logicMap.length; r++) {
            for (int c = 0; c < logicMap[0].length; c++) {
                Image image = Resources.getImageForBlockType(logicMap[r][c]);
                cells[r][c].setIcon(image != null ? new ImageIcon(image) : null);
            }
        }
    }
}