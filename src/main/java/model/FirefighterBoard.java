package model;

import util.Position;
import java.util.*;

public class FirefighterBoard implements Board<List<ModelElement>> {

  private final int columnCount;
  private final int rowCount;
  private final int initialFireCount;
  private final int initialFirefighterCount;

  private List<Element> allElements; // UNE SEULE liste pour TOUS
  private Fire fire; //  Référence pour accès rapide

  private Map<Position, List<Position>> neighbors = new HashMap();
  private Map<Position, Cell> terrain = new HashMap();
  private final Position[][] positions;
  private int step = 0;
  private final Random randomGenerator = new Random();

  public FirefighterBoard(int columnCount, int rowCount, int initialFireCount, int initialFirefighterCount) {
    this.columnCount = columnCount;
    this.rowCount = rowCount;
    this.positions = new Position[rowCount][columnCount];
    this.allElements = new ArrayList<>();

    // Initialiser les positions
    for (int column = 0; column < columnCount; column++)
      for (int row = 0; row < rowCount; row++)
        positions[row][column] = new Position(row, column);

    // Initialiser les voisins
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
    initializeTerrain();
    initializeElements();
  }

  private void initializeTerrain() {
    // terrain normal
    for (int col = 0; col < columnCount; col++) {
      for (int row = 0; row < rowCount; row++) {
        Position pos = positions[row][col];
        terrain.put(pos, new NormalCell(pos));
      }
    }

    // Ajout montagnes / routes / rocailles

    for (int i = 0; i < (rowCount * columnCount) / 10; i++) {
      Position pos = randomPosition();
      terrain.put(pos, new Mountain(pos));
    }

    for (int i = 0; i < (rowCount * columnCount) / 20; i++) {
      Position pos = randomPosition();
      terrain.put(pos, new Road(pos));
    }

    for (int i = 0; i < (rowCount * columnCount) / 20; i++) {
      Position pos = randomPosition();
      terrain.put(pos, new RockyCell(pos));

    }
  }


  private void initializeElements() {
    allElements.clear();
    // Créer le feu
    Set<Position> initialFires = new HashSet<>();
    for (int i = 0; i < initialFireCount; i++) {
      initialFires.add(randomPosition());
    }
    fire = new Fire(initialFires, neighbors,terrain);
    allElements.add(fire);

    // Créer les pompiers
    for (int i = 0; i < initialFirefighterCount; i++) {
      Firefighter ff = new Firefighter(randomPosition());
      allElements.add(ff);
    }

    // Créer les nuages
    int cloudCount = (rowCount * columnCount) / 30;
    ;
    for (int i = 0; i < cloudCount; i++) {
      Cloud cloud = new Cloud(randomPosition());
      allElements.add(cloud);
    }

    // Créer les pompiers motorisés
    int motorizedCount = 10;
    for (int i = 0; i < motorizedCount; i++) {
      MotorizedFirefighter mf = new MotorizedFirefighter(randomPosition());
      allElements.add(mf);
    }


  }

  private Position randomPosition() {
    int r = randomGenerator.nextInt(rowCount);
    int c = randomGenerator.nextInt(columnCount);
    return positions[r][c];   // <-- IMPORTANT
  }

  /*private Position randomPosition() {
    return new Position(randomGenerator.nextInt(rowCount), randomGenerator.nextInt(columnCount));
  }*/

  @Override
  public List<ModelElement> getState(Position position) {
    List<ModelElement> result = new ArrayList<>();

    for (Element e : allElements) {
      if (e.getPosition() != null && e.getPosition().equals(position)) {
        if (e instanceof Firefighter) result.add(ModelElement.FIREFIGHTER);
        if (e instanceof MotorizedFirefighter) result.add(ModelElement.MOTORIZEDFIREFIGHTER);
        if (e instanceof Cloud) result.add(ModelElement.CLOUD);

      }
    }

    Cell cell = terrain.get(position);
    if (cell != null) {
      if (cell instanceof NormalCell) result.add(ModelElement.NORMALCELL);
      else if (cell instanceof Mountain) result.add(ModelElement.MOUNTAIN);
      else if (cell instanceof Road) result.add(ModelElement.ROAD);
      else if (cell instanceof RockyCell) result.add(ModelElement.ROCKYCELL);
    }

    if (fire.isOnFire(position)) {
      result.add(ModelElement.FIRE);
    }

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


  @Override
  public List<Position> updateToNextGeneration() {
    List<Position> modifiedPositions = new ArrayList<>();

    for (Element element : allElements) {
      Position oldPos = element.getPosition();
      element.update(this);
      Position newPos = element.getPosition();

      if (oldPos != null) modifiedPositions.add(oldPos);
      if (newPos != null) modifiedPositions.add(newPos);
    }

    step++;
    return modifiedPositions;
  }

  @Override
  public int stepNumber() {
    return step;
  }

  @Override
  public void reset() {
    step = 0;
    allElements.clear();
    initializeElements();
  }

  @Override
  public void setState(List<ModelElement> state, Position position) {
    //Effacer l'ancienne case
    fire.extinguish(position);
    allElements.removeIf(e -> e instanceof Firefighter && e.getPosition().equals(position));

    for (ModelElement element : state) {
      switch (element) {
        case FIRE -> {
          if (fire != null) {
            fire.getPositions().add(position);
          }
        }
        case FIREFIGHTER -> {
          Firefighter ff = new Firefighter(position);
          allElements.add(ff);
        }
        case CLOUD -> {
          allElements.add(new Cloud(position));
        }
        case MOTORIZEDFIREFIGHTER -> {
          MotorizedFirefighter mf = new MotorizedFirefighter(position);
          allElements.add(mf);
        }
        case NORMALCELL ->{
          NormalCell nc=new NormalCell(position);
          allElements.add((Element) nc);
        }
        case ROAD -> {
          Road r=new Road(position);
          allElements.add((Element) r);
        }
        case MOUNTAIN -> {
          Mountain m=new Mountain(position);
          allElements.add((Element) m);
        }
        case ROCKYCELL -> {
          RockyCell r=new RockyCell(position);
          allElements.add((Element) r);
        }

      }
    }
  }


  public Fire getFire() {
    return fire;
  }

  public Map<Position, List<Position>> getNeighbors() {
    return neighbors;
  }

  public Map<Position, Cell> getTerrain() {
    return terrain;
  }
}