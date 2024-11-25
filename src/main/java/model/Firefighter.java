package model;

import util.Position;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Cette classe est responsable des actions des pompiers : se déplacer vers le feu
 * le plus proche et éteindre les feux voisins
 *
 */

public class Firefighter {
    private Position position;
    final model.TargetStrategy targetStrategy;
    protected Set<Position> mountainPositions;
    protected Set<Position> roadPositions;

    public Firefighter(Position position, model.TargetStrategy targetStrategy, Set<Position> mountainPositions, Set<Position> roadPositions) {
        this.position = position;
        this.targetStrategy = targetStrategy;
        this.mountainPositions = mountainPositions != null ? mountainPositions : new HashSet<>();
        this.roadPositions = roadPositions != null ? roadPositions : new HashSet<>();

    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void moveTowardsFire(Set<Position> firePositions, Map<Position, List<Position>> neighbors) {
        Position newPosition = targetStrategy.neighborClosestToFire(position, firePositions, neighbors);
        if (newPosition != null && (!mountainPositions.contains(newPosition) || roadPositions.contains(newPosition))) {
            this.position = newPosition;
        }
    }

    public void extinguishFiresAround(Set<Position> firePositions, Map<Position, List<Position>> neighbors) {
        // Éteindre le feu à la position actuelle
        firePositions.remove(position);

        // Éteindre les feux dans les positions voisines
        List<Position> adjacentPositions = neighbors.get(position);
        for (Position neighbor : adjacentPositions) {
            firePositions.remove(neighbor);
        }
    }
}
