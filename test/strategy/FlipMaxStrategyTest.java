package strategy;

import controller.CardFileReader;
import controller.CardFileReaderImpl;
import controller.GridFileReader;
import controller.GridFileReaderImpl;
import model.AIPlayer;
import model.Card;
import model.GameMode;
import model.GameModel;
import model.GameModelFactory;
import model.Grid;
import model.Move;
import model.PlayerImpl;


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * Class testing the Flip Maximising strategy which prioritizes immediate card flips.
 */
public class FlipMaxStrategyTest {

  private GameModel model;
  private AIPlayer aiPlayer;
  private PlayerImpl opponent;
  private FlipMaxStrategy strategy;
  private List<Card> cards;

  private GridFileReader gridReader;
  private Grid grid;
  private static final String GRID_FILE_PATH = "resources/grid_configs/grid1.txt";
  private static final String CARD_FILE_PATH = "resources/card_configs/cards3.txt";

  @Before
  public void setUp() {
    strategy = new FlipMaxStrategy();
    gridReader = new GridFileReaderImpl();
    CardFileReader cardReader = new CardFileReaderImpl();
    grid = gridReader.readGrid(new File(GRID_FILE_PATH));
    cards = cardReader.readCards(new File(CARD_FILE_PATH));

    GameModel gameModel = GameModelFactory.createGameModel(GameMode.PLAYER_VS_PLAYER);
    gameModel.initializeGame(grid, cards, null);

    aiPlayer = new AIPlayer("Red", strategy);
    opponent = new PlayerImpl("Blue");
    model = gameModel;
  }

  /**
   * Tests that the AI selects the correct move that flips the most opponent cards.
   */
  @Test
  public void testBasicFunctionality() {
    aiPlayer.addCardToHand(cards.get(5)); // ShadowWolf 3 7 5 6
    aiPlayer.addCardToHand(cards.get(1)); // WaterSerpent 4 6 2 8

    opponent.addCardToHand(cards.get(0)); // FireDragon 5 3 7 6
    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);

