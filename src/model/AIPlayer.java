package model;

import java.util.ArrayList;
import java.util.List;

import strategy.MoveStrategy;

/**
 * Represents an AI player in the Three Trios game.
 */
public class AIPlayer implements Player {
  private final String color;
  private final List<Card> hand;
  private final MoveStrategy strategy;

  /**
   * AI player constructor which creates an artificial player with a set strategy.
   *
   * @param color    color of the player
   * @param strategy chosen strategy which the player abides to
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
   * Makes a move using the assigned strategy.
   *
   * @param model the game model
   */
  public void makeMove(GameModel model) {
    Move move = strategy.determineMove(model, this);
    if (move != null && hand.contains(move.getCard())) {
      model.placeCard(this, move.getCard(), move.getRow(), move.getCol());
    } else {
      // Handle no valid move scenario
      System.out.println("No move was made");
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
