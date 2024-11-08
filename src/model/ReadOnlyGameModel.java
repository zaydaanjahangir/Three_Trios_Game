package model;

/**
 * Read-only interface for the Three Trios game model.
 * Contains observation methods to inspect the state of the game without modification.
 */
public interface ReadOnlyGameModel {
  /**
   * Checks if the game is over. The game ends when all playable cells on the grid are filled.
   *
   * @return true if the game is over, false otherwise.
   */
  boolean isGameOver();

  /**
   * Determines the winner of the game based on the number of owned cards.
   * This method calculates the final scores for both players and returns a string
   * indicating the winner. If the game ends in a tie, it returns a corresponding message.
   *
   * @return a String indicating the winner or if the game is a tie.
   */
  String getWinner();

  /**
   * Retrieves the current state of the game grid.
   *
   * @return the current Grid object representing the game's grid.
   */
  Grid getGrid();

  /**
   * Gets the current player whose turn it is to make a move.
   *
   * @return the Player object representing the current player.
   */
  Player getCurrentPlayer();

  /**
   * Gets the opposing player to the current player.
   *
   * @return The Player representing the opposing player from the current player
   */
  Player getOpponentPlayer();

}
