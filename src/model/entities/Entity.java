package model.entities;

import util.Direction;

public abstract class Entity {
    protected int row, col;
    protected Direction direction;
    protected float  speed; // e.g., 1 tile per step

    public Entity(int row, int col, Direction direction, float  speed) {
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.speed = speed;
    }

}
