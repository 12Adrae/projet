package model;
import util.Position;
import util.TargetStrategy;
import java.util.*;


public class FirefighterBoard implements Board<List<ModelElement>> {
  private final int columnCount;
  private final int rowCount;
  private final int initialFireCount;
  private final int initialFirefighterCount;
  private final int initialCloudCount;
  private final int initialMotorizedFireFightersCount;
  private final List<Element> elements = new ArrayList<>();
  private final Map<Position, Terrain> terrainMap = new HashMap<>();
  private final Map<Position, List<Position>> neighbors = new HashMap(); //voisin de chaque case
  private final Position[][] positions;
  private int step = 0;
  private final Random randomGenerator = new Random();

  //créer la grille et initialise les positions
  public FirefighterBoard(
          int columnCount,
          int rowCount,
          int initialFireCount,
          int initialFirefighterCount
  ) {
    this.columnCount = columnCount;
    this.rowCount = rowCount;
    this.initialFireCount = initialFireCount;
    this.initialFirefighterCount = initialFirefighterCount;
    this.initialCloudCount = 3;
    this.initialMotorizedFireFightersCount = 1;
    //creation matrice positions
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
    reset();
  }

  private Position randomPosition() {
    return new Position(
            randomGenerator.nextInt(rowCount),
            randomGenerator.nextInt(columnCount)
    );
  }

  @Override
  public List<ModelElement> getState(Position position) {
    List<ModelElement> result = new ArrayList<>();

    Terrain t = terrainMap.get(position);
    result.add(t.getType());

    for (Element e : elements) {
      if (e.getPositions().contains(position)) {
        result.add(e.getType());
      }
    }
    return result;
  }


  private void initTerrain() {
    terrainMap.clear();
    int mountainCount = (columnCount * rowCount) / 10;
    for (int i = 0; i < mountainCount; i++)
      terrainMap.put(randomPosition(), TerrainFactory.createMountain());
    int y = rowCount / 2;
    for (int col = 0; col < columnCount; col++) {
      Position p = randomPosition();
      if (!(terrainMap.get(p) instanceof Mountain))
        terrainMap.put(p, TerrainFactory.createRoad());
    }
    int rockCount = (columnCount * rowCount) / 20;
    for (int i = 0; i < rockCount; i++) {
      Position p = randomPosition();
      if (!(terrainMap.get(p) instanceof Mountain))
        terrainMap.put(p, TerrainFactory.createRock());
    }
    for (int r = 0; r < rowCount; r++)
      for (int c = 0; c < columnCount; c++)
        terrainMap.putIfAbsent(positions[r][c], TerrainFactory.createNormal());


  }


  private void initElements() {
    elements.clear();
    Set<Position> fires = new HashSet<>();
    for (int i = 0; i < initialFireCount; i++)
      fires.add(randomPosition());
    elements.add(new Fire(fires, neighbors));  // Multi-case (cas spécial)
    for (int i = 0; i < initialFirefighterCount; i++)
      elements.add(ElementFactory.create(ModelElement.FIREFIGHTER,
              randomPosition(), neighbors));
    for (int i = 0; i < initialCloudCount; i++)
      elements.add(ElementFactory.create(ModelElement.CLOUD,
              randomPosition(), neighbors));
    for (int i = 0; i < initialMotorizedFireFightersCount; i++)
      elements.add(ElementFactory.create(ModelElement.MOTORIZEDFIREFIGHTER,
              randomPosition(), neighbors));
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
    List<Position> modified = new ArrayList<>();
    for (Element e : elements) {
      modified.addAll(e.update(neighbors, this));
    }
    step++;
    return modified;
  }

  @Override
  public int stepNumber() {
    return step;
  }

  @Override
  public void reset() {
    step = 0;
    initTerrain();
    initElements();
  }


  @Override
  public void setState(List<ModelElement> state, Position position) {
    // On retire tous les éléments présents sur cette case
    elements.removeIf(e -> e.getPositions().contains(position));
    // On ajoute les nouveaux éléments via la factory
    for (ModelElement type : state) {
      elements.add(ElementFactory.create(type, position, neighbors));
    }
  }

  public Terrain getTerrain(Position p) {
    return terrainMap.get(p);
  }
  public List<Element> getElements() {
    return elements;
  }
}