package model;

import util.Position;

import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * La classe Cloud représente un nuage dans la simulation.
 * Elle est responsable de :
 *
 * - Gérer la position du nuage dans la simulation.
 * - Déplacer le nuage vers une position voisine de manière aléatoire.
 * - Permettre au nuage d'éteindre un feu à sa position actuelle.
 *
 * Cette classe est utilisée pour simuler les déplacements et les interactions des nuages
 * avec les feux dans le modèle.
 *
 * Méthodes principales :
 * - Position getPosition() : retourne la position actuelle du nuage.
 * - void move(Map<Position, List<Position>> neighbors) : déplace le nuage vers une position voisine aléatoire.
 * - void extinguishFire(Fire fire) : éteint le feu à la position actuelle du nuage.
 */

public class Cloud {
    private Position position;
    private final Random random = new Random();

    public Cloud(Position initialPosition) {
        this.position = initialPosition;
    }

    public Position getPosition() {
        return position;
    }

    public void move(Map<Position, List<Position>> neighbors) {
        List<Position> possiblePositions = neighbors.get(position);
        if (!possiblePositions.isEmpty()) {
            position = possiblePositions.get(random.nextInt(possiblePositions.size()));
        }
    }

    public void moveTo(Position target) {
        this.position = target;
    }


    public void extinguishFire(Fire fire) {
        fire.extinguish(position);
    }
}
