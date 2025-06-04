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
import model.upgrades.Upgrade;
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
        int rows = size[0], cols = size[1];

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
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // зовнішній відступ

        scoreLabel = new JLabel("Score: 0");
        styleTopLabel(scoreLabel);
        topPanel.add(scoreLabel);

        topPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        timeLabel = new JLabel("Time: 00:00");
        styleTopLabel(timeLabel);
        topPanel.add(timeLabel);

        topPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        JPanel heartsPanel = new JPanel();
        heartsPanel.setLayout(new BoxLayout(heartsPanel, BoxLayout.X_AXIS));
        heartsPanel.setBackground(Color.BLACK);
        for (int i = 0; i < gameLogic.getPacman().getLives(); i++) {
            JLabel heart = new JLabel(Resources.heartIcon);
            heart.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // внутрішній відступ між серцями
            heartLabels.add(heart);
            heartsPanel.add(heart);
        }

        topPanel.add(heartsPanel);
    }

    private void styleTopLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Rockwell", Font.BOLD, 30));
    }

    private void initGridAndLayers(int rows, int cols) {
        JPanel gridPanel = buildGridPanel(rows, cols);
        JLayeredPane layeredPane = buildLayeredPane(gridPanel);

        GridBagConstraints gameGbc = new GridBagConstraints();
        gameGbc.gridx = 0;
        gameGbc.gridy = 1;
        add(layeredPane, gameGbc);
    }

    private void initTopPanel() {
        createTopPanel();

        GridBagConstraints topGbc = new GridBagConstraints();
        topGbc.gridx = 0;
        topGbc.gridy = 0;
        topGbc.insets = new Insets(10, 10, 10, 10);
        add(topPanel, topGbc);
    }

    private void startLoops() {
        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> countdownLabel.setText("3"));
                Thread.sleep(1000);
                SwingUtilities.invokeLater(() -> countdownLabel.setText("2"));
                Thread.sleep(1000);
                SwingUtilities.invokeLater(() -> countdownLabel.setText("1"));
                Thread.sleep(1000);
                SwingUtilities.invokeLater(() -> countdownLabel.setVisible(false));
            } catch (InterruptedException ignored) {}

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
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(blockSize, blockSize));
                cells[r][c] = label;

                Image image = Resources.getImageForBlockType(logicMap[r][c]);
                if (image != null) label.setIcon(new ImageIcon(image));

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
            JLabel ghostLabel = new JLabel();
            ghostLabel.setBounds(ghost.getX(), ghost.getY(), blockSize, blockSize);
            layeredPane.add(ghostLabel, Integer.valueOf(1));

            GhostLoop loop = new GhostLoop(ghost, gameLogic.getLogicMap(), ghostLabel, gameLogic, this);
            ghostLoops.add(loop);

            ghostAnimation = new GhostAnimation(ghostLabel, ghost);
            ghostAnimation.start();
        }

        pacmanAnimation = new PacmanAnimation(pacmanLabel, gameLogic.getPacman());
        pacmanAnimation.start();

        leftPortalLabel = new JLabel();
        Point leftPortal = gameLogic.getLeftPortalPos();
        leftPortalLabel.setBounds(leftPortal.x * blockSize, leftPortal.y * blockSize, blockSize, blockSize);
        layeredPane.add(leftPortalLabel, Integer.valueOf(1));

        rightPortalLabel = new JLabel();
        Point rightPortal = gameLogic.getRightPortalPos();
        rightPortalLabel.setBounds(rightPortal.x * blockSize, rightPortal.y * blockSize, blockSize, blockSize);
        layeredPane.add(rightPortalLabel, Integer.valueOf(1));

        new PortalAnimation(leftPortalLabel, Resources.leftPortalFrames).start();
        new PortalAnimation(rightPortalLabel, Resources.rightPortalFrames).start();

        pausePanel = new PausePanel(this::resumeGame, () -> {
            gameController.stopAll();
            mainWindow.showGameMenu();
        });
        pausePanel.setBounds(0, 0, width, height);
        pausePanel.setBorder(new LineBorder(new Color(200,100,10), 5));
        pausePanel.setVisible(false);
        layeredPane.add(pausePanel, Integer.valueOf(5));

        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Jokerman", Font.BOLD, 100));
        countdownLabel.setForeground(new Color(150,10,10));
        countdownLabel.setBounds(0, height / 2 - 50, width, 100);
        countdownLabel.setVisible(true);
        layeredPane.add(countdownLabel, Integer.valueOf(6)); // above pausePanel

        return layeredPane;
    }

    public void showCountdownThenResume() {
        paused = true;
        gameController.pauseGame();

        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> countdownLabel.setText("3"));
                Thread.sleep(1000);
                SwingUtilities.invokeLater(() -> countdownLabel.setText("2"));
                Thread.sleep(1000);
                SwingUtilities.invokeLater(() -> countdownLabel.setText("1"));
                Thread.sleep(1000);
                SwingUtilities.invokeLater(() -> {
                    countdownLabel.setVisible(false);
                    paused = false;
                    gameController.resumeGame();
                });
            } catch (InterruptedException ignored) {}
        }).start();
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

    private void updateHeartsUI() {
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
        for (int r = 0; r < logicMap.length; r++) {
            for (int c = 0; c < logicMap[0].length; c++) {
                Image image = Resources.getImageForBlockType(logicMap[r][c]);
                cells[r][c].setIcon(image != null ? new ImageIcon(image) : null);
            }
        }

        for (Upgrade upgrade : gameLogic.getUpgrades()) {
            if (!upgrade.isCollected()) {
                int x = upgrade.getX();
                int y = upgrade.getY();

                boolean visible = true;

                if (upgrade.isBlinking()) {
                    long time = System.currentTimeMillis();
                    visible = (time / 500) % 2 == 0;
                }

                if (visible) {
                    Image img = switch (upgrade.getClass().getSimpleName()) {
                        case "SpeedUpgrade" -> Resources.speedUpgradeImage;
                        case "ExtraLifeUpgrade" -> Resources.extraLifeUpgradeImage;
                        case "ProtectionUpgrade" -> Resources.protectionUpgradeImage;
                        default -> null;
                    };
                    if (img != null) {
                        cells[y][x].setIcon(new ImageIcon(img));
                    }
                } else {
                    cells[y][x].setIcon(null);
                }
            }
        }
    }
}



