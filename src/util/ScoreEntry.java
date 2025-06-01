package util;

import java.io.Serializable;

public class ScoreEntry implements Serializable {
    private final String name;
    private final int score;
    private final int time;

    public ScoreEntry(String name, int score, int time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return name + " - Score: " + score + ", Time: " + (time / 60) + "m " + (time % 60) + "s";
    }
}
