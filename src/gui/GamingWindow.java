package gui;

import animation.GhostAnimation;
import animation.PacmanAnimation;
import animation.PortalAnimation;
import controller.PacmanKeyController;
import game.*;
import main.MainWindow;
import model.entities.Ghost;
import model.map.BlockType;
import model.map.MapType;
import util.Resources;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GamingWindow extends JPanel {
    private static final int blockSize = Resources.BLOCK_SIZE;

    private final MainWindow mainWindow;
    private final Logic gameLogic;

    private JLabel[][] cells;
    private JLabel pacmanLabel;
    private JLabel leftPortalLabel, rightPortalLabel;
    private final List<JLabel> heartLabels = new ArrayList<>();
    private final List<GhostLoop> ghostLoops = new ArrayList<>();

    private PacmanAnimation pacmanAnimation;
    private GhostAnimation ghostAnimation;

    private PacmanLoop gameLoop;
    private TimerLoop timerLoop;
    private GameController gameController;

    private JPanel topPanel;
    private JLabel scoreLabel, timeLabel;
    private PausePanel pausePanel;
    private JLabel countdownLabel;

    private boolean paused = false;
    private boolean gameEnded = false;

    public GamingWindow(MainWindow mainWindow, MapType mapType) {
        this.mainWindow = mainWindow;
        setupUI();

        int[] size = getBoardSize(mapType);
        int rows = size[0];
        int cols = size[1];

        gameLogic = new Logic(rows, cols);
        initController();
        initGridAndLayers(rows, cols);
        initTopPanel();
        startLoops();
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
    }

    private int[] getBoardSize(MapType mapType) {
        return switch (mapType) {
            case SMALL -> new int[]{17, 15};
            case MEDIUM -> new int[]{19, 17};
            case LARGE -> new int[]{21, 19};
        };
    }

    private void createTopPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Rockwell", Font.BOLD, 30));
        topPanel.add(scoreLabel);

        topPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        timeLabel = new JLabel("Time: 00:00");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Rockwell", Font.BOLD, 30));
        topPanel.add(timeLabel);

        topPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        JPanel heartsPanel = new JPanel();
        heartsPanel.setLayout(new BoxLayout(heartsPanel, BoxLayout.X_AXIS));
        heartsPanel.setBackground(Color.BLACK);
        for (int i = 0; i < gameLogic.getPacman().getLives(); i++) {
            JLabel heart = new JLabel(Resources.heartIcon);
            heart.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            heartLabels.add(heart);
            heartsPanel.add(heart);
        }

        topPanel.add(heartsPanel);
    }

    private void initGridAndLayers(int rows, int cols) {
        JPanel gridPanel = buildGridPanel(rows, cols);
        JLayeredPane layeredPane = buildLayeredPane(gridPanel);

        GridBagConstraints gameConstr = new GridBagConstraints();
        gameConstr.gridx = 0;
        gameConstr.gridy = 1;
        add(layeredPane, gameConstr);
    }

    private void initTopPanel() {
        createTopPanel();

        GridBagConstraints panelConstr = new GridBagConstraints();
        panelConstr.gridx = 0;
        panelConstr.gridy = 0;
        panelConstr.insets = new Insets(10, 10, 10, 10);
        add(topPanel, panelConstr);
    }

    private void startLoops() {
        runCountdown(() -> {
            gameLoop = new PacmanLoop(gameLogic, this, () -> {
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

            for (GhostLoop loop : ghostLoops) {
                loop.start();
            }

            gameController = new GameController(gameLoop, timerLoop, ghostLoops);
        });
    }

    private void runCountdown(Runnable afterCountdown) {
        new Thread(() -> {
            try {
                for (int i = 3; i >= 1; i--) {
                    int count = i;
                    SwingUtilities.invokeLater(() -> countdownLabel.setText(String.valueOf(count)));
                    Thread.sleep(1000);
                }
                SwingUtilities.invokeLater(() -> {
                    countdownLabel.setVisible(false);
                    afterCountdown.run();
                });
            } catch (InterruptedException e) {
                System.out.println("countdown thread error: " + e.getMessage());
            }
        }).start();
    }

    private JPanel buildGridPanel(int rows, int cols) {
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        gridPanel.setPreferredSize(new Dimension(cols * blockSize, rows * blockSize));

        cells = new JLabel[rows][cols];
        BlockType[][] logicMap = gameLogic.getLogicMap();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JLabel label = new JLabel();
                cells[r][c] = label;

                JPanel cell = GridRender.createCell(label, blockSize);
                gridPanel.add(cell);
            }
        }

        GridRender.renderBlockGrid(cells, logicMap);
        return gridPanel;
    }

    public void showCountdown() {
        paused = true;
        gameController.pauseGame();

        resetEntityPositions();

        runCountdown(() -> {
            paused = false;
            gameController.resumeGame();
        });
    }

    public void resetEntityPositions() {
        SwingUtilities.invokeLater(() -> {
            pacmanLabel.setLocation(gameLogic.getPacman().getX(), gameLogic.getPacman().getY());
            for (int i = 0; i < ghostLoops.size(); i++) {
                Ghost ghost = gameLogic.getGhosts().get(i);
                JLabel ghostLabel = ghostLoops.get(i).getLabel();
                ghostLabel.setLocation(ghost.getX(), ghost.getY());
            }
        });
    }

    private void initController() {
        PacmanKeyController controller = new PacmanKeyController(gameLogic.getPacman());
        addKeyListener(controller);
        setFocusable(true);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) togglePause();
            }
        });
    }

    private void togglePause() {
        paused = !paused;

        if (paused) {
            gameController.pauseGame();
        } else {
            gameController.resumeGame();
        }

        pausePanel.setVisible(paused);
        topPanel.setVisible(!paused);
    }

    private void resumeGame() {
        paused = false;
        pausePanel.setVisible(false);
        topPanel.setVisible(true);
        gameController.resumeGame();
    }

    public void stopAllLoops() {
        gameController.stopAll();
    }

    public void endGame() {
        if (gameEnded) return;
        gameEnded = true;
        stopAllLoops();
        SwingUtilities.invokeLater(() ->
                mainWindow.showGameEnd(gameLogic.getScore(), timerLoop.getSeconds()));
    }

    public void updateHeartsUI() {
        int lives = gameLogic.getPacman().getLives();
        for (int i = 0; i < heartLabels.size(); i++) {
            heartLabels.get(i).setVisible(i < lives);
        }
    }

    private void updateTimer() {
        int seconds = timerLoop.getSeconds();
        timeLabel.setText(String.format("Time: %02d:%02d", seconds / 60, seconds % 60));
    }

    private void updateGrid() {
        BlockType[][] logicMap = gameLogic.getLogicMap();
        GridRender.renderBlockGrid(cells, logicMap);
        UpgradeRender.render(gameLogic.getUpgrades(), cells);
    }

    private JLayeredPane buildLayeredPane(JPanel gridPanel) {
        int width = gridPanel.getPreferredSize().width;
        int height = gridPanel.getPreferredSize().height;

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));
        layeredPane.setLayout(null);

        gridPanel.setBounds(0, 0, width, height);
        layeredPane.add(gridPanel, Integer.valueOf(0));

        pacmanLabel = createEntityLabel(gameLogic.getPacman().getX(), gameLogic.getPacman().getY(), 1, layeredPane);
        pacmanAnimation = new PacmanAnimation(pacmanLabel, gameLogic.getPacman());
        pacmanAnimation.start();

        for (Ghost ghost : gameLogic.getGhosts()) {
            JLabel ghostLabel = createEntityLabel(ghost.getX(), ghost.getY(), 1, layeredPane);
            ghostLoops.add(new GhostLoop(ghost, gameLogic.getLogicMap(), ghostLabel, gameLogic, this));
            new GhostAnimation(ghostLabel, ghost).start();
        }

        Point leftPortal = gameLogic.getLeftPortalPos();
        leftPortalLabel = createEntityLabel(leftPortal.x * blockSize, leftPortal.y * blockSize, 1, layeredPane);
        new PortalAnimation(leftPortalLabel, Resources.leftPortalFrames).start();

        Point rightPortal = gameLogic.getRightPortalPos();
        rightPortalLabel = createEntityLabel(rightPortal.x * blockSize, rightPortal.y * blockSize, 1, layeredPane);
        new PortalAnimation(rightPortalLabel, Resources.rightPortalFrames).start();

        pausePanel = new PausePanel(this::resumeGame, () -> {
            gameController.stopAll();
            mainWindow.showGameMenu();
        });
        pausePanel.setBounds(0, 0, width, height);
        pausePanel.setBorder(new LineBorder(new Color(200, 100, 10), 5));
        pausePanel.setVisible(false);
        layeredPane.add(pausePanel, Integer.valueOf(3));

        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Jokerman", Font.BOLD, 100));
        countdownLabel.setForeground(new Color(150, 10, 10));
        countdownLabel.setBounds(0, height / 2 - 50, width, 100);
        countdownLabel.setVisible(true);
        layeredPane.add(countdownLabel, Integer.valueOf(2));

        return layeredPane;
    }


    private JLabel createEntityLabel(int x, int y, int layer, JLayeredPane pane) {
        JLabel label = new JLabel();
        label.setBounds(x, y, blockSize, blockSize);
        pane.add(label, Integer.valueOf(layer));
        return label;
    }

}



