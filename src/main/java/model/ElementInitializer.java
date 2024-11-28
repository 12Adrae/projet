package model;

import util.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class ElementInitializer {
    private final ElementManager elementManager;
    private final model.TargetStrategy targetStrategy = new model.TargetStrategy();
    private final FirefighterBoard firefighterBoard;
    private final int initialFireCount;
    private final int initialFirefighterCount;
    private final Random randomGenerator = new Random();

    public ElementInitializer(ElementManager elementManager, int initialFireCount, int initialFirefighterCount, FirefighterBoard firefighterBoard) {
        this.elementManager = elementManager;
        this.initialFireCount = initialFireCount;
        this.firefighterBoard = firefighterBoard;
        this.initialFirefighterCount = initialFirefighterCount;
    }

    public void initializeElements() {
        elementManager.setClouds(new ArrayList<>());
        elementManager.setFirefighterPositions(new ArrayList<>());
        elementManager.setFirePositions(new HashSet<>());
        elementManager.setMountainPositions(new HashSet<>());
        elementManager.setRoadPositions(new HashSet<>());
        elementManager.setRockyPositions(new HashSet<>());

        while (elementManager.getMountainPositions().size() < 10) {
            Position newPosition = randomPosition();
            elementManager.getMountainPositions().add(newPosition);
            System.out.println("Mountain Position: " + newPosition);
        }

        for (int i = 0; i < 10; i++) {
            elementManager.getClouds().add(new Cloud(randomPosition()));
        }

        for (int index = 0; index < initialFireCount * 10; index++)
            elementManager.getFirePositions().add(randomPosition());
        elementManager.setFire(new Fire(elementManager.getFirePositions()));

        int motorizedCount = initialFirefighterCount / 3;

        for (int i = 0; i < motorizedCount; i++) {
            MotorizedFireFighter motorizedFireFighter = new MotorizedFireFighter(randomPosition(), targetStrategy, elementManager.getMountainPositions(), elementManager.getRoadPositions());
            elementManager.getFirefighters().add(motorizedFireFighter);
            elementManager.getFirefighterPositions().add(motorizedFireFighter.getPosition());
        }

        for (int i = 0; i < initialFirefighterCount - motorizedCount; i++) {
            Firefighter firefighter = new Firefighter(randomPosition(), targetStrategy, elementManager.getMountainPositions(), elementManager.getRoadPositions());
            elementManager.getFirefighters().add(firefighter);
            elementManager.getFirefighterPositions().add(firefighter.getPosition());
        }

        while (elementManager.getRoadPositions().size() < 10) {
            Position newPosition = randomPosition();
            elementManager.getRoadPositions().add(newPosition);
            System.out.println("Road Position: " + newPosition);
        }

        while (elementManager.getRockyPositions().size() < 10) {
            Position newPosition = randomPosition();
            elementManager.getRockyPositions().add(newPosition);
            System.out.println("Rocky Position: " + newPosition);
        }
    }

    private Position randomPosition() {
        return new Position(randomGenerator.nextInt(firefighterBoard.rowCount()), randomGenerator.nextInt(firefighterBoard.columnCount()));
    }
}
