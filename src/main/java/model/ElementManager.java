package model;

import util.Position;
import java.util.*;

public class ElementManager {
    private final int columnCount;
    private final int rowCount;
    private final model.TargetStrategy targetStrategy = new model.TargetStrategy();
    private final Map<Position, List<Position>> neighbors = new HashMap<>();
    private final Position[][] positions;
    private List<Position> firefighterPositions;
    private List<Firefighter> firefighters;
    private Set<Position> firePositions;
    private List<Cloud> clouds;
    private Fire fire;
    private Set<Position> mountainPositions;
    private Set<Position> roadPositions;
    private Set<Position> rockyPositions;
    private Map<Position, Integer> rockyFireDelays = new HashMap<>();

    public ElementManager(int columnCount, int rowCount) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        this.positions = new Position[rowCount][columnCount];
        this.firefighters = new ArrayList<>();
        for (int column = 0; column < columnCount; column++)
            for (int row = 0; row < rowCount; row++)
                positions[row][column] = new Position(row, column);
        for (int column = 0; column < columnCount; column++)
            for (int row = 0; row < rowCount; row++) {
                List<Position> list = new ArrayList<>();
                if (row > 0) list.add(positions[row - 1][column]);
                if (column > 0) list.add(positions[row][column - 1]);
                if (row < rowCount - 1) list.add(positions[row + 1][column]);
                if (column < columnCount - 1) list.add(positions[row][column + 1]);
                neighbors.put(positions[row][column], list);
            }
        this.mountainPositions = new HashSet<>();
        this.roadPositions = new HashSet<>();
        this.rockyPositions = new HashSet<>();
    }


    public List<ModelElement> getState(Position position) {
        List<ModelElement> result = new ArrayList<>();

        // Vérifie le type de chaque pompier
        for (Firefighter firefighter : firefighters) {
            if (firefighter.getPosition().equals(position)) {
                if (firefighter instanceof MotorizedFireFighter) {
                    result.add(ModelElement.MOTORIZED_FIREFIGHTER);
                } else {
                    result.add(ModelElement.FIREFIGHTER);
                }
            }
        }
        if (firePositions.contains(position)) {
            result.add(ModelElement.FIRE);
        }
        if (clouds.stream().anyMatch(cloud -> cloud.getPosition().equals(position))) {
            result.add(ModelElement.CLOUD);
        }

        if (mountainPositions.contains(position)) {
            result.add(ModelElement.MOUNTAIN);
        }

        if (roadPositions.contains(position)) {
            result.add(ModelElement.ROUTE);
        }

        if (rockyPositions.contains(position)) {
            result.add(ModelElement.ROCK);
        }


        return result;
    }

    public void setState(List<ModelElement> state, Position position) {
        firePositions.remove(position);
        firefighterPositions.remove(position);
        for (ModelElement element : state) {
            switch (element) {
                case FIRE -> firePositions.add(position);
                case FIREFIGHTER -> {
                    Firefighter firefighter = new Firefighter(position, targetStrategy, mountainPositions, roadPositions);
                    firefighters.add(firefighter);
                    firefighterPositions.add(position);
                }
                case MOTORIZED_FIREFIGHTER -> {
                    MotorizedFireFighter motorizedFireFighter = new MotorizedFireFighter(position, targetStrategy, mountainPositions, roadPositions);
                    firefighters.add(motorizedFireFighter);
                    firefighterPositions.add(position);
                }
                case MOUNTAIN -> mountainPositions.add(position);

                case ROUTE -> roadPositions.add(position);

                case ROCK -> rockyPositions.add(position);
            }
        }
    }

    public Map<Position, List<Position>> getNeighbors() {
        return neighbors;
    }

    public List<Firefighter> getFirefighters() {
        return firefighters;
    }

    public Set<Position> getFirePositions() {
        return firePositions;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public Fire getFire() {
        return fire;
    }

    public void setFire(Fire fire) {
        this.fire = fire;
    }

    public Set<Position> getMountainPositions() {
        return mountainPositions;
    }

    public Set<Position> getRoadPositions() {
        return roadPositions;
    }

    public Set<Position> getRockyPositions() {
        return rockyPositions;
    }

    public Map<Position, Integer> getRockyFireDelays() {
        return rockyFireDelays;
    }

    public List<Position> getFirefighterPositions() {
        return firefighterPositions;
    }

    public void setFirefighterPositions(List<Position> firefighterPositions) {
        this.firefighterPositions = firefighterPositions;
    }

    public model.TargetStrategy getTargetStrategy() {
        return targetStrategy;
    }


    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    public void setFirePositions(Set<Position> firePositions) {
        this.firePositions = firePositions;
    }

    public void setMountainPositions(Set<Position> mountainPositions) {
        this.mountainPositions = mountainPositions;
    }

    public void setRoadPositions(Set<Position> roadPositions) {
        this.roadPositions = roadPositions;
    }

    public void setRockyPositions(Set<Position> rockyPositions) {
        this.rockyPositions = rockyPositions;
    }
}
