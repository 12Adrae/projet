package model;

import util.Position;


import java.util.List;
import java.util.Map;
import java.util.Set;

public class MotorizedFireFighter extends Firefighter{

    public MotorizedFireFighter(Position position, model.TargetStrategy targetStrategy, Set<Position> mountainPositions) {
        super(position, targetStrategy, mountainPositions);
    }

    @Override
    public void moveTowardsFire(Set<Position> firePositions, Map<Position, List<Position>> neighbors, Set<Position> mountainPositions) {
        Position current = getPosition();

        // Premier mouvement
        Position firstMove = targetStrategy.neighborClosestToFire(current, firePositions, neighbors);
        if (firstMove != null && !mountainPositions.contains(firstMove)) {
            current = firstMove;
        }

        // Deuxième mouvement
        Position secondMove = targetStrategy.neighborClosestToFire(current, firePositions, neighbors);
        if (secondMove != null && !mountainPositions.contains(secondMove)) {
            current = secondMove;
        }

        // Mise à jour de la position finale après deux mouvements
        setPosition(current);
    }

}
