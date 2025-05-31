package gui;

import util.ScoreEntry;
import util.ScoreSaver;

import javax.swing.*;
import java.awt.*;

public class GameEnd extends JPanel {
    public GameEnd(int finalScore, int finalSeconds) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setFont(new Font("Rockwell", Font.BOLD, 60));
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel scoreLabel = new JLabel("Your Score: " + finalScore);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Rockwell", Font.PLAIN, 30));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel timeLabel = new JLabel("Your Time: " + String.format("%02d:%02d", finalSeconds / 60, finalSeconds % 60));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Rockwell", Font.PLAIN, 30));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField nameField = new JTextField(15);
        JButton saveButton = new JButton("Save Score");

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.BLACK);
        formPanel.add(new JLabel("Enter Your Name:"));
        formPanel.add(nameField);
        formPanel.add(saveButton);

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                ScoreEntry entry = new ScoreEntry(name, finalScore, finalSeconds);
                ScoreSaver.saveScore(entry);
                saveButton.setEnabled(false);
                nameField.setEditable(false);
                JOptionPane.showMessageDialog(this, "Score saved!");
            }
        });

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.add(scoreLabel);
        centerPanel.add(timeLabel);
        centerPanel.add(formPanel);

        add(gameOverLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}



