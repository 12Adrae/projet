package model;

import util.Position;
import java.util.Map;
import java.util.Random;


public class TerrainFactory {
    public static Terrain createRandomTerrain(Position p,
                                              Map<Position, Terrain> terrainMap,
                                              int rowCount,
                                              int columnCount,
                                              Random random) {
        return new NormalCell();
    }

    public static Terrain createMountain() {
        return new Mountain();
    }

    public static Terrain createRoad() {
        return new Road();
    }

    public static Terrain createRock() {
        return new Rock();
    }

    public static Terrain createNormal() {
        return new NormalCell();
    }
}

