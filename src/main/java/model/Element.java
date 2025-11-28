package model;

import util.Position;


public interface Element {

    void update(FirefighterBoard board);
    Position getPosition();
}