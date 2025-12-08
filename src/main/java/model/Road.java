package model;

public class Road implements Terrain{
    @Override
    public boolean fireCanCross() {
        return false;
    }

    @Override
    public boolean firefighterCanCross() {
        return true;
    }

    @Override
    public int fireDelay() {
        return 0; // pas de propagation
    }
    @Override
    public ModelElement getType() {
        return ModelElement.ROAD;
    }
}
