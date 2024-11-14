package model;

import util.Position;

import java.util.*;

public class FirefighterBoard implements Board<List<ModelElement>> {
  private final int columnCount;
  private final int rowCount;
  private final int initialFireCount;
  private final int initialFirefighterCount;
  private final model.TargetStrategy targetStrategy = new model.TargetStrategy();
  private List<Firefighter> firefighters;
  private Fire fire;
  private Map<Position, List<Position>> neighbors = new HashMap<>();
  private final Position[][] positions;
  private int step = 0;
  private final Random randomGenerator = new Random();

  public FirefighterBoard(int columnCount, int rowCount, int initialFireCount, int initialFirefighterCount) {
    this.columnCount = columnCount;
    this.rowCount = rowCount;
    this.positions = new Position[rowCount][columnCount];
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
    this.initialFireCount = initialFireCount;
    this.initialFirefighterCount = initialFirefighterCount;
    initializeElements();
  }

  public void initializeElements() {
    firefighters = new ArrayList<>();
    Set<Position> initialFirePositions = new HashSet<>();
    for (int index = 0; index < initialFireCount; index++)
      initialFirePositions.add(randomPosition());
    fire = new Fire(initialFirePositions);
    for (int index = 0; index < initialFirefighterCount; index++)
      firefighters.add(new Firefighter(randomPosition(), targetStrategy));
  }

  private Position randomPosition() {
    return new Position(randomGenerator.nextInt(rowCount), randomGenerator.nextInt(columnCount));
  }

  @Override
  public List<ModelElement> getState(Position position) {
    List<ModelElement> result = new ArrayList<>();
    for (Firefighter firefighter : firefighters)
      if (firefighter.getPosition().equals(position))
        result.add(ModelElement.FIREFIGHTER);
    if (fire.getPositions().contains(position))
      result.add(ModelElement.FIRE);
    return result;
  }

  @Override
  public int rowCount() {
    return rowCount;
  }

  @Override
  public int columnCount() {
    return columnCount;
  }

  public List<Position> updateToNextGeneration() {
    List<Position> modifiedPositions = updateFirefighters();
    modifiedPositions.addAll(updateFires());
    step++;
    return modifiedPositions;
  }

  private List<Position> updateFires() {
    List<Position> modifiedPositions = new ArrayList<>();
    if (step % 2 == 0) {
      fire.spread(neighbors);
      modifiedPositions.addAll(fire.getPositions());
    }
    return modifiedPositions;
  }

  @Override
  public int stepNumber() {
    return step;
  }

  private List<Position> updateFirefighters() {
    List<Position> modifiedPositions = new ArrayList<>();
    for (Firefighter firefighter : firefighters) {
      firefighter.moveTowardsFire(fire.getPositions(), neighbors);
      firefighter.extinguishFiresAround(fire.getPositions(), neighbors);
      modifiedPositions.add(firefighter.getPosition());
    }
    return modifiedPositions;
  }

  @Override
  public void reset() {
    step = 0;
    initializeElements();
  }

  @Override
  public void setState(List<ModelElement> state, Position position) {
    fire.extinguish(position);
    for (Firefighter firefighter : firefighters) {
      if (firefighter.getPosition().equals(position)) {
        firefighters.remove(firefighter);
        break;
      }
    }
    for (ModelElement element : state) {
      switch (element) {
        case FIRE -> fire.getPositions().add(position);
        case FIREFIGHTER -> firefighters.add(new Firefighter(position, targetStrategy));
      }
    }
  }
}
