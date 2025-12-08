package model;
import util.Position;
import java.util.*;


public class Fire implements Element {

    private Set<Position> firePositions;
    private Map<Position, List<Position>> neighbors;
    private int step = 0;

    public Fire(Set<Position> initialPositions,
                Map<Position, List<Position>> neighbors) {
        this.firePositions = new HashSet<>(initialPositions);
        this.neighbors = neighbors;
    }

    @Override
    public Set<Position> getPositions() {
        return firePositions;
    }

    public boolean isOnFire(Position position) {
        return firePositions.contains(position);
    }

    public void extinguish(Position position) {
        firePositions.remove(position);
    }

    @Override
    public ModelElement getType() {
        return ModelElement.FIRE;
    }

    @Override
    public List<Position> update(Map<Position, List<Position>> ignored, FirefighterBoard board) {

        List<Position> newFires = new ArrayList<>();

        if (step % 2 == 0) {

            for (Position p : firePositions) {
                for (Position neighbor : neighbors.get(p)) {

                    Terrain t = board.getTerrain(neighbor);

                    if (!t.fireCanCross()) continue;

                    if (t.fireDelay() > 0 &&
                            (step / 2) % t.fireDelay() != 0)
                        continue;
                    newFires.add(neighbor);
                }
            }

            firePositions.addAll(newFires);
        }

        step++;
        return newFires;
    }
}
