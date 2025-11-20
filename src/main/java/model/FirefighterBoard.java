package model;

import util.Position;

import java.util.*;


public class FirefighterBoard implements Board<List<ModelElement>> {
  private final int columnCount;
  private final int rowCount;
  private final int initialFireCount;
  private final int initialFirefighterCount;
  private  Fire fire;
  private  List<Firefighter> firefighters;


  private Map<Position, List<Position>> neighbors = new HashMap(); //voisin de chaque case
  private final Position[][] positions;
  private int step = 0;
  private final Random randomGenerator = new Random();

  //créer la grille de jeux et initialise les positions
  public FirefighterBoard(int columnCount, int rowCount, int initialFireCount, int initialFirefighterCount) {
    this.columnCount = columnCount;
    this.rowCount = rowCount;
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
    this.initialFireCount = initialFireCount;
    this.initialFirefighterCount = initialFirefighterCount;
    initializeElements();
  }

  //  Initialise les feux et les pompiers de départ
  public void initializeElements() {
    // Générer les feux aléatoires
    Set<Position> initialFires = new HashSet<>();
    for (int i = 0; i < initialFireCount; i++) {
      initialFires.add(randomPosition());
    }

    // Créer l’objet Fire avec ces positions et les voisins
    fire = new Fire(initialFires, neighbors);

    // Générer les pompiers aléatoires
    firefighters = new ArrayList<>();
    for (int i = 0; i < initialFirefighterCount; i++) {
      firefighters.add(new Firefighter(randomPosition()));
    }
  }


  private Position randomPosition() {
    return new Position(randomGenerator.nextInt(rowCount), randomGenerator.nextInt(columnCount));
  }

  //regard ya quoi sur chaque position donnée
  @Override
  public List<ModelElement> getState(Position position) {
    List<ModelElement> result = new ArrayList<>();
    for (Firefighter firefighter : firefighters)
      if (firefighter.getPosition().equals(position))
        result.add(ModelElement.FIREFIGHTER);
    if (fire.isOnFire(position))
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
    List<Position> modifiedPositions = new  ArrayList<>();
    // Chaque pompier agit
    for (Firefighter f : firefighters) {
      Position oldPosition = f.getPosition();

      // nouvelle position après déplacement vers le feu le plus proche
      Position newPosition = f.move(fire.getPositions(), neighbors);

      // on ajoute ces positions modifiées (utile pour actualiser la grille)
      modifiedPositions.add(oldPosition);
      modifiedPositions.add(newPosition);

      //Le pompier éteint le feu sur sa case et autour
      modifiedPositions.addAll(f.extinguish(fire, neighbors));
    }

    // Le feu se propage (un tour sur deux)
    modifiedPositions.addAll(fire.spread(step));

    //On passe au tour suivant
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
    initializeElements();
  }

  @Override
  public void setState(List<ModelElement> state, Position position) {
    // Supprimer les anciens éléments sur cette position
    fire.extinguish(position); // retire le feu s’il y en a un
    firefighters.removeIf(f -> f.getPosition().equals(position)); // retire le pompier s’il y en a un

    //Ajouter les nouveaux éléments
    for (ModelElement element : state) {
      switch (element) {
        case FIRE -> fire.getPositions().add(position);
        case FIREFIGHTER -> firefighters.add(new Firefighter(position));
      }
    }
  }
}