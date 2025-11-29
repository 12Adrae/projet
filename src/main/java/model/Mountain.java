package model;

import util.Position;


public class Mountain extends Cell {

    public Mountain(Position position) {
        super(position);
    }

    @Override
    public boolean canFireSpread() {
        return false; // Feu bloqué
    }

    @Override
    public boolean canEntityMove() {
        return false; // Tout le monde bloqué
    }

    @Override
    public int getFireSpreadDelay() {
        return Integer.MAX_VALUE; // Jamais de propagation
    }
}