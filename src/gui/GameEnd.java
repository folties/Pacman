package gui;

import main.MainWindow;
import util.Resources;
import util.ScoreSaver;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameEnd extends JPanel {
    private final MainWindow mainWindow;
    private Image backgroundImage;
    public GameEnd(MainWindow mainWindow, int finalScore, int finalSeconds) {

        backgroundImage = Resources.endBackgroundImage;
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setForeground(new Color(100, 0, 0));
        gameOverLabel.setFont(new Font("Rockwell", Font.BOLD, 100));
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Score: " + finalScore);
        scoreLabel.setForeground(new Color(150, 150 ,150));
        scoreLabel.setFont(new Font("Rockwell", Font.PLAIN, 30));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel timeLabel = new JLabel("Time: " + String.format("%02d:%02d", finalSeconds / 60, finalSeconds % 60));
        timeLabel.setForeground(new Color(150, 150 ,150));
        timeLabel.setFont(new Font("Rockwell", Font.PLAIN, 30));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Lable your game:");
        nameLabel.setForeground(new Color(150, 150 ,150));
        nameLabel.setFont(new Font("Rockwell", Font.PLAIN, 30));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField(20);
        nameField.setMaximumSize(new Dimension(300, 60)); // обмеж розмір по ширині
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setBackground(new Color(150, 150 ,150)); // фон
        nameField.setForeground(Color.BLACK); // колір тексту
        nameField.setFont(new Font("Rockwell", Font.BOLD, 26)); // шрифт і розмір
        nameField.setBorder(new LineBorder(new Color(200,100,10), 4));
        nameField.setCaretColor(Color.ORANGE); // колір курсора
        nameField.setHorizontalAlignment(JTextField.CENTER); // 👈 this centers the text

        JButton saveButton = new JButton("Save Score");
        saveButton.setFont(new Font("Rockwell", Font.BOLD, 30));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setBackground(Color.black);
        saveButton.setMaximumSize(new Dimension(250, 45));
        saveButton.setBorder(new LineBorder(new Color(200,100,10), 1));
        saveButton.setForeground(new Color(200,100,10));
        saveButton.setFocusPainted(false);

        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(100, 0, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(Color.black);
            }
        });

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                ScoreSaver.saveScoreText(name, finalScore, finalSeconds); // ✅ TXT only
                saveButton.setEnabled(false);
                nameField.setEditable(false);
            }
        });


        JButton returnButton = new JButton("Quit");
        returnButton.setFont(new Font("Rockwell", Font.BOLD, 40));
        returnButton.addActionListener(e -> mainWindow.showGameMenu());
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.setBackground(Color.black);
        returnButton.setBorder(new LineBorder(new Color(200,100,10), 1));
        returnButton.setMaximumSize(new Dimension(300, 60));
        returnButton.setForeground(new Color(200,100,10));
        returnButton.setFocusPainted(false);

        returnButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                returnButton.setBackground(new Color(100, 0, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                returnButton.setBackground(Color.black);
            }
        });

        // 🔻 CENTER layout (score, time, form)
        JPanel centerContent = new JPanel();
        centerContent.setBackground(Color.BLACK);
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));

        centerContent.add(Box.createVerticalStrut(280));  // push downward from top
        centerContent.add(gameOverLabel);
        centerContent.add(Box.createVerticalStrut(30));  // space after game over
        centerContent.add(scoreLabel);
        centerContent.add(timeLabel);
        centerContent.add(Box.createVerticalStrut(30));  // space before form
        centerContent.add(nameLabel);
        centerContent.add(Box.createVerticalStrut(30));
        centerContent.add(nameField);// space before form
        centerContent.add(Box.createVerticalStrut(30));
        centerContent.add(saveButton);
        centerContent.add(Box.createVerticalStrut(30));
        centerContent.add(returnButton);
        centerContent.setOpaque(false);

        add(centerContent, BorderLayout.CENTER);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}



