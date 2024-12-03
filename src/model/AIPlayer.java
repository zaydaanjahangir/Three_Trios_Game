package model;

import java.util.ArrayList;
import java.util.List;

import controller.Features;
import strategy.MoveStrategy;

/**
 * Class representing the AI player which makes moves based on the strategy given.
 */
public class AIPlayer implements Player, PlayerAction {
  private final String color;
  private final List<Card> hand;
  private final MoveStrategy strategy;

  /**
   * Constructor for the AIPlayer object.
   *
   * @param color    Color of the AIPLayer.
   * @param strategy strategy that the AIPLayer is using.
   */
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
    // empty
  }

  @Override
  public void takeTurn(GameModel model) {
    Move move = strategy.determineMove(model, this);
    if (move != null) {
      try {
        model.placeCard(this, move.getCard(), move.getRow(), move.getCol());
      } catch (IllegalArgumentException e) {
        System.out.println("AIPlayer made an invalid move: " + e.getMessage());
      } catch (IllegalStateException e) {
        System.out.println("AIPlayer encountered a game state error: " + e.getMessage());
      }
    } else {
      System.out.println("AIPlayer has no valid moves.");
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
