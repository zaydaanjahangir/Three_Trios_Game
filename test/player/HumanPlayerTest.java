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

/**
 * Player test for humans in which the user controls the moves and card placement.
 */
public class HumanPlayerTest {


  @Test
  public void testPlayerImplHandManagement() {
    PlayerImpl player = new PlayerImpl("Red");

    Card card1 = new StandardCard("Card1", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);
    Card card2 = new StandardCard("Card1", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);

    player.addCardToHand(card1);
    player.addCardToHand(card2);

    List<Card> hand = player.getHand();
    assertTrue(hand.contains(card1));
    assertTrue(hand.contains(card2));
    player.removeCardFromHand(card1);
    hand = player.getHand();
    assertFalse(hand.contains(card1));
    assertTrue(hand.contains(card2));
  }

  @Test
  public void testPlayerEquality() {
    PlayerImpl player1 = new PlayerImpl("Red");
    PlayerImpl player2 = new PlayerImpl("Red");
    PlayerImpl player3 = new PlayerImpl("Blue");
    assertEquals(player1, player2);
    assertEquals(player1.hashCode(), player2.hashCode());
    assertNotEquals(player1, player3);
  }

  @Test
  public void testPlayerColor() {
    PlayerImpl player = new PlayerImpl("Red");
    assertEquals("Red", player.getColor());
    PlayerImpl anotherPlayer = new PlayerImpl("Blue");
    assertEquals("Blue", anotherPlayer.getColor());
  }

  @Test
  public void testPlayerHandIsImmutable() {
    PlayerImpl player = new PlayerImpl("Red");
    Card card = new StandardCard("Card", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);
    player.addCardToHand(card);

    List<Card> hand = player.getHand();
    hand.clear();

    assertFalse(player.getHand().isEmpty());
  }

  @Test
  public void testPlayerAddDuplicateCard() {
    PlayerImpl player = new PlayerImpl("Red");
    Card card = new StandardCard("Card", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);

    player.addCardToHand(card);
    player.addCardToHand(card);

    List<Card> hand = player.getHand();
    assertEquals(2, hand.size());
  }

  @Test
  public void testPlayerRemoveNonexistentCard() {
    PlayerImpl player = new PlayerImpl("Red");
    Card card1 = new StandardCard("Card1", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);
    Card card2 = new StandardCard("Card2", Value.TWO, Value.THREE, Value.FOUR, Value.ONE);

    player.addCardToHand(card1);
    player.removeCardFromHand(card2);

    List<Card> hand = player.getHand();
    assertTrue(hand.contains(card1));
    assertFalse(hand.contains(card2));
  }


}
