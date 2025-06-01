package model.entities;

import model.map.BlockType;
import util.Direction;

import java.awt.*;

/**
 * Represents the player character (Pacman) with movement logic,
 * collision detection, food collection, and portal teleportation.
 */
public class Pacman extends Entity implements Walkable {

    // === Constants ===
    private static final int blockSize = 45;

    // === State ===
    private int x, y;
    private int lives = 3;
    private int score = 0;
    private Direction nextDirection;

    // === Portals ===
    private Point leftPortalPos;
    private Point rightPortalPos;

    // === Initial Position ===
    private final int startRow;
    private final int startCol;

    // === Constructor ===
    public Pacman(int row, int col) {
        super(row, col, Direction.LEFT, 3); // default speed = 3
        this.startRow = row;
        this.startCol = col;
        this.nextDirection = Direction.LEFT;
        this.x = col * blockSize;
        this.y = row * blockSize;
    }

    // === Movement and Game Logic ===

    /** Called every frame to update Pacman's movement and interactions. */
    public void stepMove(BlockType[][] logicMap) {
        // At tile center: update direction if nextDirection is valid
        if (x % blockSize == 0 && y % blockSize == 0) {
            row = y / blockSize;
            col = x / blockSize;

            if (nextDirection != null) {
                int tryRow = row, tryCol = col;
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

        // Calculate movement
        int dx = 0, dy = 0;
        switch (direction) {
            case UP -> dy = (int) -speed;
            case DOWN -> dy = (int) speed;
            case LEFT -> dx = (int) -speed;
            case RIGHT -> dx = (int) speed;
        }

        int nextX = x + dx;
        int nextY = y + dy;

        // Move if no wall ahead
        if (canMoveTo(nextX, nextY, logicMap)) {
            x = nextX;
            y = nextY;

            if (x % blockSize == 0 && y % blockSize == 0) {
                row = y / blockSize;
                col = x / blockSize;

                // Collect food
                if (logicMap[row][col] == BlockType.FOOD) {
                    score += 10;
                    logicMap[row][col] = BlockType.EMPTY;
                }

                // Portal teleportation
                if (logicMap[row][col] == BlockType.LEFT_PORTAL && rightPortalPos != null) {
                    teleportTo(rightPortalPos);
                } else if (logicMap[row][col] == BlockType.RIGHT_PORTAL && leftPortalPos != null) {
                    teleportTo(leftPortalPos);
                }
            }
        }
    }

    private void teleportTo(Point portalPos) {
        x = portalPos.x * blockSize;
        y = portalPos.y * blockSize;
        row = portalPos.y;
        col = portalPos.x;
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

    // === Game State Management ===

    public void loseLife() {
        lives--;
    }

    public void resetPosition() {
        this.row = startRow;
        this.col = startCol;
        this.x = col * blockSize;
        this.y = row * blockSize;
        this.direction = Direction.LEFT;
        this.nextDirection = Direction.LEFT;
    }

    public void setPortalPositions(Point left, Point right) {
        this.leftPortalPos = left;
        this.rightPortalPos = right;
    }

    // === Getters ===


    @Override
    public Direction getDirection() { return direction; }
    @Override
    public void setDirection(Direction direction) { this.nextDirection = direction; }
    @Override
    public int getX() { return x; }
    @Override
    public int getY() { return y; }
    public int getLives() { return lives; }
    public int getScore() { return score; }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    @Override
    public int getCol() {
        return col;
    }
    @Override
    public int getRow() {
        return row;
    }



    public float  getSpeed() { return speed; }
}
