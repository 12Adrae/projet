package model;
import util.Position;
import java.util.*;

public class Fire {

    private Set<Position> positions;
    private Map<Position, List<Position>> neighbors;

    public Fire(Set<Position> initialPositions, Map<Position, List<Position>> neighbors) {
        this.positions = new HashSet<>(initialPositions);
        this.neighbors = neighbors;
    }
    //return les cases en feu
    public Set<Position> getPositions() {
        return positions;
    }

    //verification d'un feu sur une case
    public boolean isOnFire(Position position) {
        return positions.contains(position);
    }

    //extenction d'un feu
    public void extinguish(Position position) {
        positions.remove(position);
    }

    //la propagation du feu
    public List<Position> spread(int step) {
        List<Position> newFires = new ArrayList<>();
        if (step % 2 == 0) { // feu se propage un tour sur deux
            for (Position fire : positions) {
                List<Position> voisins = neighbors.get(fire);
                newFires.addAll(voisins);
            }
            positions.addAll(newFires);
        }
        return newFires;
    }

}
