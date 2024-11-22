package view;

import javafx.scene.paint.Color;

public enum ViewElement {
  FIREFIGHTER(Color.BLUE), FIRE(Color.RED), EMPTY(Color.WHITE), CLOUD(Color.GRAY);
  final Color color;
  ViewElement(Color color) {
    this.color = color;
  }
}
