import controller.ThreeTriosController;
import model.AIPlayer;
import model.Player;
import model.PlayerImpl;
import strategy.FlipMaxStrategy;
import strategy.MoveStrategy;

import java.util.Random;

/**
 * Main Class to start playing the game outside of the testing suite.
 */
public class Main {

  /**
   * Main method which starts everything.
   *
   * @param args args
   */
  public static void main(String[] args) {
    // Paths to your configuration files
    String gridConfigPath = "resources/grid_configs/grid1.txt";
    String cardConfigPath = "resources/card_configs/cards3.txt";
    Random random = new Random();

    // Create the controller, which initializes the model and starts the view
    ThreeTriosController controller = new ThreeTriosController(gridConfigPath, cardConfigPath,
            random);

    Player playerRed = new PlayerImpl("Red");
    MoveStrategy aiStrategy = new FlipMaxStrategy();
    Player playerBlue = new AIPlayer("Blue", aiStrategy);
  }


}
