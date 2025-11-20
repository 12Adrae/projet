package model;
import util.Position;
import util.TargetStrategy;
import java.util.*;

public class Firefighter {
    private Position position; //la position actuel de pompier
    private TargetStrategy strategy = new TargetStrategy(); //le voisin le plus proche  au feu


    public Firefighter(Position initialPosition) {
        this.position = initialPosition;
    }

    public Position getPosition() {
        return position;
    }

    public Position move(Set<Position> firePositions, Map<Position, List<Position>> neighbors) {
        position = strategy.neighborClosestToFire(position, firePositions, neighbors);
        return position;
    }


    public List<Position> extinguish(Fire fire, Map<Position, List<Position>> neighbors) {
        List<Position> extinguished = new ArrayList<>();

        // Sur la même case
        if (fire.isOnFire(position)) {
            fire.extinguish(position);
            extinguished.add(position);
        }

        for (Position neighbor : neighbors.get(position)) {
            if (fire.isOnFire(neighbor)) {
                fire.extinguish(neighbor);
                extinguished.add(neighbor);
            }
        }

        return extinguished;
    }




}