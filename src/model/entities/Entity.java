package model.entities;

import util.Direction;

public abstract class Entity {
    protected int row, col;
    protected Direction direction;
    protected int speed; // e.g., 1 tile per step

    public Entity(int row, int col, Direction direction, int speed) {
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.speed = speed;
    }
}
