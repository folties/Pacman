package gui;

import main.MainWindow;
import util.Resources;
import util.ScoreSaver;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameEnd extends JPanel {

    private final MainWindow mainWindow;
    private final Image backgroundImage;

    public GameEnd(MainWindow mainWindow, int finalScore, int finalSeconds) {
        this.mainWindow = mainWindow;
        this.backgroundImage = Resources.endBackgroundImage;

        setLayout(new BorderLayout());

        JLabel gameOverLabel = createLabel("Game Over", new Font("Rockwell", Font.BOLD, 100), new Color(100, 0, 0));
        JLabel scoreLabel = createLabel("Score: " + finalScore, new Font("Rockwell", Font.PLAIN, 30), new Color(150, 150, 150));
        JLabel timeLabel = createLabel("Time: " + String.format("%02d:%02d", finalSeconds / 60, finalSeconds % 60), new Font("Rockwell", Font.PLAIN, 30), new Color(150, 150, 150));
        JLabel nameLabel = createLabel("Label your game:", new Font("Rockwell", Font.PLAIN, 30), new Color(150, 150, 150));

        JTextField nameField = createNameField();
        JButton saveButton = createSaveButton(nameField, finalScore, finalSeconds);
        JButton returnButton = createReturnButton();

        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setBackground(Color.BLACK);
        centerContent.setOpaque(false);

        centerContent.add(Box.createVerticalStrut(280));
        centerContent.add(gameOverLabel);
        centerContent.add(Box.createVerticalStrut(30));
        centerContent.add(scoreLabel);
        centerContent.add(timeLabel);
        centerContent.add(Box.createVerticalStrut(30));
        centerContent.add(nameLabel);
        centerContent.add(Box.createVerticalStrut(30));
        centerContent.add(nameField);
        centerContent.add(Box.createVerticalStrut(30));
        centerContent.add(saveButton);
        centerContent.add(Box.createVerticalStrut(30));
        centerContent.add(returnButton);

        add(centerContent, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JTextField createNameField() {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(300, 60));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setBackground(new Color(150, 150, 150));
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Rockwell", Font.BOLD, 26));
        field.setBorder(new LineBorder(new Color(200, 100, 10), 4));
        field.setCaretColor(Color.ORANGE);
        field.setHorizontalAlignment(JTextField.CENTER);
        return field;
    }

    private JButton createSaveButton(JTextField nameField, int score, int seconds) {
        JButton button = styledButton("Save Score", 30);

        button.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                ScoreSaver.saveScoreText(name, score, seconds);
                button.setEnabled(false);
                nameField.setEditable(false);
            }
        });

        return button;
    }

    private JButton createReturnButton() {
        JButton button = styledButton("Quit", 40);
        button.addActionListener(e -> mainWindow.showGameMenu());
        return button;
    }

    private JButton styledButton(String text, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("Rockwell", Font.BOLD, fontSize));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.BLACK);
        button.setForeground(new Color(200, 100, 10));
        button.setBorder(new LineBorder(new Color(200, 100, 10), 1));
        button.setMaximumSize(new Dimension(300, 60));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                button.setBackground(new Color(100, 0, 0));
            }
            @Override
            public void mouseExited(MouseEvent event) {
                button.setBackground(Color.BLACK);
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
