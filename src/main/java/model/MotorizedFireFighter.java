package model;

import util.Position;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MotorizedFireFighter extends Firefighter {

    public MotorizedFireFighter(Position position, model.TargetStrategy targetStrategy, Set<Position> mountainPositions, Set<Position> roadPositions) {
        super(position, targetStrategy, mountainPositions, roadPositions);
    }

    @Override
    public void moveTowardsFire(Set<Position> firePositions, Map<Position, List<Position>> neighbors) {
        Position current = getPosition();

        // Premier mouvement
        Position firstMove = targetStrategy.neighborClosestToFire(current, firePositions, neighbors);
        if (firstMove != null && (!super.mountainPositions.contains(firstMove) || super.roadPositions.contains(firstMove))) {
            current = firstMove;
        }

        // Deuxième mouvement
        Position secondMove = targetStrategy.neighborClosestToFire(current, firePositions, neighbors);
        if (secondMove != null && (!super.mountainPositions.contains(secondMove) || super.roadPositions.contains(secondMove))) {
            current = secondMove;
        }

        // Mise à jour de la position finale après deux mouvements
        setPosition(current);
    }
}
