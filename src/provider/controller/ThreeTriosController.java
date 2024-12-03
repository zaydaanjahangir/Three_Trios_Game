package provider.controller;

import java.awt.event.ActionEvent;

import provider.model.GameCard;
import provider.model.GameModel;

/**
 * Controller interface representing behaviors for the controller in a three trios game.
 * It should be able to play the game once a card and cell have been selected, handle
 * cell clicks, handle when actions have been performed, and handle card selection.
 */
public interface ThreeTriosController {
  /**
   * Execute a single game of Three Trios given a Three Trios Model. When the game is over,
   * the playGame method ends.
   *
   * @param model a non-null Three Trios game Model
   */
  void playGame(GameModel model);

  /**
   * Handle an action in a single cell of the board, such as to make a move.
   *
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   */
  void handleCellClick(int row, int col);

  /**
   * Informs controller that an action was performed.
   * @param e the ActionEvent performed.
   */
  void actionPerformed(ActionEvent e);

  /**
   * Informs controller that a card was selected.
   * @param card the GameCard that was selected.
   */
  void cardSelected(GameCard card, int cardInHandIdx);

}
