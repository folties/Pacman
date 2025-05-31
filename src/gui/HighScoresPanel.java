package gui;

import main.MainWindow;
import util.ScoreSaver;


import javax.swing.*;
import java.awt.*;


public class HighScoresPanel extends JPanel {
    private final MainWindow mainWindow;

    public HighScoresPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JTextArea scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Rockwell", Font.PLAIN, 40));
        scoreArea.setBackground(Color.BLACK);
        scoreArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(scoreArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("High Scores"));

        var lines = ScoreSaver.loadScoresText();

        if (lines.isEmpty()) {
            scoreArea.setText("No scores yet.");
        } else {
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    int time = Integer.parseInt(parts[2]);
                    String timeFormatted = String.format("%02d:%02d", time / 60, time % 60);
                    scoreArea.append(name + " — Score: " + score + " — Time: " + timeFormatted + "\n");
                }
            }
        }


        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Rockwell", Font.PLAIN, 18));
        backButton.addActionListener(e -> mainWindow.showScreen("menu"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}

