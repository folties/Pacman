package model;

public class Pacman implements Walkable {
    private int row, col;
    private Direction direction;
    private Direction nextDirection;
    private int speed = 1;
    private int lives = 3;
    private int score = 0;
    private int x, y; // pixel positions
    private final int blockSize = 45; // adjust if needed

    public Pacman(int row, int col) {
        this.row = row;
        this.col = col;
        this.direction = Direction.LEFT;
        this.nextDirection = Direction.LEFT;

        this.x = col * blockSize;
        this.y = row * blockSize;
    }

    public void stepMove(BlockType[][] logicMap) {
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

        int dx = 0, dy = 0;
        switch (direction) {
            case UP -> dy = -1;
            case DOWN -> dy = 1;
            case LEFT -> dx = -1;
            case RIGHT -> dx = 1;
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
            }
            checkFoodCollision(logicMap);
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


    private void checkFoodCollision(BlockType[][] logicMap) {
        int left = x;
        int right = x + blockSize - 1;
        int top = y;
        int bottom = y + blockSize - 1;

        int row1 = top / blockSize;
        int row2 = bottom / blockSize;
        int col1 = left / blockSize;
        int col2 = right / blockSize;

        for (int r = row1; r <= row2; r++) {
            for (int c = col1; c <= col2; c++) {
                if (r >= 0 && r < logicMap.length && c >= 0 && c < logicMap[0].length) {
                    if (logicMap[r][c] == BlockType.FOOD) {
                        logicMap[r][c] = BlockType.EMPTY;
                        score += 10;
                    }
                }
            }
        }
    }

    private boolean isWalkable(BlockType[][] logicMap, int r, int c) {
        return r >= 0 && r < logicMap.length &&
                c >= 0 && c < logicMap[0].length &&
                logicMap[r][c] != BlockType.WALL;
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

}
