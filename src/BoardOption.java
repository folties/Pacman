import javax.swing.*;
import java.awt.*;

public class BoardOption extends JPanel {

        public BoardOption(MainWindow mainWindow) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(0, 0, 20));

            JLabel name = new JLabel("Select Board Size");
            name.setFont(new Font("Calypso", Font.BOLD, 50));
            name.setForeground(Color.white);
            name.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton smallButton = createMenuButton(mainWindow, "Small", 17, 15);
            JButton mediumButton = createMenuButton(mainWindow, "Medium", 19, 17);
            JButton largeButton = createMenuButton(mainWindow, "Large", 21, 19);

            add(Box.createVerticalStrut(250));
            add(name);
            add(Box.createVerticalStrut(50));
            add(smallButton);
            add(Box.createVerticalStrut(10));
            add(mediumButton);
            add(Box.createVerticalStrut(10));
            add(largeButton);

        }

    private JButton createMenuButton(MainWindow mainWindow, String text, int rows, int cols) {
        JButton button = new JButton(text);
        button.setFont(new Font("TOYZ", Font.BOLD, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(30, 30, 120));
        button.setForeground(Color.white);
        button.setMaximumSize(new Dimension(300, 60));
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            mainWindow.showGame(rows, cols); // switch to game panel
        });
        return button;
    }
}

