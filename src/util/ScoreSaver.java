package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreSaver {
    private static final String FILE_PATH = "scores.dat";

    public static List<ScoreEntry> loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<ScoreEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void saveScore(ScoreEntry newEntry) {
        List<ScoreEntry> scores = loadScores();
        scores.add(newEntry);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
