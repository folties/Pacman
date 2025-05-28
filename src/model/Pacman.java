package model;

public class Pacman implements Walkable {
    private int row, col;
    private Direction direction;
    private Direction nextDirection;
    private int speed = 1;
    private int lives = 3;
    private int score = 0;

    public Pacman(int row, int col) {
        this.row = row;
        this.col = col;
        this.direction = Direction.LEFT;
        this.nextDirection = Direction.LEFT;
    }

    @Override
    public void move(BlockType[][] logicMap) {
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

        int newRow = row, newCol = col;
        switch (direction) {
            case UP -> newRow--;
            case DOWN -> newRow++;
            case LEFT -> newCol--;
            case RIGHT -> newCol++;
        }

        if (isWalkable(logicMap, newRow, newCol)) {
            row = newRow;
            col = newCol;

            if (logicMap[row][col] == BlockType.FOOD) {
                score += 10;
                logicMap[row][col] = BlockType.EMPTY;
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
}
