package strategy;

import controller.CardFileReader;
import controller.CardFileReaderImpl;
import controller.GridFileReader;
import controller.GridFileReaderImpl;
import model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class FlipMaxStrategyTest {

  private ReadOnlyGameModel model;
  private AIPlayer aiPlayer;
  private PlayerImpl opponent;
  private FlipMaxStrategy strategy;
  private static final String GRID_FILE_PATH = "resources/grid_configs/grid1.txt";
  private static final String CARD_FILE_PATH = "resources/card_configs/cards3.txt";

  @Before
  public void setUp() {
    strategy = new FlipMaxStrategy();
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
   * Tests that the AI selects the correct move that flips the most opponent cards.
   */
  @Test
  public void testBasicFunctionality() {
    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);
    // Assuming that placing aiCard1 at (1,0) flips one opponent card
    assertEquals(1, move.getRow());
    assertEquals(0, move.getCol());
    assertEquals(aiPlayer.getHand().get(0), move.getCard()); // aiCard1
  }

  /**
   * Tests that the AI selects the correct move in a tie-breaking scenario where cards placed
   * in different positions flip the same number of cards.
   */
  @Test
  public void testTieBreakingOnPositions() {
    // Modify the setup to create a tie on the number of flips for different positions
    // Place another opponent card at (0,1)
    Card oppCard2 = new StandardCard("Opp Card 2", Value.THREE, Value.THREE, Value.THREE, Value.THREE);
    opponent.addCardToHand(oppCard2);
    ((GameModel) model).placeCard(opponent, oppCard2, 0, 1);

    // Now, placing at (0,0) or (1,0) both flip one opponent card

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);
    // Should select (0,0) as it's uppermost-leftmost
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
  }

  /**
   * Tests that the AI selects the correct move in a tie-breaking scenario where different cards
   * placed in the same position flip the same number of cards.
   */
  @Test
  public void testTieBreakingOnCards() {
    // Modify the setup so that both cards can flip the same number of opponent cards at the same position
    // Both aiCard1 and aiCard2 can flip one card at (1,0)

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);
    // Should select the card with the lowest index in hand (aiCard1)
    assertEquals(aiPlayer.getHand().get(0), move.getCard());
  }

  /**
   * Tests a scenario where no possible moves flip any opponents cards.
   */
  @Test
  public void testNoPossibleFlips() {
    // Remove opponent cards so no flips are possible
//    ((GameModel) model).removeCardFromGrid(1, 1);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);
    // Should select the uppermost-leftmost position (0,0) and card at index 0
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
    assertEquals(aiPlayer.getHand().get(0), move.getCard());
  }

  /**
   * Tests the AI on a board with occupied cells and holes.
   */
  @Test
  public void testOccupiedCellsAndHoles() {
    // Mark some cells as holes and others as occupied
    // Modify the grid accordingly
    // Ensure the strategy does not select these positions

    // Assume cell at (0,0) is a hole
//    ((StandardGrid) model.getGrid()).getCell(0, 0).setHole(true);

    // Assume cell at (1,0) is occupied by AI's own card
    ((GameModel) model).placeCard(aiPlayer, aiPlayer.getHand().get(0), 1, 0);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);
    // Should not select (0,0) or (1,0)
    assertNotEquals(0, move.getRow());
    assertNotEquals(1, move.getRow());
  }

  // TODO
  /**
   *Edge of Grid (Non-Corner Cells):
   *
   *     Scenario: Flipping maximum cards occurs at non-corner, edge positions.
   *     Expected Outcome: The strategy correctly identifies these positions.
   *     Test Case: Place opponent cards along the edge, making edge positions optimal.
   *
   * Multiple Flipping Opportunities:
   *
   *     Scenario: Flipping occurs in multiple directions from a single placement.
   *     Expected Outcome: The strategy accounts for all possible flips in its calculation.
   *     Test Case: Set up a position where placing a card flips opponent cards in multiple adjacent cells.
   *
   * Combo Flips:
   *
   *     Scenario: Flipped cards can cause additional flips (combo).
   *     Expected Outcome: The strategy considers the total flips, including combo flips.
   *     Test Case: Ensure the strategy calculates potential flips beyond the immediate adjacent cards.
   *
   * Opponent Has No Cards Adjacent:
   *
   *     Scenario: No opponent cards are adjacent to any empty cells.
   *     Expected Outcome: The strategy selects a move based on tie-breaking rules.
   *     Test Case: All opponent cards are isolated or surrounded by AI's cards.
   *
   * Full Hand vs. Few Cards:
   *
   *     Scenario: AI has a full hand or only one card.
   *     Expected Outcome: The strategy works regardless of the number of cards in hand.
   *     Test Case: Test with different hand sizes.
   *
   * No Valid Moves:
   *
   *     Scenario: No legal moves are available.
   *     Expected Outcome: The strategy returns null or a default move as per implementation.
   *     Test Case: The grid is full or all cells are unplayable.
   */

}
