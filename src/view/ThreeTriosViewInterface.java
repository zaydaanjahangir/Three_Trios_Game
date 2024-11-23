package view;

import controller.Features;

/**
 * Interface for the visual representation of the Three Trios game.
 */
public interface ThreeTriosViewInterface {

  /**
   * Adds the features interface for handling player interactions.
   *
   * @param features the features interface to be added
   */
  void addFeatures(Features features);

  /**
   * Updates the view to reflect the current game state.
   */
  void updateView();

  /**
   * Displays an error message to the player.
   *
   * @param message the error message to display
   */
  void showErrorMessage(String message);

  /**
   * Displays the game-over message to the player.
   *
   * @param message the game-over message to display
   */
  void showGameOverMessage(String message);

  /**
   * Sets the visibility of the view.
   *
   * @param visible true to make the view visible, false otherwise
   */
  void setVisible(boolean visible);
}

