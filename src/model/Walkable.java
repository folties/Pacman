package model;

public interface Walkable {

    int getRow();
    int getCol();
    Direction getDirection();
    void setDirection(Direction direction);
}
