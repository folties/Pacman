package main;

import gui.*;
import model.map.MapType;
import javax.swing.*;
import java.awt.*;


public class MainWindow extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    public MainWindow() {
        setTitle("Pacman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        showGameMenu();

        setContentPane(cardPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showGameMenu() {
        removeOldGameIfPresent();
        GameMenu menuPanel = new GameMenu(this);
        cardPanel.add(menuPanel, "game_menu");
        cardLayout.show(cardPanel, "game_menu");
        pack();
    }

    public void showBoardOption() {
        removeOldGameIfPresent();
        BoardOption optionPanel = new BoardOption(this);
        cardPanel.add(optionPanel, "options");
        cardLayout.show(cardPanel, "options");
        pack();
    }

    public void showGame(MapType mapType) {
        removeOldGameIfPresent();
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

    private void removeOldGameIfPresent() {
        for (Component comp : cardPanel.getComponents()) {
            if (comp instanceof GamingWindow gw) {
                gw.stopAllLoops();
                cardPanel.remove(gw);
            }
        }
    }
}
