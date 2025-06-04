package gui;

import util.Resources;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PausePanel extends JPanel {

    private final Image backgroundImage;

    public PausePanel(Runnable onContinue, Runnable onQuit) {
        this.backgroundImage = Resources.pauseBackgroundImage;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBackground(Color.BLACK);

        JLabel title = new JLabel("PAUSED");
        title.setFont(new Font("Rockwell", Font.BOLD, 100));
        title.setForeground(new Color(80, 0, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton continueButton = createPauseButton("Continue");
        continueButton.addActionListener(e -> onContinue.run());

        JButton quitButton = createPauseButton("Quit to Menu");
        quitButton.addActionListener(e -> onQuit.run());

        add(Box.createVerticalStrut(200));
        add(title);
        add(Box.createVerticalStrut(60));
        add(continueButton);
        add(Box.createVerticalStrut(30));
        add(quitButton);
    }

    private JButton createPauseButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Rockwell", Font.BOLD, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.BLACK);
        button.setForeground(new Color(200, 100, 10));
        button.setBorder(new LineBorder(new Color(200, 100, 10), 1));
        button.setMaximumSize(new Dimension(300, 60));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent event) {
                button.setBackground(new Color(100, 0, 0));
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(Color.BLACK);
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
