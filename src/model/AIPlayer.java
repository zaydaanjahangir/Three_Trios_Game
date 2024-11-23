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
  public void takeTurn(GameModel model) {
    Move move = strategy.determineMove(model, this);
    if (move != null) {
      try {
        model.placeCard(this, move.getCard(), move.getRow(), move.getCol());
        // Do not switch turn here; the controller manages turn switching
      } catch (Exception e) {
        System.err.println("AIPlayer encountered an error: " + e.getMessage());
      }
    } else {
      System.err.println("AIPlayer has no valid moves.");
      // Handle no valid moves if necessary
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
