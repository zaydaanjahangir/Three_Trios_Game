package provider.model;

import java.util.List;

/**
 * Represents the model for ThreeTrios that is read only. It does not have any side effects.
 */
public interface ReadOnlyThreeTriosModel {
  /**
   * Determines if the game is over.
   * Has no side effects.
   * @return true if the game is over and false if it is not.
   * @throws IllegalStateException if the game has not started
   */
  boolean isGameOver();

  /**
   * Determines the winner of the game.
   * Has no side effects.
   * @return the winning player if the game is over
   * @throws IllegalStateException if the game has not started
   * @throws IllegalStateException if the game is not over
   * {@link cs3500.threetrios.model.hw05.TieBreakerException}
   * @throws Exception throws a TieBreakerException if players are tied.
   */
  Player findWinner() throws Exception;

  /**
   * Returns a string of the cell at this row and column of this model's grid.
   * Has no side effects.
   * @return String representation of the cell at the given row and column
   */
  String getCellToString(int row, int col);

  /**
   * Returns the string representation of the current player's hand.
   * Has no side effects.
   * @return a string representation of the current player's hand.
   */
  String handToString();

  /**
   * Returns the current turn.
   * Has no side effects.
   * @throws IllegalStateException if the game has not started
   * @throws IllegalStateException if the game is over
   */
  Player currentTurn();

  /**
   * Returns the number of rows on the grid.
   * Has no side effects.
   * @return an integer number representing the number of rows on the grid.
   */
  int numRows();

  /**
   * Returns the number of columns on the grid.
   * Has no side effects.
   * @return an integer number representing the number of columns on the grid.
   */
  int numColumns();


  // Updated methods

  /**
   * Returns a deep-copy of the grid.
   * Mutating the returned object will have no effect on
   * the state of the game nor will it mutate the internal state of the game.
   * @return TTGrid representing the current state of the grid in a deep-copy
   */
  Grid returnGridCopy();

  /**
   * Returns a copy of the cell at the given grid coordinates.
   * Mutating the returned object will have no effect on the state of the game
   * nor will it mutate the internal state of the game.
   * @return ACell representing the current state of the grid in a deep-copy
   */
  Cell returnCell(int row, int col);


  /**
   * Returns a deep-copy of the passed in PlayerColor in a list of GameCards.
   * Mutating the returned object will have no effect on the state of the game
   * nor will it mutate the internal state of the game.
   */
  List<GameCard> returnPlayerHand(PlayerColor color);

  /**
   * Returns the score of the passed in PlayerColor.
   * @param color the PlayerColor representing a player whose score should be calculated
   * @return an integer number representing the score of the player
   */
  int calculateScore(PlayerColor color);

  /**
   * Determines how many cards could be flipped if the given player chooses to place this
   * card on the grid at the specified row and column.
   * @param card a GameCard that is chosen to be placed at the specified coordinate
   * @param row integer number representing the row of the coordinate, 0-index based
   * @param col integer number representing the column of the coordinate, 0-index based
   * @param color the color representing the player that is placing a card at the cell
   * @return an integer number count of how many cards can be flipped.
   * @throws IllegalArgumentException if row or col is out of bounds of the grid
   * @throws IllegalArgumentException if the card is null
   * @throws IllegalArgumentException if the coordinate is a HoleCell
   * @throws IllegalArgumentException if the coordinate is already occupied
   */
  int howManyCardsCanBeFlipped(GameCard card, int row, int col, PlayerColor color);


}
