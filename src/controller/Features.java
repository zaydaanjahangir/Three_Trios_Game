package controller;

import model.Card;

/**
 * Interface for features supported by the game, used by the view to communicate
 * with the controller.
 */
public interface Features {
  /**
   * Handle the event when a card is selected.
   *
   * @param card the selected card
   */
  void cardSelected(Card card);

  /**
   * Handle the event when a cell is selected.
   *
   * @param row the row index of the selected cell
   * @param col the column index of the selected cell
   */
  void cellSelected(int row, int col);
}
