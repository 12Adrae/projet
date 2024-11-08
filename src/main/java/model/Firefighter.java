package model;

import util.Position;

public class Firefighter {
    private Position position;

    public Firefighter(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void move(Position newPosition) {
        this.position = newPosition;
    }
}
