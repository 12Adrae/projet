package model;

public class NormalCell implements Terrain{
    @Override
    public boolean fireCanCross() {
        return true;
    }

    @Override
    public boolean firefighterCanCross() {
        return true;
    }

    @Override
    public int fireDelay() {
        return 0;  //aucun retard
    }
    @Override
    public ModelElement getType() {
        return ModelElement.NORMALCELL;
    }

}
