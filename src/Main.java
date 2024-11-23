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
   * @param args args
   */
  public static void main(String[] args) {
    String gridConfigPath = "resources/grid_configs/grid1.txt";
    String cardConfigPath = "resources/card_configs/cards3.txt";
    Random random = new Random();

    GridFileReader gridReader = new GridFileReaderImpl();
    Grid grid = gridReader.readGrid(new File(gridConfigPath));

    CardFileReader cardReader = new CardFileReaderImpl();
    List<Card> cards = cardReader.readCards(new File(cardConfigPath));

    GameModel model = new ThreeTriosGameModel();

    PlayerImpl playerRed = new PlayerImpl("Red");
    AIPlayer playerBlue = new AIPlayer("Blue", new CornerStrategy());
    model.setPlayers(playerRed, playerBlue);
    model.initializeGame(grid, cards, random);

    ThreeTriosViewInterface viewRed = new ThreeTriosView(model, playerRed);
    ThreeTriosViewInterface viewBlue = new ThreeTriosView(model, playerBlue);

    ThreeTriosController controllerRed = new ThreeTriosController(
            model, playerRed, playerRed, viewRed, viewBlue, playerBlue);

    ThreeTriosController controllerBlue = new ThreeTriosController(
            model, playerBlue, playerBlue, viewBlue, viewRed, playerRed);

    model.startGame();
    viewRed.setVisible(true);
    viewBlue.setVisible(true);
  }
}
