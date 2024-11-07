package model;

/**
 * Interface representing a card in the game with attack values on four sides.
 * Provides a method to compare attack values against an opponent's card.
 */
public interface Card {

  /**
   * Compares this card's attack value with an opponent's card in a given direction.
   *
   * @param opponent  the opponent's Card to compare against.
   * @param direction the Direction to use for the comparison.
   * @return true if this card's attack value is greater, false otherwise.
   */
  boolean compareAgainst(Card opponent, Direction direction);

  String getName();

  Value getNorthValue();

  Value getSouthValue();

  Value getEastValue();

  Value getWestValue();
}
