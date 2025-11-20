package model;

import util.Position;
import java.util.*;

public class Fire extends Entity {

    public Fire(Position position) {
        super(position);
    }

    // Le feu se propage vers ses voisins
    public List<Fire> spread(Map<Position, List<Position>> neighbors) {
        List<Fire> newFires = new ArrayList<>();
        for (Position neighbor : neighbors.get(position)) {
            newFires.add(new Fire(neighbor));
        }
        return newFires;
    }

    @Override
    public void update(FirefighterBoard board) {
        // Logique de propagation (tous les 2 tours)
        if (board.stepNumber() % 2 == 0) {
            board.addNewFires(this.spread(board.getNeighbors()));
        }
    }
}