    // The ideal move in an empty board is 0,0
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
    assertEquals(aiPlayer.getHand().get(0), move.getCard());
  }

  /**
   * Tests that the AI selects the correct move in a tie-breaking scenario where cards placed
   * in different positions flip the same number of cards.
   */
  @Test
  public void testTieBreakingOnPositions() {
    aiPlayer.addCardToHand(cards.get(5)); // ShadowWolf 3 7 5 6
    aiPlayer.addCardToHand(cards.get(1)); // WaterSerpent 4 6 2 8

    opponent.addCardToHand(cards.get(0)); // FireDragon 5 3 7 6
    opponent.addCardToHand(cards.get(2)); // EarthGolem 7 5 3 4

    List<Card> aiHand = aiPlayer.getHand();
    List<Card> oppHand = opponent.getHand();

    System.out.println("AI Cards:");
    for (int i = 0; i < aiHand.size(); i++) {
      System.out.println(aiHand.get(i).getName());
    }
    System.out.println("\nOpponent Cards:");
    for (int i = 0; i < oppHand.size(); i++) {
      System.out.println(oppHand.get(i).getName());
    }

    model.setCurrentPlayerForTest(opponent);

    // Place cards
    System.out.println("Before placing card, current player is: " + model.getCurrentPlayer()
            .getColor());
    model.placeCard(opponent, opponent.getHand().get(0), 0, 1);
    System.out.println("After opponent's placement, current player: " + model.getCurrentPlayer()
            .getColor());
    System.out.println("");

    model.setCurrentPlayerForTest(opponent);
    System.out.println("Before placing card, current player is: " + model.getCurrentPlayer()
            .getColor());
    model.placeCard(opponent, oppHand.get(1), 1, 0);
    System.out.println("After opponent's placement, current player: " + model.getCurrentPlayer()
            .getColor());
    System.out.println("");

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
    aiPlayer.addCardToHand(cards.get(6)); // SolarPhoenix N8 S2 E7 W5
    aiPlayer.addCardToHand(cards.get(4)); // LightningEagle N6 S4 E8 W3

    opponent.addCardToHand(cards.get(3)); // WindSpirit N2 S8 E6 W5

    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(0), 1, 1);

    model.setCurrentPlayerForTest(aiPlayer);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);

    // According to tie-breaking rules, the card at index 0 (SolarPhoenix) should be chosen
    assertEquals(aiPlayer.getHand().get(0), move.getCard());
    assertEquals(1, move.getRow());
    assertEquals(0, move.getCol());
  }


  /**
   * Tests a scenario where no possible moves flip any opponents cards.
   */
  @Test
  public void testNoPossibleFlips() {
    // AI's hand
    aiPlayer.addCardToHand(cards.get(0)); // FireDragon
    aiPlayer.addCardToHand(cards.get(1)); // WaterSerpent

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);

    // Since no flips are possible, the strategy should choose the uppermost-leftmost position
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
    assertEquals(aiPlayer.getHand().get(0), move.getCard());
  }


  /**
   * Tests the AI on a board with occupied cells and holes.
   */
  @Test
  public void testOccupiedCellsAndHoles() {
    // Using grid config with holes
    grid = gridReader.readGrid(new File("resources/grid_configs/grid6.txt"));

    aiPlayer.addCardToHand(cards.get(0)); // FireDragon
    aiPlayer.addCardToHand(cards.get(1)); // WaterSerpent

    model.setCurrentPlayerForTest(aiPlayer);
    model.placeCard(aiPlayer, aiPlayer.getHand().get(0), 1, 0);
    model.setCurrentPlayerForTest(aiPlayer);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);

    // The strategy should avoid (0,0) and (1,0)
    assertFalse(move.getRow() == 0 && move.getCol() == 0);
    assertFalse(move.getRow() == 1 && move.getCol() == 0);
  }


  /**
   * Test Edge of Grid (Non-Corner Cells).
   * Scenario: Flipping maximum cards occurs at non-corner, edge positions.
   * Expected Outcome: The strategy correctly identifies these positions.
   */
  @Test
  public void testEdgeOfGridNonCornerCells() {
    aiPlayer.addCardToHand(cards.get(0)); // FireDragon
    aiPlayer.addCardToHand(cards.get(1)); // WaterSerpent

    opponent.addCardToHand(cards.get(2)); // EarthGolem
    opponent.addCardToHand(cards.get(3)); // WindSpirit
    opponent.addCardToHand(cards.get(4)); // LightningEagle
    opponent.addCardToHand(cards.get(5)); // ShadowWolf

    model.setCurrentPlayerForTest(opponent);

    // Placing opponent cards at edge positions
    model.placeCard(opponent, opponent.getHand().get(0), 0, 1); // Top edge
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(1), 1, 2); // Right edge
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(2), 2, 1); // Bottom edge
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(3), 1, 0); // Left edge

    model.setCurrentPlayerForTest(aiPlayer);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);

    // The AI should select an edge, non-corner cell to flip maximum cards
    boolean isEdgeNonCorner = (move.getRow() == 1 && move.getCol() == 1);
    assertTrue(isEdgeNonCorner);
  }


  /**
   * Test Multiple Flipping Opportunities.
   * Scenario: Flipping occurs in multiple directions from a single placement.
   * Expected Outcome: The strategy accounts for all possible flips in its calculation.
   */
  @Test
  public void testMultipleFlippingOpportunities() {
    aiPlayer.addCardToHand(cards.get(0)); // FireDragon

    opponent.addCardToHand(cards.get(3)); // WindSpirit at (0,1)
    opponent.addCardToHand(cards.get(1)); // WaterSerpent at (1,0)
    opponent.addCardToHand(cards.get(5)); // ShadowWolf at (1,2)
    opponent.addCardToHand(cards.get(4)); // LightningEagle at (2,1)

    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(0), 0, 1);
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(1), 1, 0);
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(2), 1, 2);
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(3), 2, 1);

    model.setCurrentPlayerForTest(aiPlayer);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);

    // The AI should place at (1,1) to flip multiple opponent cards
    assertEquals(1, move.getRow());
    assertEquals(1, move.getCol());

    int potentialFlips = model.getPotentialFlips(aiPlayer, move.getCard(), move.getRow(),
            move.getCol());
    assertTrue(potentialFlips >= 2);
  }


  /**
   * Test Combo Flips.
   * Scenario: Flipped cards can cause additional flips (combo).
   * Expected Outcome: The strategy considers the total flips, including combo flips.
   */
  @Test
  public void testComboFlips() {
    // AI's hand
    aiPlayer.addCardToHand(cards.get(0)); // FireDragon

    // Opponent's cards
    opponent.addCardToHand(cards.get(3)); // WindSpirit at (0,1)
    opponent.addCardToHand(cards.get(1)); // WaterSerpent at (1,0)
    opponent.addCardToHand(cards.get(5)); // ShadowWolf at (1,2)
    opponent.addCardToHand(cards.get(4)); // LightningEagle at (2,1)
    opponent.addCardToHand(cards.get(6)); // SolarPhoenix at (0,2)

    // Place opponent's cards
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(0), 0, 1);
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(1), 1, 0);
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(2), 1, 2);
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(3), 2, 1);
    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(4), 0, 2);

    model.setCurrentPlayerForTest(aiPlayer);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);

    assertEquals(1, move.getRow());
    assertEquals(1, move.getCol());

    int potentialFlips = model.getPotentialFlips(aiPlayer, move.getCard(), move.getRow(),
            move.getCol());
    assertTrue(potentialFlips > 2);
  }


  /**
   * Test Opponent Has No Cards Adjacent.
   * Scenario: No opponent cards are adjacent to any empty cells.
   * Expected Outcome: The strategy selects a move based on tie-breaking rules.
   */
  @Test
  public void testNoAdjacentOpponentCards() {
    aiPlayer.addCardToHand(cards.get(0)); // FireDragon
    aiPlayer.addCardToHand(cards.get(1)); // WaterSerpent

    opponent.addCardToHand(cards.get(3)); // WindSpirit

    model.setCurrentPlayerForTest(opponent);
    model.placeCard(opponent, opponent.getHand().get(0), 2, 2);

    model.setCurrentPlayerForTest(aiPlayer);

    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull(move);

    // Since no flips are possible, strategy should choose (0,0)
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
    assertEquals(aiPlayer.getHand().get(0), move.getCard());
  }


  /**
   * Test Full Hand vs. Few Cards.
   * Scenario: AI has a full hand or only one card.
   * Expected Outcome: The strategy works regardless of the number of cards in hand.
   */
  @Test
  public void testFullHandVsFewCards() {
    // Full hand setup
    for (Card card : cards) {
      aiPlayer.addCardToHand(card);
    }

    // Run strategy with full hand
    Move moveFullHand = strategy.determineMove(model, aiPlayer);
    assertNotNull("Move should not be null for full hand", moveFullHand);

    // Clear AI's hand and add a single card
    aiPlayer.getHand().clear();
    aiPlayer.addCardToHand(cards.get(0)); // FireDragon

    // Run strategy with single card
    Move moveSingleCard = strategy.determineMove(model, aiPlayer);
    assertNotNull(moveSingleCard);
  }


  /**
   * Test No Valid Moves.
   * Scenario: No legal moves are available.
   * Expected Outcome: The strategy returns null or a default move as per implementation.
   */
  @Test
  public void testNoValidMoves() {
    // Fill the grid with opponent's cards
    model.setCurrentPlayerForTest(opponent);
    int cardIndex = 0;
    for (int row = 0; row < model.getGridRows(); row++) {
      for (int col = 0; col < model.getGridCols(); col++) {
        if (!model.getCellAt(row, col).isHole()) {
          opponent.addCardToHand(cards.get(cardIndex % cards.size()));
          model.setCurrentPlayerForTest(opponent);
          model.placeCard(opponent, opponent.getHand().get(opponent.getHand().size() - 1), row,
                  col);
          cardIndex++;
        }
      }
    }

    model.setCurrentPlayerForTest(aiPlayer);

    Move move = strategy.determineMove(model, aiPlayer);

    // Move should be null
    assertNull(move);
  }
}
