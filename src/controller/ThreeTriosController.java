package controller;

import model.GameModel;
import model.GameModelFactory;
import model.GameMode;
import model.Grid;
import model.Card;
import view.ThreeTriosView;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Controller class which delegates the functions between the model and the view.
 */
public class ThreeTriosController {

  /**
   * Constructor for the controller which initializes the game and starts the view.
   *
   * @param gridConfigPath path to grid configuration file
   * @param cardConfigPath path to card configuration file
   * @param random         choice for a random seed, if null deck is not shuffled
   */
  public ThreeTriosController(String gridConfigPath, String cardConfigPath, Random random) {
    GridFileReader gridReader = new GridFileReaderImpl();
    Grid grid = gridReader.readGrid(new File(gridConfigPath));

    CardFileReader cardReader = new CardFileReaderImpl();
    List<Card> cards = cardReader.readCards(new File(cardConfigPath));

    GameModel model = GameModelFactory.createGameModel(GameMode.PLAYER_VS_PLAYER);
    model.initializeGame(grid, cards, random);
    ThreeTriosView view = new ThreeTriosView(model);
    view.setVisible(true);
  }
}
