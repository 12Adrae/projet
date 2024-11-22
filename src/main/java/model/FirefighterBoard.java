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
  private List<Cloud> clouds;

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
    clouds = new ArrayList<>();
    firefighters = new ArrayList<>();
    Set<Position> initialFirePositions = new HashSet<>();

    for (int index = 0; index < initialFireCount; index++)
      initialFirePositions.add(randomPosition());
    fire = new Fire(initialFirePositions);

    for (int index = 0; index < initialFirefighterCount; index++)
      firefighters.add(new Firefighter(randomPosition(), targetStrategy));

    for (int i = 0; i < 5; i++) {
      clouds.add(new Cloud(randomPosition()));
    }
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
    if (clouds.stream().anyMatch(cloud -> cloud.getPosition().equals(position))) {
      result.add(ModelElement.CLOUD);
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

  public List<Position> updateToNextGeneration() {
    List<Position> modifiedPositions = updateFirefighters();
    modifiedPositions.addAll(updateFires());
    modifiedPositions.addAll(updateClouds(neighbors));

// Parcourt chaque nuage dans la liste des nuages
// Pour chaque nuage, on vérifie s'il se trouve à une position où il y a un feu.
// Si un feu est présent à la position du nuage, ce dernier l'éteint en appelant la méthode extinguishFire.
// La position du nuage (où le feu a été éteint) est ajoutée à la liste des positions modifiées.
    for (Cloud cloud : clouds) {
      Position cloudPosition = cloud.getPosition();
      if (this.getState(cloudPosition).contains(ModelElement.FIRE)) {
        cloud.extinguishFire(fire);
        modifiedPositions.add(cloudPosition);
      }
    }

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

  /**
   * Met à jour la position de chaque nuage, éteint les feux aux anciennes et nouvelles positions,
   * et retourne une liste des positions modifiées.
   *
   * Pour chaque nuage, la méthode :
   * 1. Récupère sa position actuelle (ancienne position).
   * 2. Déplace le nuage vers une nouvelle position en fonction de ses voisins.
   * 3. Si la position du nuage change :
   *    - Éteint le feu à l'ancienne position du nuage.
   *    - Déplace le nuage à une nouvelle position et éteint le feu à cette nouvelle position.
   * 4. Ajoute l'ancienne et la nouvelle position à la liste des positions modifiées.
   *
   * @param neighbors La carte des voisins qui permet au nuage de se déplacer.
   * @return Une liste des positions où le feu a été éteint (ancienne et nouvelle position du nuage).
   */
  private List<Position> updateClouds(Map<Position, List<Position>> neighbors) {
    List<Position> modifiedPositions = new ArrayList<>();
    for (Cloud cloud : clouds) {
      Position oldPosition = cloud.getPosition();
      cloud.move(neighbors);
      Position newPosition = cloud.getPosition();

      if (!oldPosition.equals(newPosition)) {
        fire.extinguish(oldPosition);
        modifiedPositions.add(oldPosition);

        fire.extinguish(newPosition);
        modifiedPositions.add(newPosition);
      }
    }
    return modifiedPositions;
  }

}
