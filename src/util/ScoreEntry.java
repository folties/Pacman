package util;

import java.io.Serializable;

public class ScoreEntry implements Serializable {
    private final String name;
    private final int score;
    private final int timeInSeconds;

    public ScoreEntry(String name, int score, int timeInSeconds) {
        this.name = name;
        this.score = score;
        this.timeInSeconds = timeInSeconds;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    @Override
    public String toString() {
        return name + " - Score: " + score + ", Time: " + (timeInSeconds / 60) + "m " + (timeInSeconds % 60) + "s";
    }
}
