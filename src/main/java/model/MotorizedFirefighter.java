package model;
import util.Position;
import java.util.*;

public class MotorizedFirefighter extends Firefighter {

    public MotorizedFirefighter(Position initialPosition) {
        super(initialPosition);
    }

    @Override
    public ModelElement getType() {
        return ModelElement.MOTORIZEDFIREFIGHTER;
    }

    @Override
    public List<Position> update(Map<Position, List<Position>> neighbors,
                                 FirefighterBoard board) {
        List<Position> modified = new ArrayList<>();
        // récupérer l'élément Fire dans la liste
        Fire fire = null;
        for (Element e : board.getElements()) {
            if (e instanceof Fire f) {
                fire = f;
                break;
            }
        }

        // Position actuelle
        Position currentPos = this.getPositions().iterator().next();
        //1er déplacement
        Position currentPos1 = currentPos;
        Position new1 = move(fire.getPositions(), neighbors);
        modified.add(currentPos1);
        modified.add(new1);
        // extinction après 1er déplacement
        if (fire.isOnFire(new1)) {
            fire.extinguish(new1);
            modified.add(new1);
        }
        for (Position neighbor : neighbors.get(new1)) {
            if (fire.isOnFire(neighbor)) {
                fire.extinguish(neighbor);
                modified.add(neighbor);
            }
        }
        //2ème déplacement
        Position currentpos2 = new1;
        Position new2 = move(fire.getPositions(), neighbors);

        modified.add(currentpos2);
        modified.add(new2);
        // extinction après 2ème déplacement
        if (fire.isOnFire(new2)) {
            fire.extinguish(new2);
            modified.add(new2);
        }
        for (Position nei : neighbors.get(new2)) {
            if (fire.isOnFire(nei)) {
                fire.extinguish(nei);
                modified.add(nei);
            }
        }

        return modified;
    }
}
