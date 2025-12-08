package view;

import javafx.scene.paint.Color;

public enum ViewElement {
  FIREFIGHTER(Color.BLUE), FIRE(Color.RED), EMPTY(Color.WHITE),CLOUD(Color.GRAY),MOTORIZEDFIREFIGHTER(Color.YELLOW),NORMALCELL(Color.BEIGE),MOUNTAIN(Color.GREEN),ROAD(Color.BLACK),ROCK(Color.SADDLEBROWN);
  final Color color;
  ViewElement(Color color) {
    this.color = color;
  }
}
