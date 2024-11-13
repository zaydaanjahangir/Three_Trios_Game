package strategy;

// CornerStrategyTest.java

import controller.CardFileReader;
import controller.CardFileReaderImpl;
import controller.GridFileReader;
import controller.GridFileReaderImpl;
import model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

public class CornerStrategyTest {

  private ReadOnlyGameModel model;
  private AIPlayer aiPlayer;
  private PlayerImpl opponent;
  private CornerStrategy strategy;
  private static final String GRID_FILE_PATH = "resources/grid_configs/grid1.txt";
  private static final String CARD_FILE_PATH = "resources/card_configs/cards3.txt";

  @Before
  public void setUp() {
    strategy = new CornerStrategy();
    GridFileReader gridReader = new GridFileReaderImpl();
    CardFileReader cardReader = new CardFileReaderImpl();
    Grid grid = gridReader.readGrid(new File(GRID_FILE_PATH));
    List<Card> cards = cardReader.readCards(new File(CARD_FILE_PATH));

    GameModel gameModel = GameModelFactory.createGameModel(GameMode.PLAYER_VS_PLAYER);
    gameModel.initializeGame(grid, cards, new Random());

    aiPlayer = new AIPlayer("Red", strategy);
    aiPlayer.addCardToHand(cards.get(0));
    aiPlayer.addCardToHand(cards.get(1));

    opponent = new PlayerImpl("Blue");
    opponent.addCardToHand(cards.get(3));

    // Cast the GameModel to a ReadonlyThreeTriosModel for testing
    model = (ReadOnlyGameModel) gameModel;
  }

  /**
   * Tests a scenario where all corners are available and the upper-leftmost corner is chosen.
   */
  @Test
  public void testAllCornersAvailable() {
    // All corners are available
    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);
    // Should select uppermost-leftmost corner (0,0)
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
    assertEquals(aiPlayer.getHand().get(0), move.getCard());
  }

  /**
   * Tests a scenario where some corners are unavailable and chooses the correct corner.
   */
  @Test
  public void testSomeCornersOccupied() {
    // Occupy (0,0) with opponent's card
    Card oppCard = new StandardCard("Opp Card", Value.THREE, Value.THREE, Value.THREE, Value.THREE);
    opponent = new PlayerImpl("Blue");
    opponent.addCardToHand(oppCard);
    ((GameModel) model).placeCard(opponent, oppCard, 0, 0);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);
    // Should select next available corner (0,2)
    assertEquals(0, move.getRow());
    assertEquals(2, move.getCol());
  }

  /**
   * Tests a scenario where no corners are available.
   */
  @Test
  public void testNoCornersAvailable() {
    // Occupy all corners
    Card oppCard = new StandardCard("Opp Card", Value.THREE, Value.THREE, Value.THREE, Value.THREE);
    opponent = new PlayerImpl("Blue");
    opponent.addCardToHand(oppCard);
    ((GameModel) model).placeCard(opponent, oppCard, 0, 0);
    ((GameModel) model).placeCard(opponent, oppCard, 0, 2);
    ((GameModel) model).placeCard(opponent, oppCard, 2, 0);
    ((GameModel) model).placeCard(opponent, oppCard, 2, 2);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNull("No corners available, strategy should return null or handle appropriately", move);
  }

  /**
   * Tests when multiple cards can be played to a corner.
   */
  @Test
  public void testTieBreakingOnCards() {
    // Both cards can be played at corner (0,0)
    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);
    // Should select the card with the lowest index in hand
    assertEquals(aiPlayer.getHand().get(0), move.getCard());
  }

  /**
   * Tests when all corners are holes/
   */
  @Test
  public void testCornersAreHoles() {
    // Mark corners as holes
//    ((StandardGrid) model.getGrid()).getCell(0, 0).setHole(true);
//    ((StandardGrid) model.getGrid()).getCell(0, 2).setHole(true);
//    ((StandardGrid) model.getGrid()).getCell(2, 0).setHole(true);
//    ((StandardGrid) model.getGrid()).getCell(2, 2).setHole(true);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNull("All corners are holes, strategy should return null or handle appropriately", move);
  }

  //TODO
  /**
   * Single Corner Available:
   *
   *     Scenario: Only one corner is available.
   *     Expected Outcome: The strategy selects that corner and chooses the appropriate card.
   *     Test Case: Occupy or mark holes in three corners.
   *
   *  AI Has No Cards:
   *
   *     Scenario: AI's hand is empty.
   *     Expected Outcome: The strategy cannot make a move and should handle this case.
   *     Test Case: Ensure the strategy does not throw exceptions when the AI has no cards.
   *
   * Full Grid Except Corners:
   *
   *     Scenario: All cells are occupied except for the corners.
   *     Expected Outcome: The strategy still selects the available corner.
   *     Test Case: Fill the grid accordingly.
   *
   * Hand Contains Only Unplayable Cards:
   *
   *     Scenario: The AI's hand contains cards that cannot be legally placed (e.g., due to game rules).
   *     Expected Outcome: The strategy should not select invalid moves.
   *     Test Case: Simulate conditions where cards cannot be played.
   *
   * Opposing Cards Adjacent to Corners:
   *
   *     Scenario: Placing a card in the corner may result in flipping opponent's cards.
   *     Expected Outcome: The strategy still prefers corners but should handle any flips according to game rules.
   *     Test Case: Place opponent's cards adjacent to corners.
   */

}

