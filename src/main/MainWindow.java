package main;

import gui.*;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainWindow() {
        this.setTitle("Pacman");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        GameMenu menuPanel = new GameMenu(this);
        BoardOption optionPanel = new BoardOption(this);
        GamingWindow gamePanel = new GamingWindow(this,21, 19);

        cardPanel.add(menuPanel, "menu");
        cardPanel.add(optionPanel, "options");
        cardPanel.add(gamePanel, "game");

        this.setContentPane(cardPanel);
        this.setVisible(true);

    }

    public void showScreen(String name) {
        if (name.equals("menu")) {
            // Remove any old game panels
            for (Component comp : cardPanel.getComponents()) {
                if (comp instanceof GamingWindow gw) {
                    gw.stopAllLoops();          // 🔒 stop background threads
                    cardPanel.remove(gw);       // 🗑️ remove it from memory/UI
                    break;
                }
            }
        }

        cardLayout.show(cardPanel, name);
        pack();
    }

    public void showGame(int rows, int cols) {
        GamingWindow game = new GamingWindow(this, rows, cols);
        cardPanel.add(game, "game");
        cardLayout.show(cardPanel, "game");
        pack();
    }
    public void showGameEnd(int finalScore, int finalTime) {
        GameEnd endPanel = new GameEnd(this, finalScore, finalTime);
        cardPanel.add(endPanel, "game_end");
        cardLayout.show(cardPanel, "game_end");
        pack();
    }
    public void showHighScores() {
        HighScoresPanel panel = new HighScoresPanel(this);
        cardPanel.add(panel, "highscores");
        cardLayout.show(cardPanel, "highscores");
        pack();
    }

}
