package adapters;

import java.awt.Color;

import model.Card;
import model.Player;
import model.PlayerImpl;
import provider.model.Direction;
import provider.model.GameCard;

/**
 * Adapter class to adapt the providers' GameCard into our StandardCard.
 */
public class GameCardAdapter implements GameCard {
  private final Card card;
  private Player owner;

  /**
   * GameCard Adapter constructor.
   *
   * @param card  card to be adapted
   * @param owner player
   */
  public GameCardAdapter(Card card, Player owner) {
    this.card = card;
    this.owner = owner;
  }

  @Override
  public int north() {
    return card.getNorthValue().getIntValue();
  }

  @Override
  public int south() {
    return card.getSouthValue().getIntValue();
  }

  @Override
  public int east() {
    return card.getEastValue().getIntValue();
  }

  @Override
  public int west() {
    return card.getWestValue().getIntValue();
  }

  @Override
  public void combo() {
    // Implement combo logic using your model's methods
    // You might need to interact with the game model to perform the combo
  }

  @Override
  public Color color() {
    String playerColor = owner.getColor();
    return playerColor.equals("Red") ? Color.RED : Color.BLUE;
  }

  @Override
  public void addNeighbors(Direction d, GameCard c) {
    // Implement if your model supports neighbors
    // Otherwise, you might need to simulate this functionality
  }

  @Override
  public void changeOwnership(Color color) {
    // Update the owner based on the color
    String newOwnerColor = color.equals(Color.RED) ? "Red" : "Blue";
    this.owner = new PlayerImpl(newOwnerColor);
    // Update the card's owner in your model if necessary
  }

  @Override
  public String toString() {
    return card.getName();
  }

  @Override
  public String cardToString() {
    return color().equals(Color.RED) ? "R" : "B";
  }

  @Override
  public GameCard returnCard() {
    // Return a copy of this GameCardAdapter
    return new GameCardAdapter(card, owner);
  }
}
