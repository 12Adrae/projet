package model;

import util.Position;

public class NormalCell extends Cell {

    public NormalCell(Position position) {
        super(position);
    }

    @Override
    public boolean canFireSpread() {
        return true; //  Le feu se propage
    }

    @Override
    public boolean canEntityMove() {
        return true; //  Tout le monde peut passer
    }

    @Override
    public int getFireSpreadDelay() {
        return 2; // Propagation tous les 2 tours
    }
}