import controller.*;
import model.*;
import strategy.FlipMaxStrategy;
import view.ThreeTriosView;
import view.ThreeTriosViewInterface;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Main class to start the Three Trios game.
 */
public class Main {
  public static void main(String[] args) {
    // Paths to configuration files
    String gridConfigPath = "resources/grid_configs/grid1.txt";
    String cardConfigPath = "resources/card_configs/cards3.txt";
    Random random = new Random();

    // Read grid and cards
    GridFileReader gridReader = new GridFileReaderImpl();
    Grid grid = gridReader.readGrid(new File(gridConfigPath));

    CardFileReader cardReader = new CardFileReaderImpl();
    List<Card> cards = cardReader.readCards(new File(cardConfigPath));

    // Create the shared model
    GameModel model = new ThreeTriosGameModel();

    // Create players
    PlayerImpl playerRed = new PlayerImpl("Red");
    AIPlayer playerBlue = new AIPlayer("Blue", new FlipMaxStrategy()); // AI with FlipMaxStrategy
    model.setPlayers(playerRed, playerBlue);


    // Initialize the game
    model.initializeGame(grid, cards, random);

    // Create views
    ThreeTriosViewInterface viewRed = new ThreeTriosView(model, playerRed);
    ThreeTriosViewInterface viewBlue = new ThreeTriosView(model, playerBlue);

    // Create controllers
    ThreeTriosController controllerRed = new ThreeTriosController(
            model, playerRed, playerRed, viewRed, viewBlue, playerBlue);

    ThreeTriosController controllerBlue = new ThreeTriosController(
            model, playerBlue, playerBlue, viewBlue, viewRed, playerRed);

    // Start the game
    model.startGame();

    // Set views visible
    viewRed.setVisible(true);
    viewBlue.setVisible(true);
  }
}
