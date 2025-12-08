package model;

public class Rock implements Terrain{
    @Override
    public boolean fireCanCross() {
        return true; //le feu peut passer, mais retardé
    }
    @Override
    public boolean firefighterCanCross() {
        return true;
    }
    @Override
    public int fireDelay() {
        return 4; //retard de 4 tours
    }
    @Override
    public ModelElement getType() {
        return ModelElement.ROCK;
    }
}
