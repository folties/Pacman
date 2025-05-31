package model.entities;

import model.map.BlockType;
import util.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Ghost extends Entity {
    private final int blockSize = 45;
    private int x, y;
    private Point leftPortalPos;
    private Point rightPortalPos;
    private Map<Direction, Image[]> animationFrames;

    public Ghost(int row, int col) {
        super(row, col, Direction.UP, 15);
        this.x = col * blockSize;
        this.y = row * blockSize;
    }

    public void stepMove(BlockType[][] logicMap) {
        if (x % blockSize == 0 && y % blockSize == 0) {
            row = y / blockSize;
            col = x / blockSize;

            if (logicMap[row][col] == BlockType.LEFT_PORTAL && rightPortalPos != null) {
                x = rightPortalPos.x * blockSize;
                y = rightPortalPos.y * blockSize;
                row = rightPortalPos.y;
                col = rightPortalPos.x;
            } else if (logicMap[row][col] == BlockType.RIGHT_PORTAL && leftPortalPos != null) {
                x = leftPortalPos.x * blockSize;
                y = leftPortalPos.y * blockSize;
                row = leftPortalPos.y;
                col = leftPortalPos.x;
            }

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
    public void setPortalPositions(Point left, Point right) {
        this.leftPortalPos = left;
        this.rightPortalPos = right;
    }
    public void setAnimationFrames(Map<Direction, Image[]> frames) {
        this.animationFrames = frames;
    }
    public Image[] getFramesForDirection(Direction direction) {
        return animationFrames.get(direction);
    }
    public Direction getDirection() {
        return direction;
    }

}
