package model;

import util.Position;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Cette classe est responsable des actions des pompiers : se déplacer vers le feu
 * le plus proche et éteindre les feux voisins
 *
 */

public abstract class Firefighter {
    private Position position;
    final model.TargetStrategy targetStrategy;

    public Firefighter(Position position, model.TargetStrategy targetStrategy){
        this.position = position;
        this.targetStrategy = targetStrategy;
    }

    public Position getPosition(){
        return position;
    }

    public void setPosition(Position position){
        this.position = position;
    }

    public void moveTowardsFire(Set<Position> firePositions, Map<Position, List<Position>> neighbors){
        Position newPosition = targetStrategy.neighborClosestToFire(position, firePositions, neighbors);
        if (newPosition != null){
            this.position = newPosition;
        }
    }

    public void extinguishFiresAround(Set<Position> firePositions, Map<Position, List<Position>> neighbors) {
        // Éteint le feu à la position actuelle du pompier, s'il y en a un
        firePositions.remove(position);

        // Éteint les feux dans les positions adjacentes
        List<Position> adjacentPositions = neighbors.get(position);
        for (Position neighbor : adjacentPositions) {
            firePositions.remove(neighbor);
        }
    }
}
