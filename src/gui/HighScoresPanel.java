package gui;

import main.MainWindow;
import util.ScoreEntry;
import util.ScoreSaver;


import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class HighScoresPanel extends JPanel {
    private final MainWindow mainWindow;

    public HighScoresPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JTextArea scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Rockwell", Font.PLAIN, 25));
        scoreArea.setBackground(Color.BLACK);
        scoreArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(scoreArea);
        TitledBorder border = BorderFactory.createTitledBorder("High Scores");
        border.setTitleColor(new Color(220, 120, 20)); // change color
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitleFont(new Font("Rockwell", Font.BOLD, 50)); // change font and size

        scrollPane.setBorder(border);
        scrollPane.setBackground(Color.BLACK);

        List<String> lines = ScoreSaver.loadScoresText();
        List<ScoreEntry> entries = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                int time = Integer.parseInt(parts[2]);
                entries.add(new ScoreEntry(name, score, time));
            }
        }

        entries.sort((a, b) -> Integer.compare(b.getScore(), a.getScore())); // descending by score

        if (entries.isEmpty()) {
            scoreArea.setText("No scores yet.");
        } else {
            for (ScoreEntry entry : entries) {
                String formattedTime = String.format("%02d:%02d", entry.getTime() / 60, entry.getTime() % 60);
                scoreArea.append(entry.getName() + " — Score: " + entry.getScore() + " — Time: " + formattedTime + "\n");
            }
        }


        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Rockwell", Font.BOLD, 30));
        backButton.addActionListener(e -> mainWindow.showGameMenu());
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBackground(Color.black);
        backButton.setBorder(new LineBorder(new Color(200,100,10), 2));
        backButton.setForeground(new Color(200,100,10));
        backButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50)); // Optional height
        backButton.setFocusPainted(false);

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(100, 0, 0));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.black);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}

