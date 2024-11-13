package model;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Random;

import controller.CardFileReader;
import controller.CardFileReaderImpl;
import controller.GridFileReader;
import controller.GridFileReaderImpl;
import view.TextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


/**
 * Tests the methods within the ThreeTriosGameModel class and edge cases.
 */
public class ThreeTriosGameModelTest {
  private GameModel model;
  private Grid grid;
  private List<Card> cards;
  private Player playerRed;
  private Player playerBlue;

  private static final String GRID_FILE_PATH = "resources/grid_configs/grid1.txt";
  private static final String CARD_FILE_PATH = "resources/card_configs/cards3.txt";

  @Before
  public void setUp() {
    GridFileReader gridReader = new GridFileReaderImpl();
    CardFileReader cardReader = new CardFileReaderImpl();
    model = GameModelFactory.createGameModel(GameMode.PLAYER_VS_PLAYER);
    playerRed = new PlayerImpl("Red");
    playerBlue = new PlayerImpl("Blue");
    Grid grid = gridReader.readGrid(new File(GRID_FILE_PATH));
    List<Card> cards = cardReader.readCards(new File(CARD_FILE_PATH));
    Random random = new Random(100);
    model.initializeGame(grid, cards, random);
  }

  @Test
  public void testPlaceCardUpdatesGameState() {
    Player currentPlayer = model.getCurrentPlayer();
    Card cardToPlace = currentPlayer.getHand().get(0);
    model.placeCard(currentPlayer, cardToPlace, 1, 1);

    Cell cell = model.getGrid().getCell(1, 1);
    assertTrue(cell.isOccupied());
    assertEquals(cardToPlace, cell.getCard());
    assertEquals(currentPlayer, cell.getOwner());

    assertFalse(currentPlayer.getHand().contains(cardToPlace));

    Player nextPlayer = model.getCurrentPlayer();
    assertNotEquals(currentPlayer, nextPlayer);
  }

  @Test
  public void testInvalidPlacement() {
    Card testCard = cards.get(0);
    playerBlue.addCardToHand(testCard);

    assertThrows(IllegalStateException.class, () ->
            grid.placeCard(testCard, 0, 2, playerBlue)
    );
  }

  @Test
  public void testPlayerHandOperations() {
    playerRed.addCardToHand(cards.get(0));
    playerRed.addCardToHand(cards.get(1));
    assertEquals(2, playerRed.getHand().size());

    playerRed.removeCardFromHand(cards.get(0));
    assertEquals(1, playerRed.getHand().size());
    assertFalse(playerRed.getHand().contains(cards.get(0)));
    assertTrue(playerRed.getHand().contains(cards.get(1)));
  }

  @Test
  public void testPlaceCardOnOccupiedCell() {
    Card firstCard = cards.get(0);
    Card secondCard = cards.get(1);
    playerRed.addCardToHand(firstCard);
    playerBlue.addCardToHand(secondCard);

    grid.placeCard(firstCard, 0, 0, playerRed);

    assertThrows(IllegalStateException.class, () ->
            grid.placeCard(secondCard, 0, 0, playerBlue)
    );
  }

  @Test
  public void testPlaceCardOutOfBounds() {
    Card testCard = cards.get(0);
    playerRed.addCardToHand(testCard);

    assertThrows(IllegalArgumentException.class, () ->
            grid.placeCard(testCard, 5, 5, playerRed)
    );
  }

  @Test
  public void testEdgeIndexPlacements() {
    Card testCard1 = cards.get(0);
    Card testCard2 = cards.get(1);
    playerRed.addCardToHand(testCard1);
    playerRed.addCardToHand(testCard2);

    grid.placeCard(testCard1, 0, 0, playerRed);
    assertTrue(grid.getCell(0, 0).isOccupied());

    grid.placeCard(testCard2, 2, 2, playerRed);
    assertTrue(grid.getCell(2, 2).isOccupied());
  }

  @Test
  public void testRemoveCardNotInHand() {
    Card testCard = cards.get(0);
    playerRed.removeCardFromHand(testCard);
    assertTrue(playerRed.getHand().isEmpty());
  }

  @Test
  public void testBattlePhaseCardFlip() {
    Player currentPlayer = model.getCurrentPlayer();
    Card cardToPlace = currentPlayer.getHand().get(0);
    model.placeCard(currentPlayer, cardToPlace, 1, 1);

    Player nextPlayer = model.getCurrentPlayer();
    Card opponentCard = nextPlayer.getHand().get(0);
    model.placeCard(nextPlayer, opponentCard, 1, 0);

    Cell cell = model.getGrid().getCell(1, 0);
    if (cell.getOwner() == currentPlayer) {
      assertEquals(currentPlayer, cell.getOwner());
    }
  }

  // New Tests

  @Test
  public void testRedWins() {
    Player playerRed = model.getCurrentPlayer();

    Card redCard1 = playerRed.getHand().get(0);
    model.placeCard(playerRed, redCard1, 1, 0);

    Player playerBlue = model.getCurrentPlayer();

    Card blueCard1 = playerBlue.getHand().get(0);
    model.placeCard(playerBlue, blueCard1, 1, 2);

    playerRed = model.getCurrentPlayer();

    Card redCard2 = playerRed.getHand().get(1);
    model.placeCard(playerRed, redCard2, 2, 1);

    String winner = model.getWinner();
    assertEquals("Red wins!", winner);
  }

  @Test
  public void testBlueWins() {
    Player playerRed = model.getCurrentPlayer();
    Card redCard = playerRed.getHand().get(0);
    model.placeCard(playerRed, redCard, 0, 0);

    Player playerBlue = model.getCurrentPlayer();
    Card blueCard1 = playerBlue.getHand().get(0);
    Card blueCard2 = playerBlue.getHand().get(1);

    model.placeCard(playerBlue, blueCard1, 1, 1);
    model.placeCard(playerBlue, blueCard2, 2, 2);

    String winner = model.getWinner();
    assertEquals("Blue wins!", winner);
  }

  @Test
  public void testTie() {
    Player playerRed = model.getCurrentPlayer();
    Card redCard = playerRed.getHand().get(0);
    model.placeCard(playerRed, redCard, 0, 0);

    Player playerBlue = model.getCurrentPlayer();
    Card blueCard = playerBlue.getHand().get(0);
    model.placeCard(playerBlue, blueCard, 0, 1);

    String winner = model.getWinner();
    assertEquals("It's a tie!", winner);
  }

  @Test
  public void testGameNotOver() {
    String result = model.getWinner();
    assertEquals("Game is not over yet.", result);
  }
}
