package model;
import util.Position;
import java.util.*;

public class Fire implements Element {

    private Set<Position> positions;
    private Map<Position, List<Position>> neighbors;
    private Map<Position,Cell> terrain;
    private Map<Position,Integer> fireDelays=new HashMap<>();


    public Fire(Set<Position> initialPositions, Map<Position, List<Position>> neighbors,Map<Position,Cell> terrain) {
        this.positions = new HashSet<>(initialPositions);
        this.neighbors = neighbors;
        this.terrain = terrain;
        for (Position position : positions) {
            fireDelays.put(position, 0);
        }

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
    @Override
    public void update(FirefighterBoard board) {
        List<Position> newFires = new ArrayList<>();

        for (Position firePos : positions) {
            Cell cell = terrain.get(firePos);
            int delay = fireDelays.get(firePos);

            fireDelays.put(firePos, fireDelays.get(firePos)+1);
            if(fireDelays.get(firePos) > delay) {
                fireDelays.put(firePos, delay);
                for (Position neighborPos : neighbors.get(firePos)) {
                    Cell neighborCell = terrain.get(neighborPos);
                    if (neighborCell.canFireSpread() && !fireDelays.containsKey(neighborPos)) {
                        newFires.add(neighborPos);
                        fireDelays.put(neighborPos, 0);
                    }
                }
            }
            }
            positions.addAll(newFires);

    }

    //Le feu se met à jour lui-même
    /*@Override
    public void update(FirefighterBoard board) {
        // Le feu se propage selon le tour
        spread(board.stepNumber());


    }*/

    //retourne la première position
    @Override
    public Position getPosition() {
        if (positions.isEmpty()) {
            return null;
        }
        return positions.iterator().next();
    }
    }
