package provider.model;

/**
 * Interface describing behaviors of implementations that inherit the ThreeTriosEventListener
 * interface. They should be able to be informed that the turn was changed,
 */
public interface ThreeTriosEventListener {
  /**
   * This gets notified that the turn changed.
   * @param currentPlayer the player whose turn it is.
   */
  void turnChanged(Player currentPlayer);

  /**
   * This gets notified that the game is over and that is the winner.
   * @param winner the player that won.
   */
  void gameOver(Player winner);

  /**
   * This gets notified that the hand of this player updated.
   * @param player the player's hand to update.
   */
  void handUpdated(Player player);

  /**
   * This gets notified that this passed in card was placed at the row and column.
   * @param row int representing the row
   * @param col int representing the column
   * @param card GameCard representing the card that was placed
   */
  void cardPlaced(int row, int col, GameCard card);

  /**
   * This gets notified that there was an invalid move with the given string.
   * @param message the message in string.
   */
  void invalidMove(String message);
}