package model;

import java.util.List;

/**
 * Interface representing a player in the game, managing the player's hand of cards.
 */
public interface Player {

  /**
   * Adds a card to the player's hand.
   *
   * @param card the Card to be added.
   */
  void addCardToHand(Card card);

  /**
   * Removes a card from the player's hand.
   *
   * @param card the Card to be removed.
   */
  void removeCardFromHand(Card card);

  /**
   * Retrieves the player's current hand of cards.
   *
   * @return a List of Cards in the player's hand.
   */
  List<Card> getHand();

  /**
   * Gets the color the current player is playing as.
   *
   * @return A string representation of the color
   */
  String getColor();
}
