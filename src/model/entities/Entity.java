package model.entities;

import util.Direction;


public abstract class Entity {

    protected int row, col;
    protected Direction direction;
    protected float speed;

    public Entity(int row, int col, Direction direction, float speed) {
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.speed = speed;
    }

//     public int getRow() { return row; }
//     public int getCol() { return col; }
//     public Direction getDirection() { return direction; }
//     public float getSpeed() { return speed; }
//     public void setDirection(Direction direction) { this.direction = direction; }
}
