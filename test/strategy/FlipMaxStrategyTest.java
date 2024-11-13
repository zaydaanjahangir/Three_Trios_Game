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
  private FlipMaxStrategy strategy;
  private static final String GRID_FILE_PATH = "resources/grid_configs/grid1.txt";
  private static final String CARD_FILE_PATH = "resources/card_configs/cards3.txt";

  @Before
  public void setUp() {
    // Initialize the strategy to be tested
    strategy = new FlipMaxStrategy();

    // Load grid and card data from files using the file readers
    GridFileReader gridReader = new GridFileReaderImpl();
    CardFileReader cardReader = new CardFileReaderImpl();

    // Read grid and cards from their respective files
    Grid grid = gridReader.readGrid(new File(GRID_FILE_PATH));
    List<Card> cards = cardReader.readCards(new File(CARD_FILE_PATH));

    // Initialize the model with the grid, cards, and a Random instance
    GameModel gameModel = GameModelFactory.createGameModel(GameMode.PLAYER_VS_PLAYER);
    gameModel.initializeGame(grid, cards, new Random());

    // Create the AI player, give them a hand of cards, and set the model to read-only
    aiPlayer = new AIPlayer("Red", strategy);
    aiPlayer.addCardToHand(cards.get(0)); // For example, add first card from list to AI's hand
    aiPlayer.addCardToHand(cards.get(1)); // Add second card from list to AI's hand

    // Cast the GameModel to a ReadonlyThreeTriosModel for testing
    model = (ReadOnlyGameModel) gameModel;
  }

  @Test
  public void testDetermineMove() {
    // Verify that the AI can determine a valid move
    Move move = strategy.determineMove(model, aiPlayer);
    assertNotNull("AI should determine a move", move);

    // Additional assertions can be added here to validate expected move characteristics
    assertTrue("Move row is within grid bounds", move.getRow() >= 0 && move.getRow() < model.getGridRows());
    assertTrue("Move column is within grid bounds", move.getCol() >= 0 && move.getCol() < model.getGridCols());
    assertTrue("Move card is in AI's hand", aiPlayer.getHand().contains(move.getCard()));
  }

  @Test
  public void testStrategyWithMockModel() {
    // Mock model for isolated strategy testing
    ReadOnlyGameModel mockModel = new MockThreeTriosModel();
    AIPlayer aiPlayer = new AIPlayer("Red", strategy);
    Move move = strategy.determineMove(mockModel, aiPlayer);

    // Expected values based on mock data for validating correct move selection
    int expectedRow = 1;
    int expectedCol = 1;
    Card expectedCard = aiPlayer.getHand().get(0);

    // Validate that the strategy selected the expected move based on the mock model's setup
    assertEquals("Expected row for move", expectedRow, move.getRow());
    assertEquals("Expected column for move", expectedCol, move.getCol());
    assertEquals("Expected card to be played", expectedCard, move.getCard());
  }
}
