package controller;

import org.junit.Before;
import org.junit.Test;

import model.AIPlayer;
import model.Card;
import model.PlayerImpl;
import model.StandardCard;
import model.Value;
import strategy.FlipMaxStrategy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for ThreeTriosController using mock implementations.
 */
public class ThreeTriosControllerTest {
  private MockGameModel mockModel;
  private MockThreeTriosView mockViewRed;
  private MockThreeTriosView mockViewBlue;
  private PlayerImpl playerRed;
  private AIPlayer playerBlue;
  private ThreeTriosController controllerRed;
  private ThreeTriosController controllerBlue;

  @Before
  public void setUp() {
    mockModel = new MockGameModel();
    mockViewRed = new MockThreeTriosView();
    mockViewBlue = new MockThreeTriosView();

    playerRed = new PlayerImpl("Red");
    playerBlue = new AIPlayer("Blue", new FlipMaxStrategy());
    mockModel.setPlayers(playerRed, playerBlue);

    controllerRed = new ThreeTriosController(
            mockModel, playerRed, playerRed, mockViewRed, mockViewBlue, playerBlue);

    controllerBlue = new ThreeTriosController(
            mockModel, playerBlue, playerBlue, mockViewBlue, mockViewRed, playerRed);
  }

  /**
   * Test that selecting a card correctly sets the selected card.
   */
  @Test
  public void testCardSelectedSuccess() {
    Card testCard = new StandardCard("TestCard", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);
    playerRed.addCardToHand(testCard);
    controllerRed.cardSelected(testCard);
    assertFalse(mockViewRed.methodCalls.contains("showErrorMessage: It's not your turn!"));
    assertFalse(mockViewRed.methodCalls.contains("showErrorMessage: You don't have that card."));
  }

  /**
   * Test that selecting a card when it's not the player's turn shows an error.
   */
  @Test
  public void testCardSelectedOtherPLayerTurn() {
    mockModel.setCurrentPlayer(playerBlue);
    Card testCard = new StandardCard("TestCard", Value.ONE, Value.TWO, Value.THREE,
            Value.FOUR);
    playerRed.addCardToHand(testCard);
    controllerRed.cardSelected(testCard);
    assertTrue(mockViewRed.methodCalls.contains("showErrorMessage: It's not your turn!"));
  }

  /**
   * Test that selecting a card not in the player's hand shows an error.
   */
  @Test
  public void testCardSelectedWhenCardNotInHand() {
    Card testCard = new StandardCard("TestCard", Value.ONE, Value.TWO, Value.THREE,
            Value.FOUR);
    controllerRed.cardSelected(testCard);
    assertTrue(mockViewRed.methodCalls.contains("showErrorMessage: You don't have that card."));
  }

  /**
   * Test placing a card successfully.
   */
  @Test
  public void testSuccessfulCardPlaceAtSelectedCell() {
    mockModel.setCurrentPlayer(playerRed);

    Card testCard = new StandardCard("TestCard", Value.ONE, Value.TWO, Value.THREE,
            Value.FOUR);
    playerRed.addCardToHand(testCard);
    controllerRed.cardSelected(testCard);
    controllerRed.cellSelected(0, 0);

    String expectedPlaceCardCall = "placeCard: Red, TestCard, (0,0)";
    assertTrue(mockModel.methodCalls.contains(expectedPlaceCardCall));
    assertTrue(mockModel.methodCalls.contains("switchTurn"));
    assertTrue(mockViewRed.methodCalls.contains("updateView"));
    assertTrue(mockViewBlue.methodCalls.contains("updateView"));
  }

  /**
   * Test placing a card when no card is selected shows an error.
   */
  @Test
  public void testCellSelectedNoCardSelectedErrorShows() {
    mockModel.setCurrentPlayer(playerRed);
    controllerRed.cellSelected(0, 0);
    assertTrue(mockViewRed.methodCalls.contains("showErrorMessage: No card selected."));
  }

  /**
   * Test that after placing a card, the AI takes its turn automatically.
   */
  @Test
  public void testCellSelectedThenAICardPlays() {
    mockModel.setCurrentPlayer(playerRed);
    Card redCard = new StandardCard("TestCard", Value.ONE, Value.TWO, Value.THREE,
            Value.FOUR);
    playerRed.addCardToHand(redCard);
    controllerRed.cardSelected(redCard);
    controllerRed.cellSelected(0, 0);
    String expectedPlaceCardCall = "placeCard: Red, RedCard, (0,0)";
    assertTrue(mockModel.methodCalls.contains(expectedPlaceCardCall));
    assertTrue(mockModel.methodCalls.contains("switchTurn"));

    // AI placing a card
    Card aiCard = new StandardCard("TestCard", Value.ONE, Value.TWO, Value.THREE, Value.FOUR);
    playerBlue.addCardToHand(aiCard);
    controllerBlue.cardSelected(aiCard);
    controllerBlue.cellSelected(1, 1);

    String expectedAICall = "placeCard: Blue, AICard, (1,1)";
    assertTrue(mockModel.methodCalls.contains(expectedAICall));
    assertTrue(mockModel.methodCalls.contains("switchTurn"));
    assertTrue(mockViewRed.methodCalls.contains("updateView"));
    assertTrue(mockViewBlue.methodCalls.contains("updateView"));
  }

  /**
   * Test that placing a card when the game is over shows game over messages.
   */
  @Test
  public void testCellSelectedWhenGameOverShowsMessage() {
    mockModel.setCurrentPlayer(playerRed);
    Card redCard = new StandardCard("TestCard", Value.ONE, Value.TWO, Value.THREE,
            Value.FOUR);
    playerRed.addCardToHand(redCard);
    controllerRed.cardSelected(redCard);
    mockModel.setGameOver(true, "Red");
    controllerRed.cellSelected(0, 0);


    String expectedPlaceCardCall = "placeCard: Red, RedCard, (0,0)";
    assertTrue(mockModel.methodCalls.contains(expectedPlaceCardCall));
    assertTrue(mockModel.methodCalls.contains("switchTurn"));
    assertTrue(mockViewRed.methodCalls.contains("updateView"));
    assertTrue(mockViewBlue.methodCalls.contains("updateView"));
    assertTrue(mockViewRed.methodCalls.contains("showGameOverMessage: Red"));
    assertTrue(mockViewBlue.methodCalls.contains("showGameOverMessage: Red"));
  }

  /**
   * Test placing a card with an invalid move shows an error.
   */
  @Test
  public void testCellSelected_InvalidMove() {

  }
}

