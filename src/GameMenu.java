import javax.swing.*;
import java.awt.*;

public class GameMenu extends JPanel {
    public GameMenu(MainWindow mainWindow) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0, 0, 20));

        JLabel name = new JLabel("PACMAN");
        name.setFont(new Font("Calypso", Font.BOLD, 70));
        name.setForeground(Color.white);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton newGameButton = createMenuButton("New Game");
        JButton highScoresButton = createMenuButton("High Scores");
        JButton exitButton = createMenuButton("Exit");

        add(Box.createVerticalStrut(250));
        add(name);
        add(Box.createVerticalStrut(50));
        add(newGameButton);
        add(Box.createVerticalStrut(10));
        add(highScoresButton);
        add(Box.createVerticalStrut(10));
        add(exitButton);

        newGameButton.addActionListener(e -> mainWindow.showScreen("options"));
        highScoresButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "High Scores Clicked"));
        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("TOYZ", Font.BOLD, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(30, 30, 120));
        button.setForeground(Color.white);
        button.setMaximumSize(new Dimension(300, 60));
        button.setFocusPainted(false);
        return button;
    }
}
