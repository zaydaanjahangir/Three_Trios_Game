package controller;

import java.util.ArrayList;
import java.util.List;

import view.ThreeTriosViewInterface;


/**
 * Mock implementation of the ThreeTriosViewInterface for testing purposes.
 */
public class MockThreeTriosView implements ThreeTriosViewInterface {
  // log of method calls
  public List<String> methodCalls = new ArrayList<>();

  @Override
  public void updateView() {
    methodCalls.add("updateView");
  }

  @Override
  public void addFeatures(Features features) {
    methodCalls.add("addFeatures");
  }

  @Override
  public void showErrorMessage(String message) {
    methodCalls.add("showErrorMessage: " + message);
  }

  @Override
  public void showGameOverMessage(String message) {
    methodCalls.add("showGameOverMessage: " + message);
  }

  @Override
  public void setVisible(boolean visible) {
    methodCalls.add("setVisible: " + visible);
  }
}
