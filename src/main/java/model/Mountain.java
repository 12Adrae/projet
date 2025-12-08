package model;

public class Mountain implements Terrain{
    @Override
    public boolean fireCanCross() {
        return false;
    }

    @Override
    public boolean firefighterCanCross() {
        return false;
    }

    @Override
    public int fireDelay() {
        //Le feu NE se propage jamais
        return Integer.MAX_VALUE;
    }
    @Override
    public ModelElement getType() {
        return ModelElement.MOUNTAIN;
    }

}
