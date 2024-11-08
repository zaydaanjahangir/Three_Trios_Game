import controller.ThreeTriosController;
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
  }
}
}
