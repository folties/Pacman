package gui;

import animation.GhostAnimation;
import animation.PacmanAnimation;
import animation.PortalAnimation;
import controller.PacmanKeyController;
import game.GameLoop;
import game.GhostLoop;
import game.Logic;
import game.TimerLoop;
import main.MainWindow;
import model.entities.Ghost;
import model.map.BlockType;
import model.map.MapType;
import util.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class GamingWindow extends JPanel {
    private final MainWindow mainWindow;
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
    private List<JLabel> heartLabels = new ArrayList<>();

    private JLabel timeLabel;
    private TimerLoop timerLoop;

    private JPanel topPanel;

    private PausePanel pausePanel;
    private boolean paused = false;
    private boolean gameEnded = false;



    public GamingWindow(MainWindow mainWindow, MapType mapType) {
        this.mainWindow = mainWindow;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        int rows, cols;
        switch (mapType) {
            case SMALL -> {
                rows = 17;
                cols = 15;
            }
            case MEDIUM -> {
                rows = 19;
                cols = 17;
            }
            case LARGE -> {
                rows = 21;
                cols = 19;
            }
            default -> throw new IllegalArgumentException("Unknown map type: " + mapType);
        }

        gameLogic = new Logic(rows, cols);
        initController();

        JPanel gridPanel = buildGridPanel(rows, cols);
        JLayeredPane layeredPane = buildLayeredPane(gridPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(layeredPane, gbc);

        // 1. Панель для score + сердечка
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);

        // TIME LABEL
        timeLabel = new JLabel("Time: 00:00");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Rockwell", Font.BOLD, 30));
        timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 400, 0, 0));
        topPanel.add(timeLabel, BorderLayout.CENTER);

// Score зліва
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Rockwell", Font.BOLD, 30));
        topPanel.add(scoreLabel, BorderLayout.WEST);

// Серця справа
        JPanel heartsPanel = new JPanel();
        heartsPanel.setBackground(Color.BLACK);
        heartsPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));
        for (int i = 0; i < gameLogic.getPacman().getLives(); i++) {
            JLabel heart = new JLabel(Resources.heartIcon);
            heartLabels.add(heart);
            heartsPanel.add(heart);
        }
        topPanel.add(heartsPanel, BorderLayout.EAST);

// 2. Додай topPanel (верхній рядок)
        GridBagConstraints topGbc = new GridBagConstraints();
        topGbc.gridx = 0;
        topGbc.gridy = 0;
        topGbc.insets = new Insets(10, 10, 10, 10);
        add(topPanel, topGbc);

// 3. Змісти ігрове поле вниз (gridy = 1)
        GridBagConstraints gameGbc = new GridBagConstraints();
        gameGbc.gridx = 0;
        gameGbc.gridy = 1;
        add(layeredPane, gameGbc);

        gameLoop = new GameLoop(gameLogic, this, () -> {
            updateGrid();
            SwingUtilities.invokeLater(() -> {
                pacmanLabel.setLocation(gameLogic.getPacman().getX(), gameLogic.getPacman().getY());
                updateHeartsUI();
            });
            scoreLabel.setText("Score: " + gameLogic.getScore());
        });

        gameLoop.start();

        timerLoop = new TimerLoop(() -> SwingUtilities.invokeLater(this::updateTimer));
        timerLoop.start();

    }

    private void initController() {
        PacmanKeyController controller = new PacmanKeyController(gameLogic.getPacman());
        addKeyListener(controller);
        setFocusable(true);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }
        });
    }

    private void updateHeartsUI() {
        int lives = gameLogic.getPacman().getLives();
        for (int i = 0; i < heartLabels.size(); i++) {
            heartLabels.get(i).setVisible(i < lives);
        }
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

        pausePanel = new PausePanel(this::resumeGame, () -> {
            stopAllLoops();
            mainWindow.showGameMenu();
        });
        pausePanel.setBounds(0, 0, width, height);
        pausePanel.setVisible(false);
        layeredPane.add(pausePanel, Integer.valueOf(5)); // top layer

        return layeredPane;
    }

    public void stopAllLoops() {
        if (gameLoop != null) gameLoop.stopLoop();
        if (timerLoop != null) timerLoop.stopLoop();
        for (GhostLoop loop : ghostLoops) {
            loop.stopLoop();
        }
    }


    public void endGame() {
        if (gameEnded) return;
        gameEnded = true;

        stopAllLoops();
        SwingUtilities.invokeLater(() -> {
            mainWindow.showGameEnd(gameLogic.getScore(), timerLoop.getSeconds());
        });
    }


    private void updateTimer() {
        int seconds = timerLoop.getSeconds();
        int minutes = seconds / 60;
        int secs = seconds % 60;
        String timeText = String.format("Time: %02d:%02d", minutes, secs);
        timeLabel.setText(timeText);
    }

    private void togglePause() {
        paused = !paused;
        if (paused) {
            // 🛑 FIRST pause threads
            System.out.println("Pausing all loops...");
            gameLoop.pause();
            timerLoop.pause();
            for (GhostLoop g : ghostLoops) {
                g.pause();
                System.out.println("Paused ghost: " + g);
            }
        } else {
            System.out.println("Resuming all loops...");
            gameLoop.resumeLoop();
            timerLoop.resumeLoop();
            for (GhostLoop g : ghostLoops) {
                System.out.println("Resuming ghost...");
                g.resumeLoop();
            }
        }

        // ✅ THEN update the UI
        pausePanel.setVisible(paused);
        topPanel.setVisible(!paused);
    }



    private void resumeGame() {
        paused = false;
        pausePanel.setVisible(false);
        topPanel.setVisible(true); // ✅ show the top panel again

        gameLoop.resumeLoop();
        timerLoop.resumeLoop();
        for (GhostLoop g : ghostLoops) g.resumeLoop();
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