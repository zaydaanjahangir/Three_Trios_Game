package provider.model;

import java.util.List;

/**
 * Behaviors for a game of ThreeTriosGame.
 * These methods are mutable and have side effects.
 * The goal of the game is to fill all the card cells
 * on the grid and be the player with the most cards
 * on the grid.
 */
public interface GameModel extends ReadOnlyThreeTriosModel, ModelFeaturesInterface {

  /**
   * Initializes the game of a ThreeTriosGame.
   * @param rows integer number of rows
   * @param cols integer number of columns
   * @param deck a {@code List<Card>} representing the deck players draw from.
   * @throws IllegalStateException    if the game has already been started
   * @throws IllegalStateException    if the game is over
   * @throws IllegalArgumentException if the number of rows and columns are less than
   *                                  the number of CardCells.
   * @throws IllegalArgumentException if the number of card cells is not odd.
   * @throws IllegalArgumentException if size of deck is not at least the number of card cells
   *                                  plus one
   */
  void startGame(String grid, int rows, int cols, List<GameCard> deck);

  /**
   * Plays a card to the grid at the designated row and column.
   * SIDE EFFECT: Combo effect should be invoked when a player successfully places a card on grid.
   * @throws IllegalStateException    if the game has not started
   * @throws IllegalStateException    if the game is over
   * @throws IllegalStateException    if a player attempts to play twice
   * @throws IllegalArgumentException if row or col is out of bounds
   * @throws IllegalArgumentException if the cardInHandIdx is less than 0 or greater than
   *                                  or equal to the hand size.
   * @throws IllegalArgumentException if the coordinate is a HoleCell
   * @throws IllegalArgumentException if the coordinate is already occupied
   */
  void playCardToGrid(int cardInHandIdx, int row, int col);

  /**
   * Draws a card into the player's hand.
   * @param player representing which player to draw the hand to
   * @throws IllegalStateException if the game has not started
   * @throws IllegalStateException if the game is over
   */
  void drawForHand(Player player);

  /**
   * Removes that card from the player's hand.
   * @param player the player whose hand should be removed from
   * @param card the card to remove
   */
  void removeCardFromHand(Player player, GameCard card);

}