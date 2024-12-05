package adapters;

import java.util.ArrayList;
import java.util.List;


import model.StandardCard;
import model.Value;
import provider.model.GameCard;
import provider.model.PlayerColor;
import model.Card;

/**
 * Player adapter class to adapt the providers player to ours.
 */
public class PlayerAdapter implements provider.model.Player {
  private final model.Player player;

  /**
   * Construtor for the PlayerAdapter class.
   */
  public PlayerAdapter(model.Player player) {
    this.player = player;
  }

  @Override
  public PlayerColor color() {
    return player.getColor().equals("Red") ? PlayerColor.RED : PlayerColor.BLUE;
  }

  @Override
  public void addCardToHand(GameCard card) {
    Card adaptedCard = adaptGameCardToCard(card);
    player.addCardToHand(adaptedCard);
  }

  @Override
  public boolean validateCardInHandIdx(int cardInHandIdx) {
    return cardInHandIdx >= 0 && cardInHandIdx < player.getHand().size();
  }

  @Override
  public List<GameCard> retrieveHand() {
    List<Card> hand = player.getHand();
    List<GameCard> adaptedHand = new ArrayList<>();
    for (Card card : hand) {
      adaptedHand.add(new GameCardAdapter(card, player));
    }
    return adaptedHand;
  }

  @Override
  public StringBuilder handToString() {
    StringBuilder sb = new StringBuilder();
    for (Card card : player.getHand()) {
      sb.append(card.toString()).append("\n");
    }
    return sb;
  }

  @Override
  public GameCard playCardFromHand(int cardInHandIdx) {
    if (validateCardInHandIdx(cardInHandIdx)) {
      Card card = player.getHand().get(cardInHandIdx);
      player.removeCardFromHand(card);
      return new GameCardAdapter(card, player);
    } else {
      throw new IndexOutOfBoundsException("Invalid card index");
    }
  }

  @Override
  public List<GameCard> returnHand() {
    return retrieveHand();
  }

  @Override
  public void removeCardFromHand(GameCard card) {
    Card adaptedCard = adaptGameCardToCard(card);
    player.removeCardFromHand(adaptedCard);
  }

  private Card adaptGameCardToCard(provider.model.GameCard gameCard) {
    model.Value north = adaptValue(gameCard.north());
    model.Value south = adaptValue(gameCard.south());
    model.Value east = adaptValue(gameCard.east());
    model.Value west = adaptValue(gameCard.west());
    String name = gameCard.toString();
    return new StandardCard(name, north, south, east, west);
  }

  private model.Value adaptValue(int value) {
    switch (value) {
      case 1:
        return Value.ONE;
      case 2:
        return Value.TWO;
      case 3:
        return Value.THREE;
      case 4:
        return Value.FOUR;
      case 5:
        return Value.FIVE;
      case 6:
        return Value.SIX;
      case 7:
        return Value.SEVEN;
      case 8:
        return Value.EIGHT;
      case 9:
        return Value.NINE;
      case 10:
        return Value.A;
      default:
        throw new IllegalArgumentException("Invalid value");
    }
  }
}

