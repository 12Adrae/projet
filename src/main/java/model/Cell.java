package model;

import util.Position;

public abstract class Cell {
    protected Position position;

    public Cell(Position position) {
        this.position = position;
    }

    public abstract boolean canFireSpread();

    public abstract boolean canEntityMove();

    public abstract int getFireSpreadDelay();

    public Position getPosition() {
        return position;
    }
}