package strategy;

import org.junit.Before;
import org.junit.Test;

import model.AIPlayer;
import model.Card;
import model.Move;
import model.PlayerImpl;
import model.StandardCard;
import model.Value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * Class testing the corner strategy and its edge cases.
 */

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
    strategy = new CornerStrategy();
    mockModel = new CornerMockGameModel();
    aiPlayer = new AIPlayer("Red", strategy);
    opponent = new PlayerImpl("Blue");
    mockModel.setPlayers(aiPlayer, opponent);

    fireDragon = new StandardCard("FireDragon", Value.FIVE, Value.THREE, Value.SEVEN,
            Value.SIX);
    waterSerpent = new StandardCard("WaterSerpent", Value.FOUR, Value.SIX, Value.TWO,
            Value.EIGHT);
    earthGolem = new StandardCard("EarthGolem", Value.SEVEN, Value.FIVE, Value.THREE,
            Value.FOUR);
    windSpirit = new StandardCard("WindSpirit", Value.TWO, Value.EIGHT, Value.SIX,
            Value.FIVE);
    shadowWolf = new StandardCard("ShadowWolf", Value.THREE, Value.SEVEN, Value.FIVE,
            Value.SIX);
    lightningEagle = new StandardCard("LightningEagle", Value.SIX, Value.FIVE, Value.EIGHT,
            Value.THREE);

  }

  /**
   * Tests a scenario where all corners are available and the upper-leftmost corner is chosen.
   */
  @Test
  public void testAllCornersAvailable() {
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // Set potential flips: All corners have 0 flips
    mockModel.setPotentialFlips(0, 0, 0);
    mockModel.setPotentialFlips(0, 2, 0);
    mockModel.setPotentialFlips(2, 0, 0);
    mockModel.setPotentialFlips(2, 2, 0);

    mockModel.setCurrentPlayer(aiPlayer);

    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when all corners are available", move);

    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
    assertEquals(fireDragon, move.getCard());
    assertTrue(mockModel.getInspectedCoordinates()
            .contains("0,0"));
  }

  /**
   * Tests a scenario where some corners are unavailable and chooses the correct corner.
   */
  @Test
  public void testSomeCornersOccupied() {
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    opponent.addCardToHand(earthGolem);
    mockModel.placeCard(opponent, earthGolem, 0, 0);

    // Set potential flips: (0,2) available with 0 flips
    mockModel.setPotentialFlips(0, 2, 0);
    mockModel.setPotentialFlips(2, 0, 0);
    mockModel.setPotentialFlips(2, 2, 0);

    mockModel.setCurrentPlayer(aiPlayer);
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when some corners are unavailable", move);

    // Should select next available corner (0,2)
    assertEquals(0, move.getRow());
    assertEquals(2, move.getCol());
    assertEquals(fireDragon, move.getCard());

    // Verify that the strategy inspected (0,0) and (0,2)
    assertTrue(mockModel.getInspectedCoordinates().contains("0,0"));
    assertTrue(mockModel.getInspectedCoordinates().contains("0,2"));
  }

  /**
   * Tests a scenario where no corners are available.
   */
  @Test
  public void testNoCornersAvailable() {
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // Occupy all corners with opponent's cards
    opponent.addCardToHand(earthGolem);
    opponent.addCardToHand(windSpirit);
    opponent.addCardToHand(shadowWolf);
    opponent.addCardToHand(waterSerpent);

    mockModel.placeCard(opponent, earthGolem, 0, 0);
    mockModel.placeCard(opponent, windSpirit, 0, 2);
    mockModel.placeCard(opponent, shadowWolf, 2, 0);
    mockModel.placeCard(opponent, waterSerpent, 2, 2);

    mockModel.setCurrentPlayer(aiPlayer);

    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNull(move);
  }

  /**
   * Tests when multiple cards can be played to a corner.
   */
  @Test
  public void testTieBreakingOnCards() {
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    // Set potential flips: Both cards can be played at (0,0) with 0 flips
    mockModel.setPotentialFlips(0, 0, 0);

    mockModel.setCurrentPlayer(aiPlayer);

    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when multiple cards can be played to a corner", move);

    // Should select (0,0) with the first card in hand (fireDragon)
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
    assertEquals(fireDragon, move.getCard());
    assertTrue(mockModel.getInspectedCoordinates().contains("0,0"));
  }

  /**
   * Tests when all corners are holes.
   */
  @Test
  public void testCornersAreHoles() {
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    mockModel.setHole(0, 0, true);
    mockModel.setHole(0, 2, true);
    mockModel.setHole(2, 0, true);
    mockModel.setHole(2, 2, true);

    // All corners have 0 flips
    mockModel.setPotentialFlips(0, 0, 0);
    mockModel.setPotentialFlips(0, 2, 0);
    mockModel.setPotentialFlips(2, 0, 0);
    mockModel.setPotentialFlips(2, 2, 0);

    mockModel.setCurrentPlayer(aiPlayer);
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNull("Strategy should return null when all corners are holes", move);
  }

  /**
   * Tests when only one corner is available.
   */
  @Test
  public void testSingleCornerAvailable() {
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    opponent.addCardToHand(earthGolem);
    opponent.addCardToHand(windSpirit);
    opponent.addCardToHand(shadowWolf);

    mockModel.placeCard(opponent, earthGolem, 0, 0);
    mockModel.placeCard(opponent, windSpirit, 0, 2);
    mockModel.placeCard(opponent, shadowWolf, 2, 0);

    mockModel.setPotentialFlips(2, 2, 0);

    mockModel.setCurrentPlayer(aiPlayer);

    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when one corner is available", move);

    assertEquals("Strategy should select row 2", 2, move.getRow());
    assertEquals("Strategy should select column 2", 2, move.getCol());
    assertEquals("Strategy should select the first card in hand", fireDragon,
            move.getCard());

    assertTrue("Strategy should have inspected (0,0)", mockModel.getInspectedCoordinates()
            .contains("0,0"));
    assertTrue("Strategy should have inspected (0,2)", mockModel.getInspectedCoordinates()
            .contains("0,2"));
    assertTrue("Strategy should have inspected (2,0)", mockModel.getInspectedCoordinates()
            .contains("2,0"));
    assertTrue("Strategy should have inspected (2,2)", mockModel.getInspectedCoordinates()
            .contains("2,2"));
  }

  /**
   * Tests when the AI has no cards.
   */
  @Test
  public void testAIHasNoCards() {
    aiPlayer.getHand().clear();
    mockModel.setCurrentPlayer(aiPlayer);
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNull(move);
  }

  /**
   * Tests when all cells are occupied except for the corners.
   */
  @Test
  public void testFullGridExceptCorners() {
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    opponent.addCardToHand(earthGolem);
    opponent.addCardToHand(windSpirit);
    opponent.addCardToHand(shadowWolf);
    opponent.addCardToHand(lightningEagle);

    // Place opponent's cards in non-corner positions
    mockModel.placeCard(opponent, earthGolem, 0, 1);
    mockModel.placeCard(opponent, windSpirit, 1, 0);
    mockModel.placeCard(opponent, shadowWolf, 1, 1);
    mockModel.placeCard(opponent, lightningEagle, 1, 2);
    mockModel.placeCard(opponent, windSpirit, 2, 1);

    mockModel.setPotentialFlips(0, 0, 0);
    mockModel.setPotentialFlips(0, 2, 0);
    mockModel.setPotentialFlips(2, 0, 0);
    mockModel.setPotentialFlips(2, 2, 0);

    mockModel.setCurrentPlayer(aiPlayer);
    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when corners are available", move);

    // Should select the upper-leftmost corner
    assertEquals("Strategy should select row 0", 0, move.getRow());
    assertEquals("Strategy should select column 0", 0, move.getCol());
    assertEquals("Strategy should select the first card in hand", fireDragon,
            move.getCard());

    assertTrue("Strategy should have inspected (0,0)", mockModel.getInspectedCoordinates()
            .contains("0,0"));
    assertTrue("Strategy should have inspected (0,2)", mockModel.getInspectedCoordinates()
            .contains("0,2"));
    assertTrue("Strategy should have inspected (2,0)", mockModel.getInspectedCoordinates()
            .contains("2,0"));
    assertTrue("Strategy should have inspected (2,2)", mockModel.getInspectedCoordinates()
            .contains("2,2"));
  }

  /**
   * Tests when the hand has illegal cards
   */
