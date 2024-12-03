package provider.view;

import provider.controller.ThreeTriosController;
import provider.model.GameCard;
import provider.model.Player;

/**
 * Displays the current game state as an interactive graphical interface that the player can
 * click on their card and place it to the designated location.
 */
public interface ThreeTriosGUIView {

  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh();

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible(boolean bool);

  /**
   * Set up the controller to handle click events in this view.
   *
   * @param listener the controller
   */
  public void addClickListener(ThreeTriosController listener);

  public HandPanel getLeftPanel();

  void displayGameOver(Player winner);

  void updatePlayerHand(Player player);

  void updateGrid(int row, int col, GameCard card);

  void reflectNewChanges();

  void showErrorMessage(String message);

  void setCanSelectGrid();

  void notYourTurn();

  void listenAgain();


}

