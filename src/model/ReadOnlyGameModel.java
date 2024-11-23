package model;

import java.util.List;

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

  // New Methods added in Assignment 6

  /**
   * Gets the number of rows in the grid.
   *
   * @return the number of rows.
   */
  int getGridRows();

  /**
   * Gets the number of columns in a grid.
   *
   * @return the number of columns
   */
  int getGridCols();

  /**
   * Gets the cell at a given row,col pair.
   *
   * @param row row index
   * @param col column index
   * @return Cell
   */
  Cell getCellAt(int row, int col);

  /**
   * Gets the current hand of the player.
   *
   * @param player the player you want to see the hand of.
   * @return A list of cards representing the player's hand
   */
  List<Card> getPlayerHand(Player player);

  /**
   * Gets the card owner at a row,col pair.
   *
   * @param row row index
   * @param col col index
   * @return the Player who owns the card at that row,col cell
   */
  Player getCardOwnerAt(int row, int col);

  /**
   * Determines if the move the player wants to make is legal by the rules.
   *
   * @param player Player playing the move
   * @param row    row index of the move.
   * @param col    col index of the move.
   * @return true if move is legal, false otherwise.
   */
  boolean isLegalMove(Player player, int row, int col);

  /**
   * Returns the number of potential immediate flips if a card is placed at that location.
   *
   * @param player Player playing the card.
   * @param card   The card to be played.
   * @param row    row index
   * @param col    col index
   * @return the number of flips that would occur (not combo step) if that card is placed.
   */
  int getPotentialFlips(Player player, Card card, int row, int col);

  /**
   * Gets the player's current score.
   *
   * @param player the player who's score you want to see
   * @return the player's score
   */
  int getPlayerScore(Player player);


}
