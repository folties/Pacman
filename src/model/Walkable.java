package model;

public interface Walkable {
    public void move(BlockType[][] logicMap);
    int getRow();
    int getCol();
    Direction getDirection();
    void setDirection(Direction direction);
}
