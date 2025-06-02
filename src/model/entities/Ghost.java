package model.entities;

import model.map.BlockType;
import util.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Ghost extends Entity implements Walkable {

    private static final int blockSize = 45;
    private static final float EPSILON = 0.1f;

    private static final float[] speedLevel = {1.5f, 2.5f, 3.0f, 4.5f, 6.0f, 7.5f, 9.0f, 15.0f, 22.5f};
    private int currentSpeedIndex = 0;

    private final int startRow, startCol;
    private float x, y;

    private Point leftPortalPos, rightPortalPos;
    private Map<Direction, Image[]> animationFrames;

    public Ghost(int row, int col) {
        super(row, col, Direction.DOWN, 1.25f); // Start with float speed
        this.startRow = row;
        this.startCol = col;
        this.x = col * blockSize;
        this.y = row * blockSize;
    }

    public void levelUpSpeed() {
        if (currentSpeedIndex < speedLevel.length - 1) {
            currentSpeedIndex++;
            this.speed = speedLevel[currentSpeedIndex];
        }
    }

    public void stepMove(BlockType[][] logicMap) {
        if (isAlignedToGrid()) {
            row = (int) (y / blockSize);
            col = (int) (x / blockSize);

            // Handle portals
            if (logicMap[row][col] == BlockType.LEFT_PORTAL && rightPortalPos != null) {
                teleportTo(rightPortalPos);
            } else if (logicMap[row][col] == BlockType.RIGHT_PORTAL && leftPortalPos != null) {
                teleportTo(leftPortalPos);
            }

            // Determine possible directions
            List<Direction> validDirs = new ArrayList<>();
            for (Direction dir : Direction.values()) {
                if (dir == getOpposite(direction)) continue;

                int tryRow = row + deltaRow(dir);
                int tryCol = col + deltaCol(dir);

                if (isWalkable(logicMap, tryRow, tryCol)) {
                    validDirs.add(dir);
                }
            }

            if (!validDirs.isEmpty()) {
                boolean atIntersection = validDirs.size() > 1;
                boolean blockedAhead = !isWalkable(logicMap, row + deltaRow(direction), col + deltaCol(direction));
                if (atIntersection || blockedAhead) {
                    Collections.shuffle(validDirs);
                    direction = validDirs.get(0);
                }
            }
        }

        // Move by current direction
        float dx = deltaCol(direction) * speed;
        float dy = deltaRow(direction) * speed;

        float nextX = x + dx;
        float nextY = y + dy;

        if (canMoveTo(nextX, nextY, logicMap)) {
            x = nextX;
            y = nextY;
            row = (int) (y / blockSize);
            col = (int) (x / blockSize);
        }
    }

    public boolean isAlignedToGrid() {
        return Math.abs(x % blockSize) < EPSILON && Math.abs(y % blockSize) < EPSILON;
    }

    private void teleportTo(Point portalPos) {
        this.x = portalPos.x * blockSize;
        this.y = portalPos.y * blockSize;
        this.row = portalPos.y;
        this.col = portalPos.x;
    }

    private int deltaRow(Direction dir) {
        return switch (dir) {
            case UP -> -1;
            case DOWN -> 1;
            default -> 0;
        };
    }

    private int deltaCol(Direction dir) {
        return switch (dir) {
            case LEFT -> -1;
            case RIGHT -> 1;
            default -> 0;
        };
    }

    private Direction getOpposite(Direction dir) {
        return switch (dir) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
        };
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

    public void resetPosition() {
        this.row = startRow;
        this.col = startCol;
        this.x = col * blockSize;
        this.y = row * blockSize;
        this.direction = Direction.DOWN;
    }

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

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    @Override public int getX() { return Math.round(x); }
    @Override public int getY() { return Math.round(y); }
    @Override public Direction getDirection() { return direction; }
    @Override public void setDirection(Direction direction) { this.direction = direction; }
    @Override
    public int getCol() {
        return col;
    }
    @Override
    public int getRow() {
        return row;
    }
}
