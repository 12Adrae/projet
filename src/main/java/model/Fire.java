package model;

import util.Position;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * La classe Fire gère l'état et le comportement des feux dans la simulation.
 * Elle est responsable de :
 *
 * - Stocker les positions des feux actifs sur le plateau.
 * - Propager les feux vers les cases voisines selon les règles de la simulation.
 * - Éteindre les feux à des positions spécifiques, que ce soit individuellement ou en groupe.
 *
 * Cette classe est utilisée par FirefighterBoard pour gérer et mettre à jour
 * l'état des incendies. En séparant les fonctionnalités liées aux feux dans cette classe,
 * on simplifie la gestion de l'état du plateau et facilite les modifications futures de la logique de propagation
 * ou d'extinction des feux.
 *
 * Méthodes principales :
 * - void spread(Map<Position, List<Position>> neighbors) : propage les feux vers les cases voisines.
 * - void extinguish(Position position) : éteint le feu à une position spécifique.
 * - void extinguishAll(Set<Position> positions) : éteint les feux à plusieurs positions.
 */

public class Fire {
    private final Set<Position> firePositions = new HashSet<>();

    public Fire(Set<Position> initialFirePositions) {
        this.firePositions.addAll(initialFirePositions);
    }

    public Set<Position> getPositions() {
        return firePositions;
    }

    public void spread(Map<Position, List<Position>> neighbors) {
        Set<Position> newFirePositions = new HashSet<>();
        for (Position firePosition : firePositions) {
            if (!neighbors.containsKey(firePosition)) {}
            newFirePositions.addAll(neighbors.get(firePosition));
        }
        firePositions.addAll(newFirePositions); // Ajoute les nouvelles positions enflammées
    }

    public void extinguish(Position position) {
        firePositions.remove(position);
    }

    public void extinguishAll(Set<Position> positions) {
        firePositions.removeAll(positions);
    }
}
