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

public class ThreeTriosController {
  private final GameModel model;
  private final ThreeTriosView view;

  public ThreeTriosController(String gridConfigPath, String cardConfigPath, Random random) {
    GridFileReader gridReader = new GridFileReaderImpl();
    Grid grid = gridReader.readGrid(new File(gridConfigPath));

    CardFileReader cardReader = new CardFileReaderImpl();
    List<Card> cards = cardReader.readCards(new File(cardConfigPath));

    this.model = GameModelFactory.createGameModel(GameMode.PLAYER_VS_PLAYER);
    model.initializeGame(grid, cards, random);

    // Create the view, passing in the read-only model
    this.view = new ThreeTriosView(model);

    // Start the view (e.g., make the GUI visible)
    view.setVisible(true);
  }

  // Future methods for handling user inputs and game logic will be added here
}
