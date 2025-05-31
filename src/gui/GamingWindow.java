package gui;

import animation.GhostAnimation;
import animation.PacmanAnimation;
import animation.PortalAnimation;
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

    private JLabel leftPortalLabel;
    private JLabel rightPortalLabel;
    private JLabel scoreLabel;
    private JLabel livesLabel;



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

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Rockwell", Font.BOLD, 30));

        GridBagConstraints scoreGbc = new GridBagConstraints();
        scoreGbc.gridx = 0;
        scoreGbc.gridy = 1;
        scoreGbc.insets = new Insets(10, 0, 0, 0);
        add(scoreLabel, scoreGbc);

        livesLabel = new JLabel("Lives: " + gameLogic.getPacman().getLives());
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(new Font("Rockwell", Font.BOLD, 30));

        GridBagConstraints livesConstraints = new GridBagConstraints();
        livesConstraints.gridx = 0;
        livesConstraints.gridy = 1;
        livesConstraints.insets = new Insets(10, 400, 0, 0); // spacing
        add(livesLabel, livesConstraints);


        gameLoop = new GameLoop(gameLogic, () -> {
            updateGrid();
            SwingUtilities.invokeLater(() ->
                    pacmanLabel.setLocation(gameLogic.getPacman().getX(), gameLogic.getPacman().getY()));
            scoreLabel.setText("Score: " + gameLogic.getScore());
            livesLabel.setText("Lives: " + gameLogic.getPacman().getLives());
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
            JLabel ghostLabel = new JLabel(); // або обирай залежно від типу
            ghostLabel.setBounds(ghost.getX(), ghost.getY(), blockSize, blockSize);
            layeredPane.add(ghostLabel, Integer.valueOf(1));

            GhostLoop loop = new GhostLoop(ghost, gameLogic.getLogicMap(), ghostLabel, gameLogic.getPacman(), gameLogic);
            loop.start();
            ghostLoops.add(loop);

            GhostAnimation ghostAnim = new GhostAnimation(ghostLabel, ghost);
            ghostAnim.start();

        }

        pacmanAnimation = new PacmanAnimation(
                pacmanLabel,
                gameLogic.getPacman(),
                Resources.pacmanFrames
        );
        pacmanAnimation.start();

        Point leftPortal = gameLogic.getLeftPortalPos();
        leftPortalLabel = new JLabel();
        leftPortalLabel.setBounds(leftPortal.x * blockSize, leftPortal.y * blockSize, blockSize, blockSize);
        layeredPane.add(leftPortalLabel, Integer.valueOf(1));

        Point rightPortal = gameLogic.getRightPortalPos();
        rightPortalLabel = new JLabel();
        rightPortalLabel.setBounds(rightPortal.x * blockSize, rightPortal.y * blockSize, blockSize, blockSize);
        layeredPane.add(rightPortalLabel, Integer.valueOf(1));


        PortalAnimation leftAnim = new PortalAnimation(leftPortalLabel, Resources.leftPortalFrames);
        PortalAnimation rightAnim = new PortalAnimation(rightPortalLabel, Resources.rightPortalFrames);

        leftAnim.start();
        rightAnim.start();

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