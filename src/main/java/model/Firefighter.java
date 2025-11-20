package model;

import util.Position;
import util.TargetStrategy;
import java.util.*;

public class Firefighter extends Entity {
    private final TargetStrategy targetStrategy;

    public Firefighter(Position position) {
        super(position);
        this.targetStrategy = new TargetStrategy();
    }

    // Se déplace vers le feu le plus proche
    public void moveTowardsFire(Set<Position> firePositions,
                                Map<Position, List<Position>> neighbors) {
        Position newPosition = targetStrategy.neighborClosestToFire(
                position, firePositions, neighbors
        );
        this.position = newPosition;
    }

    // Éteint les feux autour de lui
    public List<Position> extinguishNearbyFires(Map<Position, List<Position>> neighbors,
                                                Set<Position> firePositions) {
        List<Position> extinguishedPositions = new ArrayList<>();

        // Éteindre le feu sur sa position
        if (firePositions.contains(position)) {
            extinguishedPositions.add(position);
        }

        // Éteindre les feux voisins
        for (Position neighbor : neighbors.get(position)) {
            if (firePositions.contains(neighbor)) {
                extinguishedPositions.add(neighbor);
            }
        }

        return extinguishedPositions;
    }

    @Override
    public void update(FirefighterBoard board) {
        // Logique de déplacement et extinction
        moveTowardsFire(board.getFirePositions(), board.getNeighbors());
        List<Position> deadFires = extinguishNearbyFires(
                board.getNeighbors(),
                board.getFirePositions(
                )
        );
        board.removeFires(deadFires);
    }
}