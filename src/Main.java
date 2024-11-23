import controller.*;
import model.*;
import strategy.CornerStrategy;
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
  /**
   * Main method to run the game and can be played against PvP or PvCPU
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      // error handling for wrong arguments
      System.out.println("Usage: java Main <player1Type> <player2Type>");
      System.out.println("playerType options: human, flipmaxStrategy, cornerStrategy");
      System.exit(1);
    }

    String gridConfigPath = "resources/grid_configs/grid1.txt";
    String cardConfigPath = "resources/card_configs/cards3.txt";
    Random random = new Random();
    GridFileReader gridReader = new GridFileReaderImpl();
    Grid grid = gridReader.readGrid(new File(gridConfigPath));
    CardFileReader cardReader = new CardFileReaderImpl();
    List<Card> cards = cardReader.readCards(new File(cardConfigPath));

    GameModel model = new ThreeTriosGameModel();
    PlayerAction playerRed = createPlayer("Red", args[0]);
    PlayerAction playerBlue = createPlayer("Blue", args[1]);

    model.setPlayers((Player) playerRed, (Player) playerBlue);
    model.initializeGame(grid, cards, random);

    ThreeTriosViewInterface viewRed = new ThreeTriosView(model, (Player) playerRed);
    ThreeTriosViewInterface viewBlue = new ThreeTriosView(model, (Player) playerBlue);
    ThreeTriosController controllerRed = new ThreeTriosController(
            model, playerRed, (Player) playerRed, viewRed, viewBlue, (PlayerAction) playerBlue);

    ThreeTriosController controllerBlue = new ThreeTriosController(
            model, playerBlue, (Player) playerBlue, viewBlue, viewRed, (PlayerAction) playerRed);

    model.startGame();
    viewRed.setVisible(true);
    viewBlue.setVisible(true);
  }

  private static PlayerAction createPlayer(String color, String playerType) {
    switch (playerType.toLowerCase()) {
      case "human":
        return new PlayerImpl(color);
      case "flipmaxstrategy":
        return new AIPlayer(color, new FlipMaxStrategy());
      case "cornerstrategy":
        return new AIPlayer(color, new CornerStrategy());
      default:
        System.out.println("Invalid player type: " + playerType);
        System.out.println("Valid options: human, flipmaxStrategy, cornerStrategy");
        System.exit(1);
        return null;
    }
  }
}
