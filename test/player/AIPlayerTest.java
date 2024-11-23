package player;

import org.junit.Before;
import org.junit.Test;

import model.AIPlayer;
import model.Card;
import model.Player;
import model.PlayerImpl;
import model.StandardCard;
import model.Value;
import strategy.FlipMaxStrategy;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test class for AIPlayer.
 */
public class AIPlayerTest {
  private MockGameModel mockModel;
  private AIPlayer aiPlayer;
  private Player opponentPlayer;
  private Card testCard1;
  private Card testCard2;

  @Before
  public void setUp() {
    // Create players
    aiPlayer = new AIPlayer("Blue", new FlipMaxStrategy());
    opponentPlayer = new PlayerImpl("Red");

    // Set up mock model
    mockModel = new MockGameModel(aiPlayer, opponentPlayer);

    // Create test cards
    testCard1 = new StandardCard("TestCard1", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);
    testCard2 = new StandardCard("TestCard1", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);

    // Add cards to AI player's hand
    aiPlayer.addCardToHand(testCard1);
    aiPlayer.addCardToHand(testCard2);
  }

  @Test
  public void testAIPlayerMakesMove() {
    // Set up the mock responses
    mockModel.setIsLegalMoveResult(true);
    mockModel.setPotentialFlipsResult(2);

    // AIPlayer takes its turn
    aiPlayer.takeTurn(mockModel);

    // Verify that AIPlayer called the necessary methods on the model
    assertTrue(mockModel.methodCalls.contains("getCurrentPlayer"));

    // Verify that AIPlayer attempted to find legal moves
    assertTrue(mockModel.methodCalls.stream().anyMatch(s -> s.startsWith("isLegalMove")));

    // Verify that AIPlayer evaluated potential flips
    assertTrue(mockModel.methodCalls.stream().anyMatch(s -> s.startsWith("getPotentialFlips")));

    // Verify that AIPlayer placed a card
    assertTrue(mockModel.methodCalls.stream().anyMatch(s -> s.startsWith("placeCard")));

    // Optionally, verify the specific move made by the AIPlayer
    String expectedPlaceCardCall = "placeCard: Blue, TestCard1, (0,0)";
    assertTrue(mockModel.methodCalls.contains(expectedPlaceCardCall));
  }

  @Test
  public void testAIPlayerNoValidMoves() {
    // Set up the mock responses
    mockModel.setIsLegalMoveResult(false); // All moves are illegal

    // AIPlayer takes its turn
    aiPlayer.takeTurn(mockModel);

    // Verify that AIPlayer called isLegalMove but did not place a card
    assertTrue(mockModel.methodCalls.stream().anyMatch(s -> s.startsWith("isLegalMove")));
    assertFalse(mockModel.methodCalls.stream().anyMatch(s -> s.startsWith("placeCard")));

    // Optionally, check for any error handling (e.g., logs or exceptions)
    // Since our AIPlayer prints an error message, you can redirect System.err and check the output
  }

  @Test
  public void testAIPlayerChoosesMaxFlipMove() {

  }
  @Test
  public void testAIPlayerWithCornerStrategy(){

  }

}
