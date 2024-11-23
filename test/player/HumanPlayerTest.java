package player;

import org.junit.Test;

import java.util.List;

import model.Card;
import model.PlayerImpl;
import model.StandardCard;
import model.Value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class HumanPlayerTest {


  @Test
  public void testPlayerImplHandManagement() {
    PlayerImpl player = new PlayerImpl("Red");

    Card card1 = new StandardCard("Card1", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);
    Card card2 = new StandardCard("Card1", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);

    // Add cards to the player's hand
    player.addCardToHand(card1);
    player.addCardToHand(card2);

    // Verify that the hand contains the cards
    List<Card> hand = player.getHand();
    assertTrue(hand.contains(card1));
    assertTrue(hand.contains(card2));

    // Remove a card from the hand
    player.removeCardFromHand(card1);

    // Verify that the card is removed
    hand = player.getHand();
    assertFalse(hand.contains(card1));
    assertTrue(hand.contains(card2));
  }

  @Test
  public void testPlayerEquality() {
    PlayerImpl player1 = new PlayerImpl("Red");
    PlayerImpl player2 = new PlayerImpl("Red");
    PlayerImpl player3 = new PlayerImpl("Blue");

    // Verify that players with the same color are considered equal
    assertEquals(player1, player2);
    assertEquals(player1.hashCode(), player2.hashCode());

    // Verify that players with different colors are not equal
    assertNotEquals(player1, player3);
  }


}
