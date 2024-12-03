package controller;

import model.Card;


/**
 * Interface for controlling the game by handling player actions.
 */
public interface GameController {
  /**
   * Handles the event of a card being selected by a player.
   *
   * @param card the card selected by the player
   */
  void cardSelected(Card card);

  /**
   * Handles the event of a cell being selected on the grid by a player.
   *
   * @param row the row index of the selected cell
   * @param col the column index of the selected cell
   */
  void cellSelected(int row, int col);
}

