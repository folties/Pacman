package model.entities;

import model.map.BlockType;
import util.Direction;


public interface Walkable {
    void stepMove(BlockType[][] logicMap);

    int getX();
    int getY();
    int getCol();
    int getRow();
    Direction getDirection();
    void setDirection(Direction direction);
}
