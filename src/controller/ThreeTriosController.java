package controller;

import java.io.File;
import java.util.List;
import java.util.Random;

import model.*;
import view.ThreeTriosView;
import view.ThreeTriosViewInterface;

public class ThreeTriosController implements Features {
  private final GameModel model;
  private final ThreeTriosViewInterface view;
  private final PlayerAction player1;
  private final PlayerAction player2;

  /**
   * Constructor for the controller.
   * Reads files, initializes the model, and connects the view to the controller.
   *
   * @param gridConfigPath path to the grid configuration file
   * @param cardConfigPath path to the card configuration file
   * @param player1        the first player (human or AI)
   * @param player2        the second player (human or AI)
   * @param random         optional random seed for shuffling
   */
  public ThreeTriosController(String gridConfigPath, String cardConfigPath,
                              PlayerAction player1, PlayerAction player2, Random random) {
    // File reading and model initialization
    GridFileReader gridReader = new GridFileReaderImpl();
    Grid grid = gridReader.readGrid(new File(gridConfigPath));

    CardFileReader cardReader = new CardFileReaderImpl();
    List<Card> cards = cardReader.readCards(new File(cardConfigPath));

    this.model = GameModelFactory.createGameModel(GameMode.PLAYER_VS_PLAYER);
    this.model.initializeGame(grid, cards, random);

    // Setting up the players
    this.player1 = player1;
    this.player2 = player2;

    // Pass the model and controller as features to the view
    this.view = new ThreeTriosView(this.model);
    this.view.addFeatures(this);
  }

  /**
   * Starts the game and notifies the first player.
   */
  public void startGame() {
    view.updateView(); // Ensure the initial state is shown
    notifyPlayerTurn();
  }

  /**
   * Notifies the current player (human or AI) to take their turn.
   */
  private void notifyPlayerTurn() {
    Player currentPlayer = model.getCurrentPlayer();

    if (currentPlayer.equals(((Player) player1))) {
      player1.takeTurn(model);
    } else if (currentPlayer.equals(((Player) player2))) {
      player2.takeTurn(model);
    }
  }

  @Override
  public void cardSelected(Card card) {
    try {
      Player currentPlayer = model.getCurrentPlayer();
      currentPlayer.removeCardFromHand(card);
    } catch (Exception e) {
      view.showErrorMessage("Error selecting card: " + e.getMessage());
    }
  }

  @Override
  public void cellSelected(int row, int col) {
    try {
      Player currentPlayer = model.getCurrentPlayer();
      if (currentPlayer.getHand().isEmpty()) {
        view.showErrorMessage("No card selected. Select a card first.");
        return;
      }

      Card selectedCard = currentPlayer.getHand().get(0); // Assuming only one selected
      model.placeCard(currentPlayer, selectedCard, row, col);

      view.updateView(); // Refresh the view

      if (model.isGameOver()) {
        view.showGameOverMessage(model.getWinner());
      } else {
        notifyPlayerTurn(); // Notify the next player
      }
    } catch (Exception e) {
      view.showErrorMessage("Error placing card: " + e.getMessage());
    }
  }
}
