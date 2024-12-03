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
  private Card testCard1;

  @Before
  public void setUp() {
    aiPlayer = new AIPlayer("Blue", new FlipMaxStrategy());
    Player opponentPlayer = new PlayerImpl("Red");
    mockModel = new MockGameModel(aiPlayer, opponentPlayer);
    testCard1 = new StandardCard("TestCard1", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);
    Card testCard2 = new StandardCard("TestCard1", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);
    aiPlayer.addCardToHand(testCard1);
    aiPlayer.addCardToHand(testCard2);
  }

  @Test
  public void testAIPlayerMakesMove() {
    mockModel.setIsLegalMoveResult(true);
    mockModel.setPotentialFlipsResult(2);
    aiPlayer.takeTurn(mockModel);
    String expectedPlaceCardCall = "placeCard: Blue, TestCard1, (0,0)";
    assertTrue(mockModel.methodCalls.contains(expectedPlaceCardCall));
  }

  @Test
  public void testAIPlayerNoValidMoves() {
    mockModel.setIsLegalMoveResult(false); // makes moves are illegal
    aiPlayer.takeTurn(mockModel);
    assertTrue(mockModel.methodCalls.contains("isLegalMove"));
    assertTrue(mockModel.methodCalls.contains("placeCard"));
    // parenthesis only included in a successful place
    assertFalse(mockModel.methodCalls.contains("("));
  }

  @Test
  public void testAIPlayerSelectsHighestFlipMove() {
    mockModel.setIsLegalMoveResult(true);
    mockModel.setPotentialFlipsResult(3);
    mockModel.addPotentialFlipsResult(5);
    aiPlayer.takeTurn(mockModel);
    assertTrue(mockModel.methodCalls.contains("placeCard: Blue, TestCard2, (0,0)"));
  }

  @Test
  public void testAIPlayerHandlesEmptyHand() {
    aiPlayer.getHand().clear();
    aiPlayer.takeTurn(mockModel);
    assertFalse(mockModel.methodCalls.contains("placeCard"));
    assertTrue(mockModel.methodCalls.contains("getHand"));
  }

  @Test
  public void testAIPlayerHandlesIllegalMoves() {
    mockModel.setIsLegalMoveResult(false);
    aiPlayer.takeTurn(mockModel);
    assertFalse(mockModel.methodCalls.contains("placeCard"));
    assertTrue(mockModel.methodCalls.contains("isLegalMove"));
  }

  @Test
  public void testAIPlayerHandlesNoFlippableMoves() {
    mockModel.setIsLegalMoveResult(true);
    mockModel.setPotentialFlipsResult(0);
    aiPlayer.takeTurn(mockModel);
    assertTrue(mockModel.methodCalls.contains("placeCard"));
  }

  @Test
  public void testAIPlayerHandlesTieBreaking() {
    mockModel.setIsLegalMoveResult(true);
    mockModel.setPotentialFlipsResult(3);
    mockModel.addPotentialFlipsResult(3);
    aiPlayer.takeTurn(mockModel);
    assertTrue(mockModel.methodCalls.contains("placeCard: Blue, TestCard1, (0,0)"));
  }

  @Test
  public void testAIPlayerRemovesCardFromHandAfterMove() {
    mockModel.setIsLegalMoveResult(true);
    mockModel.setPotentialFlipsResult(2);
    aiPlayer.takeTurn(mockModel);
    assertFalse(aiPlayer.getHand().contains(testCard1));
  }
}
