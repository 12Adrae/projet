package model;

import util.Position;

/**
 * Cellule route
 * - Feu NE PEUT PAS se propager
 * - SEULEMENT les pompiers peuvent passer
 * - Chemin de secours
 */
public class Road extends Cell {

    public Road(Position position) {
        super(position);
    }

    @Override
    public boolean canFireSpread() {
        return false; //  Feu bloqué sur routes
    }

    @Override
    public boolean canEntityMove() {
        return true; // Pompiers peuvent passer
    }

    @Override
    public int getFireSpreadDelay() {
        return Integer.MAX_VALUE; // Pas de propagation
    }
}