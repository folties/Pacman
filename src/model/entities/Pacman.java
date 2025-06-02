package model.entities;

import model.map.BlockType;
import util.Direction;
import util.Resources;

import java.awt.*;
import java.util.Map;

/**
 * Represents the player character (Pacman) with movement logic,
 * collision detection, food collection, and portal teleportation.
 */
public class Pacman extends Entity implements Walkable {

    // === Constants ===
    private static final float EPSILON = 0.1f;
    private static final int blockSize = 45;
    private Map<Direction, Image[]> frames;




    // === State ===
    private float  x, y;
    private int lives = 3;
    private int score = 0;
    private Direction nextDirection;

    private boolean isProtected = false;


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
        this.frames = Resources.pacmanFrames;
    }

    // === Movement and Game Logic ===

    /** Called every frame to update Pacman's movement and interactions. */
    public void stepMove(BlockType[][] logicMap) {
        // At tile center: update direction if nextDirection is valid
        if (isAlignedToGrid()){
            row = (int) (y / blockSize);
            col = (int) (x / blockSize);

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
        float dx = 0, dy = 0;
        switch (direction) {
            case UP -> dy = -speed;
            case DOWN -> dy = speed;
            case LEFT -> dx = -speed;
            case RIGHT -> dx = speed;
        }


        float  nextX = x + dx;
        float  nextY = y + dy;

        // Move if no wall ahead
        if (canMoveTo(nextX, nextY, logicMap)) {
            x = nextX;
            y = nextY;

            if (x % blockSize == 0 && y % blockSize == 0) {
                row = (int) (y / blockSize);
                col = (int) (x / blockSize);

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

    public boolean isAlignedToGrid() {
        return Math.abs(x % blockSize) < EPSILON && Math.abs(y % blockSize) < EPSILON;
    }

    private void teleportTo(Point portalPos) {
        x = portalPos.x * blockSize;
        y = portalPos.y * blockSize;
        row = portalPos.y;
        col = portalPos.x;
    }

    private boolean canMoveTo(float  x, float  y, BlockType[][] logicMap) {
        int left = (int) x;
        int right = (int) (x + blockSize - 1);
        int top = (int) y;
        int bottom = (int) (y + blockSize - 1);

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

    public void revertSpeed(float originalSpeed) {
        this.speed = originalSpeed;

        // Fix alignment if Pacman is off-grid after speed change
        if (!isAlignedToGrid()) {
            this.x = Math.round(x / blockSize) * blockSize;
            this.y = Math.round(y / blockSize) * blockSize;
            this.col = (int) (x / blockSize);
            this.row = (int) (y / blockSize);
        }
    }

    public void gainLife() {
        if (lives < 3) {
            lives++;
        }
    }



    // === Getters ===


    @Override
    public Direction getDirection() { return direction; }
    @Override
    public void setDirection(Direction direction) { this.nextDirection = direction; }
    @Override public int getX() { return Math.round(x); }
    @Override public int getY() { return Math.round(y); }
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

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean value) {
        this.isProtected = value;
    }

    public void setFrames(Map<Direction, Image[]> frames) {
        this.frames = frames;
    }
    public Map<Direction, Image[]> getFrames() {
        return frames;
    }

    public float  getSpeed() { return speed; }
}
