package controller;

import model.*;
import view.ThreeTriosViewInterface;

/**
 * Controller class that delegates between the model and the view for a specific player.
 */
public class ThreeTriosController implements Features, GameController {
  private final GameModel model;
  private final PlayerAction playerAction;
  private final Player playerModel;
  private final ThreeTriosViewInterface ownView;
  private final ThreeTriosViewInterface otherView;
  private Card selectedCard;
  private final PlayerAction opponentPlayerAction;


  /**
   * Constructs a controller for a player.
   *
   * @param model        the shared game model
   * @param playerAction the player's action interface (could be human or AI)
   * @param playerModel  the player in the model
   * @param ownView      the view associated with this player
   * @param otherView    the view of the other player
   */
  public ThreeTriosController(GameModel model, PlayerAction playerAction, Player playerModel,
                              ThreeTriosViewInterface ownView, ThreeTriosViewInterface otherView,
                              PlayerAction opponentPlayerAction) {
    this.model = model;
    this.playerAction = playerAction;
    this.playerModel = playerModel;
    this.ownView = ownView;
    this.otherView = otherView;
    this.opponentPlayerAction = opponentPlayerAction;

    this.ownView.addFeatures(this);
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
      model.placeCard(playerModel, selectedCard, row, col);
      selectedCard = null;
      model.switchTurn();

      ownView.updateView();
      otherView.updateView();

      if (model.isGameOver()) {
        ownView.showGameOverMessage(model.getWinner());
        otherView.showGameOverMessage(model.getWinner());
        return;
      }

      Player currentPlayer = model.getCurrentPlayer();
      if (opponentPlayerAction instanceof AIPlayer &&
              currentPlayer.equals(model.getPlayerBlue())) {
        opponentPlayerAction.takeTurn(model);
        model.switchTurn();
        ownView.updateView();
        otherView.updateView();
        if (model.isGameOver()) {
          ownView.showGameOverMessage(model.getWinner());
          otherView.showGameOverMessage(model.getWinner());
        }
      }
    } catch (IllegalArgumentException e) {
      ownView.showErrorMessage("Invalid move: " + e.getMessage());
    } catch (IllegalStateException e) {
      ownView.showErrorMessage("Game state error: " + e.getMessage());
    }
  }

  private void notifyNextPlayer() {
    Player currentPlayer = model.getCurrentPlayer();
    if (currentPlayer.equals(playerModel)) {
      playerAction.takeTurn(model);

      if (playerAction instanceof AIPlayer && model.getCurrentPlayer().equals(playerModel)) {
        notifyNextPlayer(); // make sure infinite loops prevented
      } else {
        ownView.updateView();
        otherView.updateView();
        if (model.isGameOver()) {
          ownView.showGameOverMessage(model.getWinner());
          otherView.showGameOverMessage(model.getWinner());
        }
      }
    }
  }
}
