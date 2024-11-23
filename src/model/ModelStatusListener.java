package model;

/**
 * Listener for receiving updates about changes in the game state.
 */
public interface ModelStatusListener {

  /**
   * Notifies when the active player's turn changes.
   *
   * @param currentPlayer the new active player
   */
  void turnChanged(Player currentPlayer);

  /**
   * Notifies when the game is over.
   *
   * @param result a message indicating the game's result
   */
  void gameOver(String result);
}

