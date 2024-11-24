package model;

import java.util.ArrayList;
import java.util.List;

import controller.Features;

/**
 * Represents a player in the Three Trios game.
 * Manages the player's hand and color.
 */
public class PlayerImpl implements Player, PlayerAction {
  private final String color;
  private final List<Card> hand;

  public PlayerImpl(String color) {
    this.color = color;
    this.hand = new ArrayList<>();
  }

  @Override
  public void addCardToHand(Card card) {
    hand.add(card);
  }

  @Override
  public void removeCardFromHand(Card card) {
    hand.remove(card);
  }

  @Override
  public List<Card> getHand() {
    return new ArrayList<>(hand);
  }

  @Override
  public String getColor() {
    return color;
  }

  /**
   * Set the Features interface for this player, allowing communication
   * with the controller.
   *
   * @param features the Features implementation from the controller
   */
  public void setFeatures(Features features) {
    // empty
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof Player) {
      Player other = (Player) obj;
      return this.getColor().equals(other.getColor());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return color.hashCode();
  }

  @Override
  public void takeTurn(GameModel model) {
    // empty
  }
}

