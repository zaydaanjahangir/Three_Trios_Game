import model.GameMode;
import model.GameModel;
import model.GameModelFactory;
import view.TextView;

import java.io.File;
import java.util.Random;

/**
 * Main Class to start playing the game outside of the testing suite.
 */
public class Main {

  /**
   * Main method.
   *
   * @param args .
   */
  public static void main(String[] args) {
    Random random = new Random();
    File cardConfig = new File("resources/card_configs/cards3.txt");
    File gridConfig = new File("resources/grid_configs/grid3.txt");

    GameModel model = GameModelFactory.createGameModel(GameMode.PLAYER_VS_PLAYER);
    model.initializeGame(gridConfig, cardConfig, random);

    TextView view = new TextView(model);
    System.out.println(view.render());
  }
}
