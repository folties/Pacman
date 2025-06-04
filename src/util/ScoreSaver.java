package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreSaver {
    private static final String FILE_PATH = "scores.txt";

    public static void saveScoreText(String name, int score, int timeInSeconds) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(name + "," + score + "," + timeInSeconds + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadScoresText() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
        }
        return lines;
    }
}
