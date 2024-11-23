package controller;

import model.*;
import view.ThreeTriosViewInterface;

/**
 * Controller class that mediates between the model and the view for a specific player.
 */
public class ThreeTriosController implements Features {
  private final GameModel model;
  private final PlayerAction playerAction;
  private final Player playerModel;
  private final ThreeTriosViewInterface ownView;
  private final ThreeTriosViewInterface otherView;
  private Card selectedCard;

  /**
   * Constructs a controller for a player.
   *
   * @param model       the shared game model
   * @param playerAction the player's action interface (could be human or AI)
   * @param playerModel  the player in the model
   * @param ownView      the view associated with this player
   * @param otherView    the view of the other player
   */
  public ThreeTriosController(GameModel model, PlayerAction playerAction, Player playerModel,
                              ThreeTriosViewInterface ownView, ThreeTriosViewInterface otherView) {
    this.model = model;
    this.playerAction = playerAction;
    this.playerModel = playerModel;
    this.ownView = ownView;
    this.otherView = otherView;

    this.ownView.addFeatures(this);

    // Set features in the player if necessary
    if (playerAction instanceof PlayerImpl) {
      ((PlayerImpl) playerAction).setFeatures(this);
    } else if (playerAction instanceof AIPlayer) {
      ((AIPlayer) playerAction).setFeatures(this);
    }
  }

  @Override
  public void cardSelected(Card card) {
    if (!model.getCurrentPlayer().equals(playerModel)) {
      ownView.showErrorMessage("It's not your turn!");
      return;
    }

    if (!playerModel.getHand().contains(card)) {
      ownView.showErrorMessage("You don't have that card.");
      return;
    }

    selectedCard = card;
  }

  @Override
  public void cellSelected(int row, int col) {
    if (!model.getCurrentPlayer().equals(playerModel)) {
      ownView.showErrorMessage("It's not your turn!");
      return;
    }

    if (selectedCard == null) {
      ownView.showErrorMessage("No card selected.");
      return;
    }

    try {
      // Place the card on the grid
      model.placeCard(playerModel, selectedCard, row, col);
      selectedCard = null;

      // Switch turn in the model
      model.switchTurn();

      // Update both views to reflect the new state
      ownView.updateView();
      otherView.updateView();

      // Check if the game is over
      if (model.isGameOver()) {
        ownView.showGameOverMessage(model.getWinner());
        otherView.showGameOverMessage(model.getWinner());
      } else {
        // Notify the next player (if applicable)
        notifyNextPlayer();
      }
    } catch (Exception e) {
      ownView.showErrorMessage("Error placing card: " + e.getMessage());
    }
  }


  private void notifyNextPlayer() {
    Player currentPlayer = model.getCurrentPlayer();
    if (currentPlayer.equals(playerModel)) {
      playerAction.takeTurn(model);
    }
  }


}
