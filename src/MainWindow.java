import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainWindow() {
        setTitle("Pacman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Panels (pass MainWindow for control)
        GameMenu menuPanel = new GameMenu(this);
        BoardOption optionPanel = new BoardOption(this); // stub or full
        GamingWindow gamePanel = new GamingWindow(21, 19); // default size

        cardPanel.add(menuPanel, "menu");
        cardPanel.add(optionPanel, "options");
        cardPanel.add(gamePanel, "game"); //TODO: duplicate

        setContentPane(cardPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showScreen(String name) {
        cardLayout.show(cardPanel, name);
    }

    public void showGame(int rows, int cols) {
        GamingWindow game = new GamingWindow(rows, cols);
        cardPanel.add(game, "game"); // Add new instance //TODO: duplicate
        cardLayout.show(cardPanel, "game");
        pack(); // Resize window to match new game panel
    }
}
