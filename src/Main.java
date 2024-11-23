import controller.ThreeTriosController;
import model.AIPlayer;
import model.PlayerAction;
import model.PlayerImpl;
import strategy.CornerStrategy;
import strategy.FlipMaxStrategy;

import java.util.Random;

public class Main {
  public static void main(String[] args) {
    String player1Type = args.length > 0 ? args[0] : "human";
    String player2Type = args.length > 1 ? args[1] : "human";

    // Paths to configuration files
    String gridConfigPath = "resources/grid_configs/grid1.txt";
    String cardConfigPath = "resources/card_configs/cards3.txt";
    Random random = new Random();

    // Create players based on arguments
    PlayerAction player1 = createPlayer("Red", player1Type);
    PlayerAction player2 = createPlayer("Blue", player2Type);

    // Create and start the controller
    ThreeTriosController controller = new ThreeTriosController(
            gridConfigPath, cardConfigPath, player1, player2, random);
    controller.startGame();
  }

  private static PlayerAction createPlayer(String color, String type) {
    if (type.equalsIgnoreCase("human")) {
      return new PlayerImpl(color);
    } else if (type.equalsIgnoreCase("strategy1")) {
      return new AIPlayer(color, new FlipMaxStrategy());
    } else if (type.equalsIgnoreCase("strategy2")) {
      return new AIPlayer(color, new CornerStrategy());
    } else {
      throw new IllegalArgumentException("Unknown player type: " + type);
    }
  }
}

