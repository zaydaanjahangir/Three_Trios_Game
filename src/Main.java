import controller.ThreeTriosController;
import model.AIPlayer;
import model.PlayerImpl;
import strategy.FlipMaxStrategy;

import java.util.Random;

public class Main {
  public static void main(String[] args) {
    // Paths to configuration files
    String gridConfigPath = "resources/grid_configs/grid1.txt";
    String cardConfigPath = "resources/card_configs/cards3.txt";
    Random random = new Random();

    // Create human and AI players
    PlayerImpl playerRed = new PlayerImpl("Red");
    AIPlayer playerBlue = new AIPlayer("Blue", new FlipMaxStrategy());

    // Create and start the controller
    ThreeTriosController controller = new ThreeTriosController(
            gridConfigPath, cardConfigPath, playerRed, playerBlue, random);
    controller.startGame();
  }
}
