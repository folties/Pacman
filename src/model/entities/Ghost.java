package model.entities;

import model.map.BlockType;
import util.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Represents a single ghost entity in the game.
 * Handles movement logic, portal traversal, and animation direction.
 */
public class Ghost extends Entity implements Walkable {

    // === Constants & Start Position ===
    private static final int blockSize = 45;
    private final int startRow, startCol;

    // === Position and Direction ===
    private int x, y;

    // === Portals ===
    private Point leftPortalPos;
    private Point rightPortalPos;

    // === Animation ===
    private Map<Direction, Image[]> animationFrames;

    // === Constructor ===
    public Ghost(int row, int col) {
        super(row, col, Direction.DOWN, 45); // Initial direction and speed
        this.startRow = row;
        this.startCol = col;
        this.x = col * blockSize;
        this.y = row * blockSize;
    }

    // === Movement Logic ===
    public void stepMove(BlockType[][] logicMap) {
        if (x % blockSize == 0 && y % blockSize == 0) {
            row = y / blockSize;
            col = x / blockSize;

            // Handle portals
            if (logicMap[row][col] == BlockType.LEFT_PORTAL && rightPortalPos != null) {
                teleportTo(rightPortalPos);
            } else if (logicMap[row][col] == BlockType.RIGHT_PORTAL && leftPortalPos != null) {
                teleportTo(leftPortalPos);
            }

            // Determine valid directions (excluding reverse)
            List<Direction> validDirs = new ArrayList<>();
            for (Direction dir : Direction.values()) {
                if (dir == getOpposite(direction)) continue;

                int tryRow = row + (dir == Direction.UP ? -1 : dir == Direction.DOWN ? 1 : 0);
                int tryCol = col + (dir == Direction.LEFT ? -1 : dir == Direction.RIGHT ? 1 : 0);

                if (canMoveTo(tryCol * blockSize, tryRow * blockSize, logicMap)) {
                    validDirs.add(dir);
                }
            }

            // Choose new direction at intersections or dead ends
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
        int dx = deltaCol(direction) * speed;
        int dy = deltaRow(direction) * speed;

        int nextX = x + dx;
        int nextY = y + dy;

        if (canMoveTo(nextX, nextY, logicMap)) {
            x = nextX;
            y = nextY;
            row = y / blockSize;
            col = x / blockSize;
        }
    }

    private void teleportTo(Point portalPos) {
        this.x = portalPos.x * blockSize;
        this.y = portalPos.y * blockSize;
        this.row = portalPos.y;
        this.col = portalPos.x;
    }

    // === Movement Utilities ===

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

    // === State Reset ===

    public void resetPosition() {
        this.row = startRow;
        this.col = startCol;
        this.x = col * blockSize;
        this.y = row * blockSize;
        this.direction = Direction.DOWN;
    }

    // === Setters and Getters ===

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
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() { return speed; }



    @Override
    public int getX() { return x; }
    @Override
    public int getY() { return y; }

    @Override public Direction getDirection() {
        return direction;
    }
    @Override public void setDirection(Direction direction) {
        this.direction = direction; }

}
