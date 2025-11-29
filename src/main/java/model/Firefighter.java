package model;
import util.Position;
import util.TargetStrategy;
import java.util.*;

public class Firefighter implements Element {
    public Position position; //la position actuel de pompier
    public TargetStrategy strategy = new TargetStrategy(); //le voisin le plus proche  au feu
    public Map<Position, Cell> terrain;


    public Firefighter(Position initialPosition) {
        this.position = initialPosition;
    }

    /*public Position move(Set<Position> firePositions, Map<Position, List<Position>> neighbors) {
        Position newPosition = strategy.neighborClosestToFire(position,firePositions,neighbors);
        if(terrain != null) {
            Cell target = terrain.get(newPosition);
            if(!target.canEntityMove()) return position;
        }


    position = newPosition;
    return position;
}*/

    public Position move(Set<Position> firePositions, Map<Position, List<Position>> neighbors) {


        List<Position> available = new ArrayList<>();
        for (Position p : neighbors.get(position)) {
            Cell c = terrain.get(p);

            if (c == null || c.canEntityMove()) {
                available.add(p);
            }
        }

        if (available.isEmpty()) {
            return position;
        }

        Position next = strategy.neighborClosestToFire(position, firePositions, neighbors);

        position = next;
        return next;
    }






    public List<Position> extinguish(Fire fire, Map<Position, List<Position>> neighbors) {
        List<Position> extinguished = new ArrayList<>();

        // Sur la même case
        if (fire.isOnFire(position)) {
            fire.extinguish(position);
            extinguished.add(position);
        }

        List<Position> voisins = neighbors.get(position);
        if (voisins != null) {
            for (Position neighbor : voisins) {
                if (fire.isOnFire(neighbor)) {
                    fire.extinguish(neighbor);
                    extinguished.add(neighbor);
                }
            }
        }

        return extinguished;
    }



    @Override
    public Position getPosition() {
        return position;
    }


    @Override
    public void update(FirefighterBoard board) {

        if (terrain == null) {
            terrain = board.getTerrain();
        }
        // Se déplacer
        move(board.getFire().getPositions(), board.getNeighbors());
        // Éteindre les feux
        extinguish(board.getFire(), board.getNeighbors());
    }

    }