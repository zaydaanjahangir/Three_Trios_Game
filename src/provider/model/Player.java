package provider.model;

import java.util.List;

/**
 * Represents the interface of a Player for a ThreeTriosGame. A Player has a hand
 * of cards, and can add to their hand, validate the bounds, and playing the cards.
 */
public interface Player {

  /**
   * Returns the PlayerColor associated with this player.
   * @return a PlayerColor
   */
  PlayerColor color();

  /**
   * Adds this card to this player's hand.
   * @param card the GameCard to be added to this player's hand
   */
  void addCardToHand(GameCard card);

  /**
   * Validates that this card index is a valid index in this player's hand.
   * @param cardInHandIdx the integer number representing the index in the hand, 0-based
   * @return a boolean, true if the index is a valid index
   */
  boolean validateCardInHandIdx(int cardInHandIdx);

  /**
   * Returns the hand of this player.
   * Modifying this list should not mutate the hand.
   * @return a list of cards that are in this player's hand
   */
  List<GameCard> retrieveHand();

  /**
   * Converts the Player's hand to its String format.
   * @return a StringBuilder containing a string format of the player's hand
   */
  StringBuilder handToString();

  /**
   * Given the index, returns the card and removes it from this player's hand.
   * @param cardInHandIdx an integer number representing the index in the hand, 0-based
   * @return the GameCard that was chosen from the hand at the given index
   */
  GameCard playCardFromHand(int cardInHandIdx);

  /**
   * Returns a string representation of the player and their details.
   * @return a string representation of the player
   */
  String toString();

  /**
   * Returns a copy of this Player's hand. Mutating this list will have no effect on the game.
   * @return {@code List<GameCard>} representing a copy of this player's hand
   */
  List<GameCard> returnHand();

  void removeCardFromHand(GameCard card);
}