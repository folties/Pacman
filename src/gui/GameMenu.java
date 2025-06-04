package gui;

import main.MainWindow;
import util.Resources;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * GameMenu is the main entry screen shown when the app starts.
 * Provides options to start a new game, view high scores, or exit.
 */
public class GameMenu extends JPanel {

    private final Image backgroundImage;

    public GameMenu(MainWindow mainWindow) {
        this.backgroundImage = Resources.menuBackgroundImage;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton newGameButton = createMenuButton("New Game");
        JButton highScoresButton = createMenuButton("High Scores");
        JButton exitButton = createMenuButton("Exit");

        add(Box.createVerticalStrut(400));
        add(newGameButton);
        add(Box.createVerticalStrut(10));
        add(highScoresButton);
        add(Box.createVerticalStrut(10));
        add(exitButton);

        newGameButton.addActionListener(e -> mainWindow.showBoardOption());
        highScoresButton.addActionListener(e -> mainWindow.showHighScores());
        exitButton.addActionListener(e -> System.exit(0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Rockwell", Font.BOLD, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.BLACK);
        button.setForeground(new Color(200, 100, 10));
        button.setBorder(new LineBorder(new Color(200, 100, 10), 1));
        button.setMaximumSize(new Dimension(300, 60));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 0, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK);
            }
        });

        return button;
    }
}
