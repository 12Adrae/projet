package model;

import util.Position;


import java.util.List;
import java.util.Map;
import java.util.Set;

public class MotorizedFireFighter extends Firefighter{

    public MotorizedFireFighter(Position position, model.TargetStrategy targetStrategy) {
        super(position, targetStrategy);
    }

    @Override
    public void moveTowardsFire(Set<Position> firePositions, Map<Position, List<Position>> neighbors) {
        Position current = getPosition();

        //Premier mouvement
        Position firstMove = targetStrategy.neighborClosestToFire(current, firePositions, neighbors);
        if (firstMove != null) {
            current = firstMove;
        }

        //DEuxieme mouvement
        Position secondMove = targetStrategy.neighborClosestToFire(current, firePositions, neighbors);
        if (secondMove != null) {
            current = secondMove;
        }

        setPosition(current);
    }
}
