package model;

import util.Position;
import java.util.ArrayList;
import java.util.List;

public class BoardUpdater {
    private final ElementManager elementManager;
    private final FirefighterBoard firefighterBoard;

    public BoardUpdater(ElementManager elementManager, FirefighterBoard firefighterBoard) {
        this.elementManager = elementManager;
        this.firefighterBoard = firefighterBoard;
    }

    public List<Position> updateToNextGeneration() {
        List<Position> modifiedPositions = updateFirefighters();
        modifiedPositions.addAll(updateFires());
        modifiedPositions.addAll(updateClouds());

        for (Cloud cloud : elementManager.getClouds()) {
            Position cloudPosition = cloud.getPosition();
            if (elementManager.getState(cloudPosition).contains(ModelElement.FIRE)) {
                cloud.extinguishFire(elementManager.getFire());
                modifiedPositions.add(cloudPosition);
            }
        }
        firefighterBoard.incrementStep();
        return modifiedPositions;
    }

    private List<Position> updateFires() {
        List<Position> modifiedPositions = new ArrayList<>();
        if (firefighterBoard.stepNumber() % 2 == 0) {
            List<Position> newFirePositions = new ArrayList<>();
            for (Position fire : elementManager.getFirePositions()) {
                for (Position neighbor : elementManager.getNeighbors().get(fire)) {
                    // Vérifie que la position voisine n'est pas occupée par un nuage,
                    // qu'elle n'est pas une montagne, et qu'elle n'est pas une route
                    if (!elementManager.getClouds().stream().anyMatch(cloud -> cloud.getPosition().equals(neighbor))
                            && !elementManager.getMountainPositions().contains(neighbor)
                            && !elementManager.getRoadPositions().contains(neighbor)) {
                        if (elementManager.getRockyPositions().contains(neighbor)) {
                            if (!elementManager.getRockyFireDelays().containsKey(neighbor)) {
                                elementManager.getRockyFireDelays().put(neighbor, 4);
                            } else {
                                int remainingDelay = elementManager.getRockyFireDelays().get(neighbor);
                                if (remainingDelay > 1) {
                                    elementManager.getRockyFireDelays().put(neighbor, remainingDelay - 1);
                                } else {
                                    // Si le délai est écoulé, le feu se propage
                                    newFirePositions.add(neighbor);
                                    elementManager.getRockyFireDelays().remove(neighbor);
                                }
                            }
                        } else {
                            newFirePositions.add(neighbor);
                        }
                    }
                }
            }
            elementManager.getFirePositions().addAll(newFirePositions);
            modifiedPositions.addAll(newFirePositions);
        }

        return modifiedPositions;
    }

    private List<Position> updateFirefighters() {
        List<Position> modifiedPositions = new ArrayList<>();
        List<Position> firefighterNewPositions = new ArrayList<>();

        for (Firefighter firefighter : elementManager.getFirefighters()) {
            Position originalPosition = firefighter.getPosition();
            Position newFirefighterPosition = originalPosition;

            if (firefighter instanceof MotorizedFireFighter) {
                Position firstMove = elementManager.getTargetStrategy().neighborClosestToFire(newFirefighterPosition, elementManager.getFirePositions(), elementManager.getNeighbors());
                if (firstMove != null && !elementManager.getMountainPositions().contains(firstMove)) {
                    if (elementManager.getRoadPositions().contains(firstMove)) {
                        newFirefighterPosition = firstMove;
                    } else {
                        newFirefighterPosition = firstMove;
                    }
                }

                Position secondMove = elementManager.getTargetStrategy().neighborClosestToFire(newFirefighterPosition, elementManager.getFirePositions(), elementManager.getNeighbors());
                if (secondMove != null && !elementManager.getMountainPositions().contains(secondMove)) {
                    if (elementManager.getRoadPositions().contains(secondMove)) {
                        newFirefighterPosition = secondMove;
                    } else {
                        newFirefighterPosition = secondMove;
                    }
                }
            } else {
                Position neighbor = elementManager.getTargetStrategy().neighborClosestToFire(newFirefighterPosition, elementManager.getFirePositions(), elementManager.getNeighbors());
                if (neighbor != null && !elementManager.getMountainPositions().contains(neighbor)) {
                    if (elementManager.getRoadPositions().contains(neighbor)) {
                        newFirefighterPosition = neighbor;
                    } else {
                        newFirefighterPosition = neighbor;
                    }
                }
            }

            firefighter.setPosition(newFirefighterPosition);
            firefighterNewPositions.add(newFirefighterPosition);

            extinguish(newFirefighterPosition);
            List<Position> neighborFirePositions = elementManager.getNeighbors().get(newFirefighterPosition).stream()
                    .filter(elementManager.getFirePositions()::contains).toList();
            for (Position firePosition : neighborFirePositions) {
                extinguish(firePosition);
            }

            modifiedPositions.add(originalPosition);
            modifiedPositions.add(newFirefighterPosition);
            modifiedPositions.addAll(neighborFirePositions);
        }

        elementManager.setFirefighterPositions(firefighterNewPositions);
        return modifiedPositions;
    }

    private void extinguish(Position position) {
        elementManager.getFirePositions().remove(position);
    }

    private List<Position> updateClouds() {
        List<Position> modifiedPositions = new ArrayList<>();
        for (Cloud cloud : elementManager.getClouds()) {
            Position oldPosition = cloud.getPosition();
            cloud.move(elementManager.getNeighbors());
            Position newPosition = cloud.getPosition();

            if (!oldPosition.equals(newPosition)) {
                elementManager.getFire().extinguish(oldPosition);
                modifiedPositions.add(oldPosition);

                elementManager.getFire().extinguish(newPosition);
                modifiedPositions.add(newPosition);
            }
        }
        return modifiedPositions;
    }
}
