package view;

import controller.Features;

public interface ThreeTriosViewInterface {
  void addFeatures(Features features);
  void updateView();
  void showErrorMessage(String message);
  void showGameOverMessage(String message);
}
