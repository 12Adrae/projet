package model;
import util.Position;
import util.TargetStrategy;
import java.util.*;


public class Firefighter implements Element {

    private Position position;
    private TargetStrategy strategy = new TargetStrategy();

    public Firefighter(Position initialPosition) {
        this.position = initialPosition;
    }

    @Override
    public Set<Position> getPositions() {
        return Set.of(position);
    }

    @Override
    public ModelElement getType() {
        return ModelElement.FIREFIGHTER;
    }

    public Position move(Set<Position> firePositions,
                         Map<Position, List<Position>> neighbors) {

        position = strategy.neighborClosestToFire(position, firePositions, neighbors);
        return position;
    }

    @Override
    public List<Position> update(Map<Position, List<Position>> neighbors,
                                 FirefighterBoard board) {

        List<Position> modified = new ArrayList<>();
        Position oldPos = position;

        // récupérer l'élément Fire dans la liste des elements
        Fire fire = null;
        for (Element e : board.getElements()) {
            if (e instanceof Fire f) {
                fire = f;
                break;
            }
        }
        // move
        Position newPos = move(fire.getPositions(), neighbors);

        modified.add(oldPos);
        modified.add(newPos);

        // extinction sur place
        if (fire.isOnFire(newPos)) {
            fire.extinguish(newPos);
            modified.add(newPos);
        }

        // extinction autour
        for (Position neighbor : neighbors.get(newPos)) {
            if (fire.isOnFire(neighbor)) {
                fire.extinguish(neighbor);
                modified.add(neighbor);
            }
        }

        return modified;
    }
}
