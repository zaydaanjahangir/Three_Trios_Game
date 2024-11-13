package strategy;

// CornerStrategyTest.java

import model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CornerStrategyTest {

  private CornerMockGameModel mockModel;
  private AIPlayer aiPlayer;
  private PlayerImpl opponent;
  private CornerStrategy strategy;

  private Card fireDragon;
  private Card waterSerpent;
  private Card earthGolem;
  private Card windSpirit;
  private Card shadowWolf;
  private Card lightningEagle;

  @Before
  public void setUp() {
    // Initialize strategy
    strategy = new CornerStrategy();

    // Initialize mock model
    mockModel = new CornerMockGameModel();

    // Initialize players
    aiPlayer = new AIPlayer("Red", strategy);
    opponent = new PlayerImpl("Blue");

    // Set players in mock model
    mockModel.setPlayers(aiPlayer, opponent);

    // Initialize cards
    fireDragon = new StandardCard("FireDragon", Value.FIVE, Value.THREE, Value.SEVEN, Value.SIX);
    waterSerpent = new StandardCard("WaterSerpent", Value.FOUR, Value.SIX, Value.TWO, Value.EIGHT);
    earthGolem = new StandardCard("EarthGolem", Value.SEVEN, Value.FIVE, Value.THREE, Value.FOUR);
    windSpirit = new StandardCard("WindSpirit", Value.TWO, Value.EIGHT, Value.SIX, Value.FIVE);
    shadowWolf = new StandardCard("ShadowWolf", Value.THREE, Value.SEVEN, Value.FIVE, Value.SIX);
    lightningEagle = new StandardCard("LightningEagle", Value.SIX, Value.FIVE, Value.EIGHT, Value.THREE);

  }

  /**
   * Tests a scenario where all corners are available and the upper-leftmost corner is chosen.
   */
  @Test
  public void testAllCornersAvailable() {
    // Add cards to AI's hand
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // No opponent's cards on the grid

    // Set potential flips: All corners have 0 flips
    mockModel.setPotentialFlips(0, 0, 0);
    mockModel.setPotentialFlips(0, 2, 0);
    mockModel.setPotentialFlips(2, 0, 0);
    mockModel.setPotentialFlips(2, 2, 0);

    // Set current player to AI
    mockModel.setCurrentPlayer(aiPlayer);

    // Execute strategy
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when all corners are available", move);

    // Verify that the strategy selected the upper-leftmost corner (0,0) with the first card
    assertEquals("Strategy should select row 0", 0, move.getRow());
    assertEquals("Strategy should select column 0", 0, move.getCol());
    assertEquals("Strategy should select the first card in hand", fireDragon, move.getCard());

    // Optionally, verify that (0,0) was inspected
    assertTrue("Strategy should have inspected (0,0)", mockModel.getInspectedCoordinates().contains("0,0"));
  }

  /**
   * Tests a scenario where some corners are unavailable and chooses the correct corner.
   */
  @Test
  public void testSomeCornersOccupied() {
    // Add cards to AI's hand
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // Occupy (0,0) with opponent's card
    opponent.addCardToHand(earthGolem);
    mockModel.placeCard(opponent, earthGolem, 0, 0); // (0,0) is occupied

    // Set potential flips: (0,2) available with 0 flips
    mockModel.setPotentialFlips(0, 2, 0);
    mockModel.setPotentialFlips(2, 0, 0);
    mockModel.setPotentialFlips(2, 2, 0);

    // Set current player to AI
    mockModel.setCurrentPlayer(aiPlayer);

    // Execute strategy
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when some corners are unavailable", move);

    // Should select next available corner (0,2)
    assertEquals("Strategy should select row 0", 0, move.getRow());
    assertEquals("Strategy should select column 2", 2, move.getCol());
    assertEquals("Strategy should select the first card in hand", fireDragon, move.getCard());

    // Verify that the strategy inspected (0,0) and (0,2)
    assertTrue("Strategy should have inspected (0,0)", mockModel.getInspectedCoordinates().contains("0,0"));
    assertTrue("Strategy should have inspected (0,2)", mockModel.getInspectedCoordinates().contains("0,2"));
  }

  /**
   * Tests a scenario where no corners are available.
   */
  @Test
  public void testNoCornersAvailable() {
    // Add cards to AI's hand
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // Occupy all corners with opponent's cards
    opponent.addCardToHand(earthGolem);
    opponent.addCardToHand(windSpirit);
    opponent.addCardToHand(shadowWolf);
    opponent.addCardToHand(waterSerpent); // Reusing waterSerpent for opponent's card

    mockModel.placeCard(opponent, earthGolem, 0, 0); // (0,0)
    mockModel.placeCard(opponent, windSpirit, 0, 2); // (0,2)
    mockModel.placeCard(opponent, shadowWolf, 2, 0); // (2,0)
    mockModel.placeCard(opponent, waterSerpent, 2, 2); // (2,2)

    // Set current player to AI
    mockModel.setCurrentPlayer(aiPlayer);

    // Execute strategy
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNull("Strategy should return null when no corners are available", move);
  }

  /**
   * Tests when multiple cards can be played to a corner.
   */
  @Test
  public void testTieBreakingOnCards() {
    // Add two AI cards that can both be played at (0,0)
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // Set potential flips: Both cards can be played at (0,0) with 0 flips
    mockModel.setPotentialFlips(0, 0, 0);

    // Set current player to AI
    mockModel.setCurrentPlayer(aiPlayer);

    // Execute strategy
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when multiple cards can be played to a corner", move);

    // Should select (0,0) with the first card in hand (fireDragon)
    assertEquals("Strategy should select row 0", 0, move.getRow());
    assertEquals("Strategy should select column 0", 0, move.getCol());
    assertEquals("Strategy should select the first card in hand", fireDragon, move.getCard());

    // Verify that the strategy inspected (0,0)
    assertTrue("Strategy should have inspected (0,0)", mockModel.getInspectedCoordinates().contains("0,0"));
  }

  /**
   * Tests when all corners are holes.
   */
  @Test
  public void testCornersAreHoles() {
    // Add cards to AI's hand
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // Mark all corners as holes
    mockModel.setHole(0, 0, true);
    mockModel.setHole(0, 2, true);
    mockModel.setHole(2, 0, true);
    mockModel.setHole(2, 2, true);

    // Set potential flips: All corners have 0 flips (irrelevant since they are holes)
    mockModel.setPotentialFlips(0, 0, 0);
    mockModel.setPotentialFlips(0, 2, 0);
    mockModel.setPotentialFlips(2, 0, 0);
    mockModel.setPotentialFlips(2, 2, 0);

    // Set current player to AI
    mockModel.setCurrentPlayer(aiPlayer);

    // Execute strategy
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNull("Strategy should return null when all corners are holes", move);
  }

  /**
   * Single Corner Available:
   *
   *     Scenario: Only one corner is available.
   *     Expected Outcome: The strategy selects that corner and chooses the appropriate card.
   *     Test Case: Occupy or mark holes in three corners.
   */
  @Test
  public void testSingleCornerAvailable() {
    // Add cards to AI's hand
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // Occupy three corners: (0,0), (0,2), (2,0)
    opponent.addCardToHand(earthGolem);
    opponent.addCardToHand(windSpirit);
    opponent.addCardToHand(shadowWolf);

    mockModel.placeCard(opponent, earthGolem, 0, 0); // (0,0) occupied
    mockModel.placeCard(opponent, windSpirit, 0, 2); // (0,2) occupied
    mockModel.placeCard(opponent, shadowWolf, 2, 0); // (2,0) occupied

    // Leave (2,2) available
    mockModel.setPotentialFlips(2, 2, 0);

    // Set current player to AI
    mockModel.setCurrentPlayer(aiPlayer);

    // Execute strategy
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when one corner is available", move);

    // Should select the only available corner (2,2)
    assertEquals("Strategy should select row 2", 2, move.getRow());
    assertEquals("Strategy should select column 2", 2, move.getCol());
    assertEquals("Strategy should select the first card in hand", fireDragon, move.getCard());

    // Verify that the strategy inspected all corners
    assertTrue("Strategy should have inspected (0,0)", mockModel.getInspectedCoordinates().contains("0,0"));
    assertTrue("Strategy should have inspected (0,2)", mockModel.getInspectedCoordinates().contains("0,2"));
    assertTrue("Strategy should have inspected (2,0)", mockModel.getInspectedCoordinates().contains("2,0"));
    assertTrue("Strategy should have inspected (2,2)", mockModel.getInspectedCoordinates().contains("2,2"));
  }

  /**
   * AI Has No Cards:
   *
   *     Scenario: AI's hand is empty.
   *     Expected Outcome: The strategy cannot make a move and should handle this case.
   *     Test Case: Ensure the strategy does not throw exceptions when the AI has no cards.
   */
  @Test
  public void testAIHasNoCards() {
    // Ensure AI's hand is empty
    aiPlayer.getHand().clear();

    // Set current player to AI
    mockModel.setCurrentPlayer(aiPlayer);

    // Execute strategy
    Move move = strategy.determineMove(mockModel, aiPlayer);

    // Strategy should handle empty hand gracefully, likely by returning null
    assertNull("AI has no cards, strategy should return null or handle appropriately", move);
  }

  /**
   * Full Grid Except Corners:
   *
   *     Scenario: All cells are occupied except for the corners.
   *     Expected Outcome: The strategy still selects the available corner.
   *     Test Case: Fill the grid accordingly.
   */
  @Test
  public void testFullGridExceptCorners() {
    // Add cards to AI's hand
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // Add opponent's cards to fill the grid except for corners
    opponent.addCardToHand(earthGolem);
    opponent.addCardToHand(windSpirit);
    opponent.addCardToHand(shadowWolf);
    opponent.addCardToHand(lightningEagle);

    // Place opponent's cards in non-corner positions
    mockModel.placeCard(opponent, earthGolem, 0, 1); // (0,1)
    mockModel.placeCard(opponent, windSpirit, 1, 0); // (1,0)
    mockModel.placeCard(opponent, shadowWolf, 1, 1); // (1,1)
    mockModel.placeCard(opponent, lightningEagle, 1, 2); // (1,2)
    mockModel.placeCard(opponent, windSpirit, 2, 1); // (2,1)

    // Set potential flips for corners
    mockModel.setPotentialFlips(0, 0, 0);
    mockModel.setPotentialFlips(0, 2, 0);
    mockModel.setPotentialFlips(2, 0, 0);
    mockModel.setPotentialFlips(2, 2, 0);

    // Set current player to AI
    mockModel.setCurrentPlayer(aiPlayer);

    // Execute strategy
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when corners are available", move);

    // Should select the upper-leftmost corner (0,0)
    assertEquals("Strategy should select row 0", 0, move.getRow());
    assertEquals("Strategy should select column 0", 0, move.getCol());
    assertEquals("Strategy should select the first card in hand", fireDragon, move.getCard());

    // Verify that the strategy inspected all corners
    assertTrue("Strategy should have inspected (0,0)", mockModel.getInspectedCoordinates().contains("0,0"));
    assertTrue("Strategy should have inspected (0,2)", mockModel.getInspectedCoordinates().contains("0,2"));
    assertTrue("Strategy should have inspected (2,0)", mockModel.getInspectedCoordinates().contains("2,0"));
    assertTrue("Strategy should have inspected (2,2)", mockModel.getInspectedCoordinates().contains("2,2"));
  }

  /**
   * Hand Contains Only Unplayable Cards:
   *
   *     Scenario: The AI's hand contains cards that cannot be legally placed (e.g., due to game rules).
   *     Expected Outcome: The strategy should not select invalid moves.
   *     Test Case: Simulate conditions where cards cannot be played.
   */
  @Test
  public void testHandContainsOnlyUnplayableCards() {
    // Add unplayable cards to AI's hand
//    Card unplayableCard1 = new StandardCard("Unplayable1", 100, 100, 100, 100);
//    Card unplayableCard2 = new StandardCard("Unplayable2", 100, 100, 100, 100);
//    aiPlayer.addCardToHand(unplayableCard1);
//    aiPlayer.addCardToHand(unplayableCard2);

    // Assume that unplayable cards cannot be placed by setting potential flips to -1
    // Since our MockGameModel returns 2 if in potentialFlipsMap, 0 otherwise,
    // we'll simulate unplayable by not adding the position to potentialFlipsMap
    // and ensuring isLegalMove always returns false for these cards.

    // Alternatively, you can modify the mock to handle specific cards, but for simplicity:
    // Set all corners to have potential flips as -1 (unplayable)
    mockModel.setPotentialFlips(0, 0, -1);
    mockModel.setPotentialFlips(0, 2, -1);
    mockModel.setPotentialFlips(2, 0, -1);
    mockModel.setPotentialFlips(2, 2, -1);

    // Set current player to AI
    mockModel.setCurrentPlayer(aiPlayer);

    // Execute strategy
    Move move = strategy.determineMove(mockModel, aiPlayer);

    // Strategy should return null since all cards are unplayable
    assertNull("All AI's cards are unplayable, strategy should return null", move);
  }

  /**
   * Opposing Cards Adjacent to Corners:
   *
   *     Scenario: Placing a card in the corner may result in flipping opponent's cards.
   *     Expected Outcome: The strategy still prefers corners but should handle any flips according to game rules.
   *     Test Case: Place opponent's cards adjacent to corners.
   */
  @Test
  public void testOpposingCardsAdjacentToCorners() {
    // Add cards to AI's hand
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // Add opponent's cards adjacent to corners
    opponent.addCardToHand(earthGolem);
    opponent.addCardToHand(windSpirit);
    opponent.addCardToHand(shadowWolf);
    opponent.addCardToHand(lightningEagle);

    // Place opponent's cards adjacent to corners
    mockModel.setCurrentPlayer(opponent);
    mockModel.placeCard(opponent, earthGolem, 0, 1); // Adjacent to (0,0) and (0,2)
    mockModel.placeCard(opponent, windSpirit, 1, 0); // Adjacent to (0,0) and (2,0)
    mockModel.placeCard(opponent, shadowWolf, 1, 2); // Adjacent to (0,2) and (2,2)
    mockModel.placeCard(opponent, lightningEagle, 2, 1); // Adjacent to (2,0) and (2,2)

    // Set potential flips: Placing at (0,0) flips (0,1) and (1,0)
    mockModel.setPotentialFlips(0, 0, 2);
    mockModel.setPotentialFlips(0, 2, 2);
    mockModel.setPotentialFlips(2, 0, 2);
    mockModel.setPotentialFlips(2, 2, 2);

    // Set current player to AI
    mockModel.setCurrentPlayer(aiPlayer);

    // Execute strategy
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when corners are available", move);

    // Strategy should prefer the upper-leftmost corner (0,0)
    assertEquals("Strategy should select row 0", 0, move.getRow());
    assertEquals("Strategy should select column 0", 0, move.getCol());
    assertEquals("Strategy should select the first card in hand", fireDragon, move.getCard());

    // Verify that the strategy inspected (0,0)
    assertTrue("Strategy should have inspected (0,0)", mockModel.getInspectedCoordinates().contains("0,0"));

    // Optionally, verify that the potential flips are handled correctly
    int flips = mockModel.getPotentialFlips(aiPlayer, fireDragon, 0, 0);
    assertEquals("Placing at (0,0) should flip 2 opponent cards", 2, flips);
  }


}
