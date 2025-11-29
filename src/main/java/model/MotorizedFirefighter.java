package model;
import util.Position;
import java.util.*;


public class MotorizedFirefighter extends Firefighter {

    public MotorizedFirefighter(Position initialPosition) {
        super(initialPosition);
    }


    @Override
    public Position move(Set<Position> firePositions, Map<Position, List<Position>> neighbors) {

        for (int i = 0; i < 2; i++) {
            Position next = strategy.neighborClosestToFire(position, firePositions, neighbors);

            Cell cell = terrain.get(next);
            if (cell != null && !cell.canEntityMove())
                return position;

            position = next;
        }

        return position;
    }



}