package model;

import util.Position;
import util.TargetStrategy;

import java.util.*;


public class FirefighterBoard implements Board<List<ModelElement>> {
  static  int columnCount;
  static  int rowCount;
  private final int initialFireCount;
  private final int initialFirefighterCount;
  private final int initialCloudCount;
  private final int initialMotorizedFireFighterCount;
  private final int initialMountainCount;
  private final int initialRoadCount;
  private final int initialRockeryCount;

  private int step = 0;
  static Random randomGenerator = new Random();
  public static HashMap<Position, ArrayList<BoardElement>> elementPosition=new HashMap<>();

  public FirefighterBoard(int columnCount, int rowCount, int initialFireCount, int initialFirefighterCount, int initialFireCount1, int initialFirefighterCount1, int initialCloudCount, int initialMotorizedFireFighterCount, int initialMountainCount, int initialRoadCount, int initialRockeryCount) {
    this.columnCount = columnCount;
    this.rowCount = rowCount;
    this.initialFireCount = initialFireCount1;
    this.initialFirefighterCount = initialFirefighterCount1;
    this.initialCloudCount = initialCloudCount;
    this.initialMotorizedFireFighterCount = initialMotorizedFireFighterCount;
    this.initialMountainCount = initialMountainCount;
    this.initialRoadCount = initialRoadCount;
    this.initialRockeryCount = initialRockeryCount;
  }

  public void initializeElements() {
   /* firefighterPositions = new ArrayList<>();
    firePositions = new HashSet<>();
    for (int index = 0; index < initialFireCount; index++)
      firePositions.add(randomPosition());
    for (int index = 0; index < initialFirefighterCount; index++)
      firefighterPositions.add(randomPosition());*/
  }

  public static Position randomPosition() {
    return new Position(randomGenerator.nextInt(rowCount), randomGenerator.nextInt(columnCount));
  }



  /*public List<ModelElement> getState(Position position) {
    List<ModelElement> result = new ArrayList<>();
    for (Position firefighterPosition : firefighterPositions)
      if (firefighterPosition.equals(position))
        result.add(ModelElement.FIREFIGHTER);
    if (firePositions.contains(position))
      result.add(ModelElement.FIRE);
    return result;
  }*/


  public int rowCount() {
    return rowCount;
  }


  public int columnCount() {
    return columnCount;
  }


  @Override
  public int stepNumber() {
    return step;
  }


  @Override
  public void reset() {
    step = 0;
    initializeElements();
  }



  public static List<Position> neighbors(Position position) {
    List<Position> list = new ArrayList<>();
    if (position.row() > 0) list.add(new Position(position.row() - 1, position.column()));
    if (position.column() > 0) list.add(new Position(position.row(), position.column() - 1));
    if (position.row() < rowCount - 1) list.add(new Position(position.row() + 1, position.column()));
    if (position.column() < columnCount - 1) list.add(new Position(position.row(), position.column() + 1));
    return list;
  }

  @Override
  public List<ModelElement> getState(Position position) {
    return List.of();
  }

  @Override
  public void setState(List<ModelElement> state, Position position) {

  }

  @Override
  public List<Position> updateToNextGeneration() {
    return List.of();
  }


}