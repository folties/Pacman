package gui;

import util.Resources;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PausePanel extends JPanel {
    private Image backgroundImage;
    public PausePanel(Runnable onContinue, Runnable onQuit) {
        backgroundImage = Resources.pauseBackgroundImage;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        JLabel title = new JLabel("PAUSED");
        title.setFont(new Font("Rockwell", Font.BOLD, 60));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton continueButton = createPauseButton("Continue");
        continueButton.addActionListener(e -> onContinue.run());

        JButton quitButton = createPauseButton("Quit to Menu");
        quitButton.addActionListener(e -> onQuit.run());

        add(Box.createVerticalStrut(150));
        add(title);
        add(Box.createVerticalStrut(50));
        add(continueButton);
        add(Box.createVerticalStrut(20));
        add(quitButton);
    }

    private JButton createPauseButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Rockwell", Font.BOLD, 30));
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

        return button;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (Resources.pauseBackgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
