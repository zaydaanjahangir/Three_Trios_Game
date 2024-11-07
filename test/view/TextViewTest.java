package view;


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import model.Card;
import model.GameMode;
import model.GameModel;
import model.GameModelFactory;
import model.Grid;
import model.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Tests the view representing the current grid state.
 */
public class TextViewTest {

  private GameModel model;
  private TextView view;
  private File gridFile;
  private File cardFile;
  private Random random;

  @Before
  public void setUp() {
    model = GameModelFactory.createGameModel(GameMode.PLAYER_VS_PLAYER);
    view = new TextView(model);
    gridFile = getResourceFile("grid_configs/grid1.txt");
    cardFile = getResourceFile("card_configs/cards3.txt");
    random = new Random(100);
    model.initializeGame(gridFile, cardFile, random);
  }

  private File getResourceFile(String path) {
    ClassLoader classLoader = getClass().getClassLoader();
    URL resource = classLoader.getResource(path);
    assertNotNull("Resource file not found: " + path, resource);
    try {
      return new File(resource.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException("Failed to load resource file: " + path, e);
    }
  }

  /**
   * Test the initial rendering of the game state.
   */
  @Test
  public void testRenderGameStart() {
    String output = view.render();

    String expectedOutput = "" +
            "Player: RED\n" +
            "___\n" +
            "___\n" +
            "___\n" +
            "Hand:\n" +
            "ShadowWolf 3 7 5 6\n" +
            "FireDragon 5 3 7 6\n" +
            "LightningEagle 6 4 8 3\n" +
            "EarthGolem 7 5 3 4\n" +
            "OceanLeviathan A 3 7 6\n";

    assertEquals(expectedOutput, output);
  }

  /**
   * Test rendering after the Red player places a card.
   */
  @Test
  public void testRenderAfterRedPlacesCard() {
    Player currentPlayer = model.getCurrentPlayer();
    Card cardToPlace = currentPlayer.getHand().get(0);
    model.placeCard(currentPlayer, cardToPlace, 1, 1);
    String output = view.render();

    String expectedOutput = "" +
            "Player: BLUE\n" +
            "___\n" +
            "_R_\n" +
            "___\n" +
            "Hand:\n" +
            "ThunderDragon 8 4 9 2\n" +
            "StoneGiant 6 5 4 7\n" +
            "SolarPhoenix 8 2 7 5\n" +
            "WindSpirit 2 8 6 5\n" +
            "WaterSerpent 4 6 2 8\n";

    assertEquals(expectedOutput, output);
  }

  /**
   * Test rendering after both players have placed one card.
   */
  @Test
  public void testRenderAfterBothPlayersPlaceCards() {
    Player redPlayer = model.getCurrentPlayer();
    Card redCard = redPlayer.getHand().get(0);
    model.placeCard(redPlayer, redCard, 1, 1);

    Player bluePlayer = model.getCurrentPlayer();
    Card blueCard = bluePlayer.getHand().get(0);
    model.placeCard(bluePlayer, blueCard, 0, 0);

    String output = view.render();

    String expectedOutput = "" +
            "Player: RED\n" +
            "B__\n" +
            "_R_\n" +
            "___\n" +
            "Hand:\n" +
            "FireDragon 5 3 7 6\n" +
            "LightningEagle 6 4 8 3\n" +
            "EarthGolem 7 5 3 4\n" +
            "OceanLeviathan A 3 7 6\n";

    assertEquals(expectedOutput, output);
  }

  /**
   * Test rendering after a battle phase where a card is flipped.
   */
  @Test
  public void testRenderAfterBattlePhaseFlipsCard() {
    Player redPlayer = model.getCurrentPlayer();
    Card redCard = redPlayer.getHand().get(0);
    model.placeCard(redPlayer, redCard, 1, 1);

    Player bluePlayer = model.getCurrentPlayer();
    Card blueCard = bluePlayer.getHand().get(0);
    model.placeCard(bluePlayer, blueCard, 1, 0);

    String output = view.render();

    String expectedOutput = "" +
            "Player: RED\n" +
            "___\n" +
            "BR_\n" +
            "___\n" +
            "Hand:\n" +
            "FireDragon 5 3 7 6\n" +
            "LightningEagle 6 4 8 3\n" +
            "EarthGolem 7 5 3 4\n" +
            "OceanLeviathan A 3 7 6\n";

    assertEquals(expectedOutput, output);
  }

  /**
   * Test rendering when the game is over.
   */
  @Test
  public void testRenderGameOverTie() {
    // Simulate both players placing all their cards
    while (!model.isGameOver()) {
      Player currentPlayer = model.getCurrentPlayer();
      List<Card> hand = currentPlayer.getHand();
      if (!hand.isEmpty()) {
        Card cardToPlace = hand.get(0);
        Grid grid = model.getGrid();
        boolean placed = false;
        for (int row = 0; row < grid.getRows(); row++) {
          for (int col = 0; col < grid.getCols(); col++) {
            if (grid.isPlayable(row, col)) {
              model.placeCard(currentPlayer, cardToPlace, row, col);
              placed = true;
              break;
            }
          }
          if (placed) {
            break;
          }
        }
      } else {
        break;
      }
    }

    String output = view.render();

    String expectedOutput = "" +
            "Player: BLUE\n" +
            "RBR\n" +
            "BRB\n" +
            "RBR\n" +
            "Hand:\n" +
            "WaterSerpent 4 6 2 8\n";

    assertEquals(expectedOutput, output);
    assertTrue(model.isGameOver());

    String winner = model.getWinner();
    assertEquals("It's a tie!", winner);
  }

  /**
   * Test attempting to place a card on an occupied cell.
   */
  @Test
  public void testInvalidMove() {
    Player currentPlayer = model.getCurrentPlayer();
    Card cardToPlace = currentPlayer.getHand().get(0);
    model.placeCard(currentPlayer, cardToPlace, 1, 1);

    currentPlayer = model.getCurrentPlayer();
    cardToPlace = currentPlayer.getHand().get(0);

    Player finalCurrentPlayer = currentPlayer;
    Card finalCardToPlace = cardToPlace;
    assertThrows(IllegalArgumentException.class, () -> {
      model.placeCard(finalCurrentPlayer, finalCardToPlace, 1, 1);
    });

  }

  /**
   * Test a full game played with a combo step and a win.
   */
  @Test
  public void testGameOverWithWinAndCombo() {
    gridFile = getResourceFile("grid_configs/grid1.txt");
    cardFile = getResourceFile("card_configs/cards_for_combo.txt");

    random = new Random(42);
    model.initializeGame(gridFile, cardFile, random);

    Player redPlayer = model.getCurrentPlayer();
    Player bluePlayer = model.getOpponentPlayer();

    assertTrue("Red player has insufficient cards for the test",
            redPlayer.getHand().size() >= 5);
    assertTrue("Blue player has insufficient cards for the test",
            bluePlayer.getHand().size() >= 4);

    try {
      // Red: Place CardA at (1, 1)
      Card redCard1 = redPlayer.getHand().get(0);
      model.placeCard(redPlayer, redCard1, 1, 1);

      // Blue: Place CardB at (1, 0)
      Card blueCard1 = bluePlayer.getHand().get(0);
      model.placeCard(bluePlayer, blueCard1, 1, 0);

      // Red: Place CardC at (0, 1)
      Card redCard2 = redPlayer.getHand().get(0);
      model.placeCard(redPlayer, redCard2, 0, 1);

      // Blue: Place CardD at (0, 0)
      Card blueCard2 = bluePlayer.getHand().get(0);
      model.placeCard(bluePlayer, blueCard2, 0, 0);

      // Red: Place CardE at (2, 1)
      Card redCard3 = redPlayer.getHand().get(0);
      model.placeCard(redPlayer, redCard3, 2, 1);

      // Blue: Place CardF at (1, 2)
      Card blueCard3 = bluePlayer.getHand().get(0);
      model.placeCard(bluePlayer, blueCard3, 1, 2);

      // Red: Place CardG at (2, 0)
      Card redCard4 = redPlayer.getHand().get(0);
      model.placeCard(redPlayer, redCard4, 2, 0);

      // Blue: Place CardH at (2, 2)
      Card blueCard4 = bluePlayer.getHand().get(0);
      model.placeCard(bluePlayer, blueCard4, 2, 2);

      // Red: Place CardI at (0, 2)
      Card redCard5 = redPlayer.getHand().get(0);
      model.placeCard(redPlayer, redCard5, 0, 2);

    } catch (IndexOutOfBoundsException e) {
      fail("IndexOutOfBoundsException during card placements, check hand sizes and indices.");
    }

    String output = view.render();
    String expectedOutput = "Player: RED\n" +
            "RRR\n" +
            "RRR\n" +
            "RRR\n" +
            "Hand:\n";
    assertEquals(expectedOutput, output);

    assertTrue(model.isGameOver());
    assertEquals("Red wins!", model.getWinner());
  }

}
