package provider.model;


import java.awt.Color;

/**
 * Interface representing a GameCard in ThreeTriosGame, where each GameCard has
 * integer numbers in the Directions of the game, north, south, east, and west.
 * GameCards also have neighbors, and a color to represent the ownership.
 */
public interface GameCard extends Card {
  /**
   * Returns this card's North number.
   *
   * @return the north integer number of this card
   */
  int north();

  /**
   * Returns this card's South number.
   *
   * @return the south integer number of this card
   */
  int south();

  /**
   * Returns this card's East number.
   *
   * @return the east integer number of this card
   */
  int east();

  /**
   * Returns this card's West number.
   *
   * @return the west integer number of this card
   */
  int west();

  /**
   * Compares this card to its neighbors to achieve the combo effect.
   * Compare each number to the neighbor card in that direction's opposite direction.
   * If this card wins, it should flip the neighbor card and continue calling this method until
   * there are no more neighboring cards left. When none of its neighbors can be flipped,
   * this method should stop looking.
   */
  void combo();

  /**
   * Returns the color of this card, representing its current ownership.
   *
   * @return the color of this card
   */
  Color color();

  /**
   * Add the passed in card to this card's neighbors in the direction that was passed in.
   *
   * @param d representing the direction of the neighbors
   * @param c representing the card that should be added to this card's neighbors
   */
  void addNeighbors(Direction d, GameCard c);

  /**
   * Flips the color of this card to the given color.
   *
   * @param color a color to switch the card's color to
   */
  void changeOwnership(Color color);

  /**
   * Returns a string representation of this card, providing details of this GameCard.
   *
   * @return a string representing this GameCard
   */
  String toString();

  /**
   * Returns a string representing of this card, specifically displaying its owner, "R" or "B".
   *
   * @return a string describing the card's owner
   */
  String cardToString();

  /**
   * Returns a copy of this card. Mutating this card will not affect the game.
   *
   * @return GameCard a copy of this card
   */
  GameCard returnCard();
}
