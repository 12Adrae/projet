package model;

import util.Position;
import java.util.*;


public class Cloud implements Element {

    private Position position;
    private final Random random = new Random();


    public Cloud(Position position) {
        this.position = position;
    }


    @Override
    public Position getPosition() {
        return position;
    }


    public void moveRandomly(Map<Position, List<Position>> neighbors) {
        List<Position> possibleMoves = neighbors.get(position);

        // Vérifications
        if (possibleMoves == null || possibleMoves.isEmpty()) {
            return;
        }

        // Choisir un voisin aléatoire
        int randomIndex = random.nextInt(possibleMoves.size());
        Position newPosition = possibleMoves.get(randomIndex);

        if (newPosition != null) {
            this.position = newPosition;
        }
    }


    public void extinguishFire(Fire fire) {
        if (fire != null) {
            fire.extinguish(position);
        }
    }

    @Override
    public void update(FirefighterBoard board) {
        // Se déplacer aléatoirement
        moveRandomly(board.getNeighbors());

        // Éteindre les feux
        extinguishFire(board.getFire());
    }
}