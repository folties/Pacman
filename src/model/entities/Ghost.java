package model.entities;

import model.map.BlockType;
import util.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Ghost extends Entity {
    private final int blockSize = 45;
    private int x, y;

    public Ghost(int row, int col) {
        super(row, col, Direction.UP, 15);
        this.x = col * blockSize;
        this.y = row * blockSize;
    }

    public void stepMove(BlockType[][] logicMap) {
        if (x % blockSize == 0 && y % blockSize == 0) {
            row = y / blockSize;
            col = x / blockSize;

            List<Direction> validDirs = new ArrayList<>();

            for (Direction dir : Direction.values()) {
                if (dir == getOpposite(direction)) continue; // skip reverse

                int tryRow = row + (dir == Direction.UP ? -1 : dir == Direction.DOWN ? 1 : 0);
                int tryCol = col + (dir == Direction.LEFT ? -1 : dir == Direction.RIGHT ? 1 : 0);

                int px = tryCol * blockSize;
                int py = tryRow * blockSize;

                if (canMoveTo(px, py, logicMap)) {
                    validDirs.add(dir);
                }
            }

            if (!validDirs.isEmpty()) {
                // 💡 Randomly choose a new direction at intersection — with some probability
                if (validDirs.size() > 1 || !isWalkable(logicMap, row + (direction == Direction.UP ? -1 : direction == Direction.DOWN ? 1 : 0),
                        col + (direction == Direction.LEFT ? -1 : direction == Direction.RIGHT ? 1 : 0))) {
                    Collections.shuffle(validDirs);
                    direction = validDirs.get(0);
                }
            }
        }


        // Рух на кожному кадрі
        int dx = 0, dy = 0;
        switch (direction) {
            case UP -> dy = -speed;
            case DOWN -> dy = speed;
            case LEFT -> dx = -speed;
            case RIGHT -> dx = speed;
        }

        int nextX = x + dx;
        int nextY = y + dy;

        if (canMoveTo(nextX, nextY, logicMap)) {
            x = nextX;
            y = nextY;
            row = y / blockSize;
            col = x / blockSize;
        }
    }



    private Direction getOpposite(Direction dir) {
        return switch (dir) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
        };
    }



    private Direction getRandomDirection(BlockType[][] logicMap) {
        List<Direction> directions = new ArrayList<>();
        System.out.println("[Ghost] Checking directions:");
        for (Direction dir : Direction.values()) {
            if (dir == getOpposite(direction)) continue; // ❌ не йдемо назад

            int tryRow = row + (dir == Direction.UP ? -1 : dir == Direction.DOWN ? 1 : 0);
            int tryCol = col + (dir == Direction.LEFT ? -1 : dir == Direction.RIGHT ? 1 : 0);
            int pixelX = tryCol * blockSize;
            int pixelY = tryRow * blockSize;
            System.out.print("  " + dir + ": ");
            if (canMoveTo(pixelX, pixelY, logicMap)) {
                directions.add(dir);
                System.out.println("✅ walkable");
            }else {
                System.out.println("❌ wall or out");
            }
        }

        if (directions.isEmpty()) return getOpposite(direction); // єдиний варіант — назад
        Collections.shuffle(directions);
        return directions.get(0);
    }



    private boolean canMoveTo(int x, int y, BlockType[][] logicMap) {
        int left = x;
        int right = x + blockSize - 1;
        int top = y;
        int bottom = y + blockSize - 1;

        return isWalkable(logicMap, top / blockSize, left / blockSize) &&
                isWalkable(logicMap, top / blockSize, right / blockSize) &&
                isWalkable(logicMap, bottom / blockSize, left / blockSize) &&
                isWalkable(logicMap, bottom / blockSize, right / blockSize);
    }

    private boolean isWalkable(BlockType[][] logicMap, int r, int c) {
        return r >= 0 && r < logicMap.length &&
                c >= 0 && c < logicMap[0].length &&
                logicMap[r][c] != BlockType.WALL;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
