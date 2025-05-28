package gui;

import main.MainWindow;
import util.Resources;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BoardOption extends JPanel {
    private Image backgroundImage;
        public BoardOption(MainWindow mainWindow) {
            backgroundImage = Resources.optionBackgroundImage;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JLabel name = new JLabel("Select Board Size");
            name.setFont(new Font("TOYZ", Font.BOLD, 50));
            name.setForeground(new Color(245, 200, 10));
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

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private JButton createMenuButton(MainWindow mainWindow, String text, int rows, int cols) {
        JButton button = new JButton(text);
        button.setFont(new Font("TOYZ", Font.BOLD, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.black);
        button.setBorder(new LineBorder(new Color(200,100,10), 1));
        button.setForeground(new Color(200,100,10));
        button.setMaximumSize(new Dimension(300, 60));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 0, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.black);
            }
        });

        button.addActionListener(e -> {
            mainWindow.showGame(rows, cols);
        });
        return button;
    }
}


