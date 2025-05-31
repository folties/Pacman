package gui;

import javax.swing.*;
import java.awt.*;

public class PausePanel extends JPanel {
    public PausePanel(Runnable onContinue, Runnable onQuit) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0, 0, 0, 180)); // semi-transparent black
        setOpaque(true);

        JLabel title = new JLabel("PAUSED");
        title.setFont(new Font("Rockwell", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton continueButton = new JButton("Continue");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(e -> onContinue.run());

        JButton quitButton = new JButton("Quit to Menu");
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.addActionListener(e -> onQuit.run());

        add(Box.createVerticalStrut(100));
        add(title);
        add(Box.createVerticalStrut(30));
        add(continueButton);
        add(Box.createVerticalStrut(10));
        add(quitButton);
    }
}
