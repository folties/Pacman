package main;

import gui.BoardOption;
import gui.GameMenu;
import gui.GamingWindow;

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
        GamingWindow gamePanel = new GamingWindow(21, 19);

        cardPanel.add(menuPanel, "menu");
        cardPanel.add(optionPanel, "options");
        cardPanel.add(gamePanel, "game");

        this.setContentPane(cardPanel);
        this.setVisible(true);

    }

    public void showScreen(String name) {
        cardLayout.show(cardPanel, name);
    }

    public void showGame(int rows, int cols) {
        GamingWindow game = new GamingWindow(rows, cols);
        cardPanel.add(game, "game");
        cardLayout.show(cardPanel, "game");
        pack();
    }
}