//  @Test
//  public void testHandContainsOnlyUnplayableCards() {
////    Card unplayableCard1 = new StandardCard("Unplayable1", 100, 100, 100, 100);
////    Card unplayableCard2 = new StandardCard("Unplayable2", 100, 100, 100, 100);
////    aiPlayer.addCardToHand(unplayableCard1);
////    aiPlayer.addCardToHand(unplayableCard2);

//    mockModel.setPotentialFlips(0, 0, -1);
//    mockModel.setPotentialFlips(0, 2, -1);
//    mockModel.setPotentialFlips(2, 0, -1);
//    mockModel.setPotentialFlips(2, 2, -1);
//    mockModel.setCurrentPlayer(aiPlayer);
//
//    Move move = strategy.determineMove(mockModel, aiPlayer);
//    assertNull(move);
//  }

  /**
   * Tests when corner placement can flip cards.
   */
  @Test
  public void testOpposingCardsAdjacentToCorners() {
    aiPlayer.addCardToHand(fireDragon);
    aiPlayer.addCardToHand(waterSerpent);

    opponent.addCardToHand(earthGolem);
    opponent.addCardToHand(windSpirit);
    opponent.addCardToHand(shadowWolf);
    opponent.addCardToHand(lightningEagle);

    mockModel.setCurrentPlayer(opponent);
    mockModel.placeCard(opponent, earthGolem, 0, 1);
    mockModel.placeCard(opponent, windSpirit, 1, 0);
    mockModel.placeCard(opponent, shadowWolf, 1, 2);
    mockModel.placeCard(opponent, lightningEagle, 2, 1);

    mockModel.setPotentialFlips(0, 0, 2);
    mockModel.setPotentialFlips(0, 2, 2);
    mockModel.setPotentialFlips(2, 0, 2);
    mockModel.setPotentialFlips(2, 2, 2);

    mockModel.setCurrentPlayer(aiPlayer);

    Move move = strategy.determineMove(mockModel, aiPlayer);
    assertNotNull("Strategy should return a move when corners are available", move);

    // Strategy should prefer the upper-leftmost corner
    assertEquals("Strategy should select row 0", 0, move.getRow());
    assertEquals("Strategy should select column 0", 0, move.getCol());
    assertEquals("Strategy should select the first card in hand", fireDragon, move.getCard());

    assertTrue(mockModel.getInspectedCoordinates().contains("0,0"));

    // double check
    int flips = mockModel.getPotentialFlips(aiPlayer, fireDragon, 0, 0);
    assertEquals(2, flips);
  }
}
