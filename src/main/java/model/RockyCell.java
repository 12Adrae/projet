package model;

import util.Position;

public class RockyCell extends Cell {

    public RockyCell(Position position) {
        super(position);
    }

    @Override
    public boolean canFireSpread() {
        return true; //  Feu peut se propager
    }

    @Override
    public boolean canEntityMove() {
        return true; // Tout le monde peut passer
    }

    @Override
    public int getFireSpreadDelay() {
        return 4; //  4 tours au lieu de 2
    }
}