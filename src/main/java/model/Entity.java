package model;

import util.Position;

public abstract class Entity {
    protected Position position;

    public Entity(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    // Méthode abstraite : chaque entité se met à jour différemment
    public abstract void update(FirefighterBoard board);
}