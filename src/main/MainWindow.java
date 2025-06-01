package main;

import gui.*;
import model.map.MapType;

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

        showGameMenu();

        this.setContentPane(cardPanel);
        this.setVisible(true);

    }

    public void showGameMenu() {
        // ✅ Always clean old game first
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof GamingWindow gw) {
                gw.stopAllLoops();
                cardPanel.remove(gw);
                break;
            }
        }

        GameMenu menuPanel = new GameMenu(this);
        cardPanel.add(menuPanel, "game_menu");
        cardLayout.show(cardPanel, "game_menu");
        pack();
    }

    public void showBoardOption() {
        // ✅ Always clean old game first
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof GamingWindow gw) {
                gw.stopAllLoops();
                cardPanel.remove(gw);
                break;
            }
        }

        BoardOption optionPanel = new BoardOption(this);
        cardPanel.add(optionPanel, "options");
        cardLayout.show(cardPanel, "options");
        pack();
    }

    public void showGame(MapType mapType) {
        // ✅ Always clean old game first
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof GamingWindow gw) {
                gw.stopAllLoops();
                cardPanel.remove(gw);
                break;
            }
        }

        GamingWindow game = new GamingWindow(this, mapType);
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
