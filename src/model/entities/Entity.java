package model.entities;

import util.Direction;


public abstract class Entity {

    protected int row;
    protected int col;
    protected Direction direction;
    protected float speed;
    protected float x;
    protected float y;

    public Entity(int row, int col, Direction direction, float speed) {
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.speed = speed;
    }

    public int getX() { return Math.round(x); }
    public int getY() { return Math.round(y); }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public Direction getDirection() { return direction; }
    public abstract void setDirection(Direction direction);
}
