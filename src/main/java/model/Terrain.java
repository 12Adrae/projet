package model;

public interface Terrain {
    boolean fireCanCross();
    boolean firefighterCanCross();
    int fireDelay();
    ModelElement getType();
}
