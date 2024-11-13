import controller.ThreeTriosController;
import model.AIPlayer;
import model.GameModel;
import model.Player;
import model.PlayerImpl;
import model.ThreeTriosGameModel;
import strategy.FlipMaxStrategy;
import strategy.MoveStrategy;

import java.util.Random;

/**
 * Main Class to start playing the game outside of the testing suite.
 */
public class Main {
  public static void main(String[] args) {
    // Paths to your configuration files
    String gridConfigPath = "resources/grid_configs/grid1.txt";
    String cardConfigPath = "resources/card_configs/cards3.txt";
    Random random = new Random();

    // Create the controller, which initializes the model and starts the view
    ThreeTriosController controller = new ThreeTriosController(gridConfigPath, cardConfigPath, random);
    // In your controller or main method

    // Create players
    Player playerRed = new PlayerImpl("Red"); // Human player
    MoveStrategy aiStrategy = new FlipMaxStrategy();
    Player playerBlue = new AIPlayer("Blue", aiStrategy); // AI player

    // Initialize the model
//    GameModel model = new ThreeTriosGameModel(playerRed, playerBlue);
//    model.initializeGame(grid, cards, random);

  }


}
