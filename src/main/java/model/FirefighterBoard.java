package model;

import util.Position;
import util.TargetStrategy;

import java.util.*;


public class FirefighterBoard implements Board<List<ModelElement>> {
  private final int columnCount;
  private final int rowCount;
  private final int initialFireCount; //NOMBRE DE FEU
  private final int initialFirefighterCount; //NOMBRE DE POMPIERS
  /*private final TargetStrategy targetStrategy = new TargetStrategy();
  private List<Position> firefighterPositions;
  private Set<Position> firePositions;
  */

  private List<Firefighter> firefighters;//UNE LISTE CONTIENT DES OBJETS FIREFIGHTER
  private List<Fire> fires;//UNE LISTE CONTIENT DES OBJETS FIRE

  private Map<Position, List<Position>> neighbors = new HashMap();//DICTIONNAIRE
  private final Position[][] positions; //TABLEAU à 2 DIMENSIONS
  private int step = 0; //COMPTEUR DE TOURS
  private final Random randomGenerator = new Random(); //GéNéRER DES NOMBRES ALéATOIRES (POMPIERS et FEUX)

  //CONSTRUCTEUR
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

  //place les feux et pompiers
  public void initializeElements() {
    firefighters = new ArrayList<>();
    fires = new ArrayList<>();
    for (int index = 0; index < initialFireCount; index++)
      fires.add(new Fire(randomPosition()));
    for (int index = 0; index < initialFirefighterCount; index++)
      firefighters.add(new Firefighter(randomPosition()));
  }

  private Position randomPosition() {
    return new Position(randomGenerator.nextInt(rowCount), randomGenerator.nextInt(columnCount));
  }


  @Override
  public List<ModelElement> getState(Position position) {
    List<ModelElement> result = new ArrayList<>();

    // Vérifier si un pompier est à cette position
    for (Firefighter firefighter : firefighters)
      if (firefighter.getPosition().equals(position))
        result.add(ModelElement.FIREFIGHTER);

    for (Fire fire : fires)
      if (fire.getPosition().equals(position))
        result.add(ModelElement.FIRE);
    return result;

  }

  // retourne le nombre de lignes
  @Override
  public int rowCount() {
    return rowCount;
  }

  // retourne le nombre de colonnes
  @Override
  public int columnCount() {
    return columnCount;
  }

  //fait avancer la simulation d'un tour
  public List<Position> updateToNextGeneration() {
    List<Position> modifiedPositions = new ArrayList<>();
    modifiedPositions.addAll(updateFirefighters());
    modifiedPositions.addAll(updateFires());
    step++;
    return modifiedPositions;
  }

  // gère les actions des pompiers
  private List<Position> updateFirefighters() {
    List<Position> modifiedPositions = new ArrayList<>();
    Set<Position> firePositions= getFirePositions();
    for(Firefighter firefighter:firefighters){
      Position oldPosition= firefighter.getPosition();
      firefighter.moveTowardsFire(firePositions,neighbors);
      Position newPosition = firefighter.getPosition();

      List<Position> extinguishedPositions = firefighter.extinguishNearbyFires(neighbors,firePositions);
      fires.removeIf(fire->extinguishedPositions.contains(fire.getPosition()));

      modifiedPositions.add(oldPosition);
      modifiedPositions.add(newPosition);
      modifiedPositions.addAll(extinguishedPositions);
    }
    return modifiedPositions;

  }

  // gère la propagation du feu
  private List<Position> updateFires(){
    List<Position> modifiedPositions = new ArrayList<>();
    if(step % 2 == 0) {
      List<Fire> newFires = new ArrayList<>();

      for (Fire fire : fires) {
        List<Fire> spreadFires = fire.spread(neighbors);
        newFires.addAll(spreadFires);

      }
      fires.addAll(newFires);
      for(Fire fire : fires)
        modifiedPositions.add(fire.getPosition());
    }
    return modifiedPositions;
  }

  //retourne le numéro du tour actuel
  @Override
  public int stepNumber() {
    return step;
  }


  // remet tout à zéro
  @Override
  public void reset() {
    step = 0;
    initializeElements();
  }


  // placer/retirer des éléments à une position
  @Override
  public void setState(List<ModelElement> state, Position position) {
    fires.removeIf(fire -> fire.getPosition().equals(position));
    firefighters.removeIf(firefighter -> firefighter.getPosition().equals(position));

    for (ModelElement element : state) {
      switch (element) {
        case FIRE -> fires.add(new Fire(position));
        case FIREFIGHTER -> firefighters.add(new Firefighter(position));
      }
    }
  }

  //convertit la liste de Fire en Set de Position
  Set<Position> getFirePositions() {
    Set<Position> positions = new HashSet<>();
    for (Fire fire : fires) {
      positions.add(fire.getPosition());
    }
    return positions;
  }

  public Map<Position, List<Position>> getNeighbors() {
    return neighbors;
  }

  public void addNewFires(List<Fire> newFires) {
    fires.addAll(newFires);
  }

  public void removeFires(List<Position> positions) {
    fires.removeIf(f -> positions.contains(f.getPosition()));
  }


}