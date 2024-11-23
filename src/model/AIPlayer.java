package model;

import java.util.ArrayList;
import java.util.List;

import controller.Features;
import strategy.MoveStrategy;

public class AIPlayer implements Player, PlayerAction {
  private final String color;
  private final List<Card> hand;
  private final MoveStrategy strategy;
  private Features features;

  public AIPlayer(String color, MoveStrategy strategy) {
    this.color = color;
    this.hand = new ArrayList<>();
    this.strategy = strategy;
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
    this.features = features;
  }

  @Override
  public void takeTurn(ReadOnlyGameModel model) {
    // AI determines its move
    Move move = strategy.determineMove(model, this);
    if (move != null) {
      features.cardSelected(move.getCard());
      features.cellSelected(move.getRow(), move.getCol());
    }
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
}
