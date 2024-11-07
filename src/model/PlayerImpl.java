package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Three Trios game.
 * Manages the player's hand and color.
 */
public class PlayerImpl implements Player {
  private final String color; // "Red" or "Blue"
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

  public String getColor() {
    return color;
  }
}

