package gui;

import main.MainWindow;
import model.map.MapType;
import util.Resources;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BoardOption extends JPanel {

    private final Image backgroundImage;

    public BoardOption(MainWindow mainWindow) {
        this.backgroundImage = Resources.optionBackgroundImage;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel name = createTitleLabel("Select Board Size");
        JButton smallButton = createOptionButton(mainWindow, "Small", MapType.SMALL);
        JButton mediumButton = createOptionButton(mainWindow, "Medium", MapType.MEDIUM);
        JButton largeButton = createOptionButton(mainWindow, "Large", MapType.LARGE);

        add(Box.createVerticalStrut(250));
        add(name);
        add(Box.createVerticalStrut(50));
        add(smallButton);
        add(Box.createVerticalStrut(10));
        add(mediumButton);
        add(Box.createVerticalStrut(10));
        add(largeButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private JButton createOptionButton(MainWindow mainWindow, String text, MapType mapType) {
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

        button.addActionListener(e -> mainWindow.showGame(mapType));

        return button;
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Rockwell", Font.BOLD, 50));
        label.setForeground(new Color(245, 200, 10));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
