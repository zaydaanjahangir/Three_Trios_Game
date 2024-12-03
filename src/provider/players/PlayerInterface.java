package provider.players;

import provider.model.GameCard;

/**
 * The interface for PlayerInterface, which can be a human or a machine. These behaviors
 * are what all Players must be able to do in a game of Three Trios.
 */
public interface PlayerInterface {

  // Features interface: player choosing a card to play, player choosing a position to play it to
  // controller has to wait for these in order to respond (machine-> gets it from machine player
  // when implementing a strategy). Human ->

  // Model should publish who's turn it is. Notify that its now "your turn"

  /**
   * Called when a player selects a card from their hand.
   * @param card the GameCard chosen
   */
  void selectedCard(GameCard card);

  /**
   * Called when a cell has been selected.
   * @param row the integer number representing the row
   * @param col the integer number representing the column
   */
  void selectedCell(int row, int col);
}
