package gui;

import main.MainWindow;
import util.ScoreEntry;
import util.ScoreSaver;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class HighScoresPanel extends JPanel {

    private final MainWindow mainWindow;

    public HighScoresPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JTextArea scoreArea = createScoreArea();
        JScrollPane scrollPane = createScrollPane(scoreArea);

        initializeScores(scoreArea);

        JButton backButton = createBackButton();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    private JTextArea createScoreArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Rockwell", Font.PLAIN, 25));
        area.setBackground(Color.BLACK);
        area.setForeground(Color.WHITE);
        return area;
    }

    private JScrollPane createScrollPane(JTextArea area) {
        JScrollPane scrollPane = new JScrollPane(area);
        TitledBorder border = BorderFactory.createTitledBorder("High Scores");
        border.setTitleColor(new Color(220, 120, 20));
        border.setTitleFont(new Font("Rockwell", Font.BOLD, 50));
        border.setTitleJustification(TitledBorder.CENTER);

        scrollPane.setBorder(border);
        scrollPane.setBackground(Color.BLACK);
        return scrollPane;
    }

    private void initializeScores(JTextArea area) {
        List<String> lines = ScoreSaver.loadScoresText();
        List<ScoreEntry> entries = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                try {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    int time = Integer.parseInt(parts[2]);
                    entries.add(new ScoreEntry(name, score, time));
                } catch (NumberFormatException e) {
                    System.out.println("can't initialize score: " + e.getMessage());
                }
            }
        }

        entries.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        if (entries.isEmpty()) {
            area.setText("no scores yet");
        } else {
            for (ScoreEntry entry : entries) {
                String timeStr = String.format("%02d:%02d", entry.getTime() / 60, entry.getTime() % 60);
                area.append(entry.getName() + " ------- Score: " + entry.getScore() + " ------- Time: " + timeStr + "\n");
            }
        }
    }

    private JButton createBackButton() {
        JButton button = new JButton("Back to Menu");
        button.setFont(new Font("Rockwell", Font.BOLD, 30));
        button.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.BLACK);
        button.setForeground(new Color(200, 100, 10));
        button.setBorder(new LineBorder(new Color(200, 100, 10), 2));

        button.addActionListener(e -> mainWindow.showGameMenu());

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                button.setBackground(new Color(100, 0, 0));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(Color.BLACK);
            }
        });

        return button;
    }
}
