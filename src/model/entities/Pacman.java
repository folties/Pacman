package model.entities;

import model.map.BlockType;
import util.Direction;

import java.awt.*;

public class Pacman extends Entity implements Walkable {
    private Direction nextDirection;
    private int lives = 3;
    private int score = 0;
    private int x, y; // pixel positions
    private final int blockSize = 45; // adjust if needed
    private Point leftPortalPos;
    private Point rightPortalPos;
    private final int startRow, startCol;


    public Pacman(int row, int col) {
        super(row, col, Direction.LEFT, 3);
        this.nextDirection = Direction.LEFT;
        this.startRow = row;
        this.startCol = col;
        this.x = col * blockSize;
        this.y = row * blockSize;
    }

    public void resetPosition() {
        this.row = startRow;
        this.col = startCol;
        this.x = col * blockSize;
        this.y = row * blockSize;
        this.direction = Direction.LEFT;
        this.nextDirection = Direction.LEFT;
    }

    public void stepMove(BlockType[][] logicMap) {
        if (x % blockSize == 0 && y % blockSize == 0) {
            row = y / blockSize;
            col = x / blockSize;

            if (nextDirection != null) {
                int tryRow = row;
                int tryCol = col;
                switch (nextDirection) {
                    case UP -> tryRow--;
                    case DOWN -> tryRow++;
                    case LEFT -> tryCol--;
                    case RIGHT -> tryCol++;
                }
                if (isWalkable(logicMap, tryRow, tryCol)) {
                    direction = nextDirection;
                }
            }
        }

        int dx = 0, dy = 0;
        switch (direction) {
            case UP -> dy =- speed;
            case DOWN -> dy = speed;
            case LEFT -> dx = -speed;
            case RIGHT -> dx = speed;
        }

        int nextX = x + dx;
        int nextY = y + dy;

        if (canMoveTo(nextX, nextY, logicMap)) {
            x = nextX;
            y = nextY;

            if (x % blockSize == 0 && y % blockSize == 0) {
                row = y / blockSize;
                col = x / blockSize;

                if (logicMap[row][col] == BlockType.FOOD) {
                    score += 10;
                    logicMap[row][col] = BlockType.EMPTY;
                }
                // ⬇️ ADD THIS BLOCK HERE:
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
            }
        }
    }

    private boolean canMoveTo(int x, int y, BlockType[][] logicMap) {
        // Four corners of Pacman
        int left = x;
        int right = x + blockSize - 1;
        int top = y;
        int bottom = y + blockSize - 1;

        // Convert each corner to a row/col and check
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
    public void loseLife() {
        lives--;
    }

    @Override public int getRow() { return row; }
    @Override public int getCol() { return col; }
    @Override public Direction getDirection() { return direction; }
    @Override public void setDirection(Direction direction) { this.nextDirection = direction; }

    public int getSpeed() { return speed; }
    public int getLives() { return lives; }
    public int getScore() { return score; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPortalPositions(Point left, Point right) {
        this.leftPortalPos = left;
        this.rightPortalPos = right;
    }



}
