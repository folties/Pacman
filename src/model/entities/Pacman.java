package model.entities;

import game.Logic;
import model.map.BlockType;
import util.Direction;
import util.Resources;
import java.awt.*;
import java.util.Map;

public class Pacman extends Entity implements Walkable {

    private static final float step = 0.1f;
    private static final int blockSize = Resources.BLOCK_SIZE;

    private float x;
    private float y;
    private int lives = 3;
    private int score = 0;

    private Direction nextDirection;
    private boolean isProtected = false;

    private final int startRow;
    private final int startCol;

    private Point leftPortalPos, rightPortalPos;
    private Map<Direction, Image[]> frames;
    private Logic logic;

    public Pacman(int row, int col) {
        super(row, col, Direction.LEFT, 3f); // Default speed = 3
        this.startRow = row;
        this.startCol = col;
        this.nextDirection = Direction.LEFT;
        this.x = col * blockSize;
        this.y = row * blockSize;
        this.frames = Resources.pacmanFrames;
    }

    public void stepMove(BlockType[][] logicMap) {
        if (isAlignedToGrid()) {
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

        float dx = 0, dy = 0;
        switch (direction) {
            case UP -> dy = -speed;
            case DOWN -> dy = speed;
            case LEFT -> dx = -speed;
            case RIGHT -> dx = speed;
        }

        float nextX = x + dx;
        float nextY = y + dy;

        if (canMoveTo(nextX, nextY, logicMap)) {
            x = nextX;
            y = nextY;

            if (x % blockSize == 0 && y % blockSize == 0) {
                row = (int) (y / blockSize);
                col = (int) (x / blockSize);

                if (logicMap[row][col] == BlockType.FOOD) {
                    score += 10;
                    logicMap[row][col] = BlockType.EMPTY;
                }

                if (logicMap[row][col] == BlockType.LEFT_PORTAL && rightPortalPos != null) {
                    teleportTo(rightPortalPos);
                } else if (logicMap[row][col] == BlockType.RIGHT_PORTAL && leftPortalPos != null) {
                    teleportTo(leftPortalPos);
                }
            }
        }
    }

    private boolean canMoveTo(float x, float y, BlockType[][] logicMap) {
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

    public boolean isAlignedToGrid() {
        return Math.abs(x % blockSize) < step && Math.abs(y % blockSize) < step;
    }

    private void teleportTo(Point portalPos) {
        x = portalPos.x * blockSize;
        y = portalPos.y * blockSize;
        row = portalPos.y;
        col = portalPos.x;
    }

    public void loseLife() {
        lives--;
    }

    public void gainLife() {
        if (lives < 3) lives++;
    }

    public void resetPosition() {
        this.row = startRow;
        this.col = startCol;
        this.x = col * blockSize;
        this.y = row * blockSize;
        this.direction = Direction.LEFT;
        this.nextDirection = Direction.LEFT;
    }

    public void revertSpeed(float originalSpeed) {
        this.speed = originalSpeed;

        if (!isAlignedToGrid()) {
            this.x = Math.round(x / blockSize) * blockSize;
            this.y = Math.round(y / blockSize) * blockSize;
            this.col = (int) (x / blockSize);
            this.row = (int) (y / blockSize);
        }
    }

    public void setPortalPositions(Point left, Point right) {
        this.leftPortalPos = left;
        this.rightPortalPos = right;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    @Override
    public int getX() { return Math.round(x); }
    @Override
    public int getY() { return Math.round(y); }
    @Override
    public int getRow() { return row; }
    @Override
    public int getCol() { return col; }
    @Override
    public Direction getDirection() { return direction; }
    @Override
    public void setDirection(Direction direction) { this.nextDirection = direction; }

    public int getLives() { return lives; }
    public int getScore() { return score; }
    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public boolean isProtected() { return isProtected; }
    public void setProtected(boolean value) { this.isProtected = value; }
    public Map<Direction, Image[]> getFrames() { return frames; }
    public void setFrames(Map<Direction, Image[]> frames) { this.frames = frames; }
    public Logic getLogic() { return logic; }
}
