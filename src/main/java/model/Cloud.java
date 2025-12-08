package model;
import util.Position;
import java.util.*;


public class Cloud implements Element {
    private Position position;
    private final Random random = new Random();

    public Cloud(Position start) {
        this.position = start;
    }

    @Override
    public Set<Position> getPositions() {
        return Set.of(position);
    }

    @Override
    public ModelElement getType() {
        return ModelElement.CLOUD;
    }

    private Position move(Map<Position, List<Position>> neighbors) {
        List<Position> possibles = neighbors.get(position);

        if (!possibles.isEmpty()) {
            position = possibles.get(random.nextInt(possibles.size()));
        }

        return position;
    }

    @Override
    public List<Position> update(Map<Position, List<Position>> neighbors,
                                 FirefighterBoard board) {

        List<Position> modified = new ArrayList<>();
        Position oldPos = position;

        // déplacement
        Position newPos = move(neighbors);

        modified.add(oldPos);
        modified.add(newPos);

        // chercher l'élément Fire dans la liste des Element
        Fire fire = null;
        for (Element e : board.getElements()) {
            if (e instanceof Fire f) {
                fire = f;
                break;
            }
        }

        // extinction
        if (fire != null) {
            fire.extinguish(newPos);
            modified.add(newPos);
        }

        return modified;
    }
}
