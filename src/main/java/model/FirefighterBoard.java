package model;

import util.Position;

import java.util.*;


public class FirefighterBoard implements Board<List<ModelElement>> {
    private final int columnCount;
    private final int rowCount;
    private final int initialFireCount;
    private final int initialFirefighterCount;
    private final model.TargetStrategy targetStrategy = new model.TargetStrategy();
    private List<Position> firefighterPositions;
    private List<Firefighter> firefighters;
    private Set<Position> firePositions;
    private Map<Position, List<Position>> neighbors = new HashMap();
    private final Position[][] positions;
    private int step = 0;
    private final Random randomGenerator = new Random();
    private List<Cloud> clouds;

    private Fire fire;

    private Set<Position> mountainPositions;


    public FirefighterBoard(int columnCount, int rowCount, int initialFireCount, int initialFirefighterCount) {
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
        this.initialFireCount = initialFireCount;
        this.initialFirefighterCount = initialFirefighterCount;
        this.mountainPositions = new HashSet<>();

        initializeElements();
    }

    public void initializeElements() {
        clouds = new ArrayList<>();
        firefighterPositions = new ArrayList<>();
        firePositions = new HashSet<>();
        mountainPositions = new HashSet<>();

        while (mountainPositions.size() < 10) {
            Position newPosition = randomPosition();
            mountainPositions.add(newPosition);
            System.out.println("Mountain Position: " + newPosition);  // Affiche la position générée
        }


        for (int i = 0; i < 50; i++) {
            clouds.add(new Cloud(randomPosition()));
        }

        for (int index = 0; index < initialFireCount * 20; index++)
            firePositions.add(randomPosition());
        fire = new Fire(firePositions);

        int motorizedCount = initialFirefighterCount / 2;

        for (int i = 0; i < motorizedCount; i++) {
            MotorizedFireFighter motorizedFireFighter = new MotorizedFireFighter(randomPosition(), targetStrategy, mountainPositions);
            firefighters.add(motorizedFireFighter);
            firefighterPositions.add(motorizedFireFighter.getPosition());
        }

        // Initialisation des pompiers réguliers
        for (int i = 0; i < initialFirefighterCount - motorizedCount; i++) {
            Firefighter firefighter = new Firefighter(randomPosition(), targetStrategy, mountainPositions);
            firefighters.add(firefighter);
            firefighterPositions.add(firefighter.getPosition());
        }
    }

    private Position randomPosition() {
        return new Position(randomGenerator.nextInt(rowCount), randomGenerator.nextInt(columnCount));
    }

    @Override
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
            List<Position> newFirePositions = new ArrayList<>();
            for (Position fire : firePositions) {
                //newFirePositions.addAll(neighbors.get(fire));
                for (Position neighbor : neighbors.get(fire)) {
                    if (!clouds.stream().anyMatch(cloud -> cloud.getPosition().equals(neighbor)) && !mountainPositions.contains(neighbor)) {
                        newFirePositions.add(neighbor);
                    }
                }
            }
            firePositions.addAll(newFirePositions);
            modifiedPositions.addAll(newFirePositions);
        }
        return modifiedPositions;

    }

    @Override
    public int stepNumber() {
        return step;
    }

    private List<Position> updateFirefighters() {
        List<Position> modifiedPositions = new ArrayList<>();
        List<Position> firefighterNewPositions = new ArrayList<>();

        for (Firefighter firefighter : firefighters) {
            Position originalPosition = firefighter.getPosition();
            Position newFirefighterPosition = originalPosition;

            // Déplacer le pompier vers le feu le plus proche
            if (firefighter instanceof MotorizedFireFighter) {
                // Premier mouvement
                Position firstMove = targetStrategy.neighborClosestToFire(newFirefighterPosition, firePositions, neighbors);
                if (firstMove != null && !mountainPositions.contains(firstMove)) {
                    newFirefighterPosition = firstMove;
                }

                // Deuxième mouvement
                Position secondMove = targetStrategy.neighborClosestToFire(newFirefighterPosition, firePositions, neighbors);
                if (secondMove != null && !mountainPositions.contains(secondMove)) {
                    newFirefighterPosition = secondMove;
                }
            } else {
                newFirefighterPosition = targetStrategy.neighborClosestToFire(newFirefighterPosition, firePositions, neighbors);
            }

            // Mettre à jour la position du pompier
            firefighter.setPosition(newFirefighterPosition);
            firefighterNewPositions.add(newFirefighterPosition);

            // Éteindre les feux autour de la nouvelle position
            extinguish(newFirefighterPosition);
            List<Position> neighborFirePositions = neighbors.get(newFirefighterPosition).stream()
                    .filter(firePositions::contains).toList();
            for (Position firePosition : neighborFirePositions) {
                extinguish(firePosition);
            }

            // Ajouter les positions modifiées à la liste
            modifiedPositions.add(originalPosition);
            modifiedPositions.add(newFirefighterPosition);
            modifiedPositions.addAll(neighborFirePositions);
        }

        firefighterPositions = firefighterNewPositions;
        return modifiedPositions;
    }

    @Override
    public void reset() {
        step = 0;
        initializeElements();
    }

    private void extinguish(Position position) {
        firePositions.remove(position);
    }


    @Override
    public void setState(List<ModelElement> state, Position position) {
        firePositions.remove(position);
        firefighterPositions.remove(position);
        for (ModelElement element : state) {
            switch (element) {
                case FIRE -> firePositions.add(position);
                case FIREFIGHTER -> {
                    Firefighter firefighter = new Firefighter(position, targetStrategy,  mountainPositions);
                    firefighters.add(firefighter);
                    firefighterPositions.add(position);
                }
                case MOTORIZED_FIREFIGHTER -> {
                    MotorizedFireFighter motorizedFireFighter = new MotorizedFireFighter(position, targetStrategy, mountainPositions);
                    firefighters.add(motorizedFireFighter);
                    firefighterPositions.add(position);
                }
                case MOUNTAIN -> mountainPositions.add(position);
            }
        }
    }

    private List<Position> updateClouds (Map < Position, List < Position >> neighbors){
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
