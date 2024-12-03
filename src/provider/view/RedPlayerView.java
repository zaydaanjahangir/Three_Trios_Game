package provider.view;


import java.awt.*;
import java.util.List;

import javax.swing.*;

import provider.controller.ThreeTriosController;
import provider.model.GameCard;
import provider.model.Player;
import provider.model.PlayerColor;
import provider.model.ReadOnlyThreeTriosModel;


/**
 * Displays the red player's view and updates based on either players game clicks.
 */
public class RedPlayerView extends ThreeTriosGraphicsView {

  private ReadOnlyThreeTriosModel model;
  private JButton[][] buttonGrid;

  /**
   * Constructs a ThreeTriosGraphicsView object.
   *
   * @param model the ReadOnlyThreeTriosModel to get information about the game state from
   */
  public RedPlayerView(ReadOnlyThreeTriosModel model) {

    super(model);
    this.model = model;
    this.setTitle("Current Player: " + model.currentTurn());
    this.setSize(1000, 600);
    this.setMinimumSize(new Dimension(700, 600));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    // Red Player Panel
    this.leftPanel = new HandPanel(model.currentTurn().color(), PlayerColor.RED,
            model.returnPlayerHand(PlayerColor.RED));
    this.add(leftPanel, BorderLayout.WEST);

    // Blue Player Panel -> can't touch or select these cards
    this.rightPanel = new HandPanel(model.currentTurn().color(), PlayerColor.BLUE,
            model.returnPlayerHand(PlayerColor.BLUE));
    this.add(rightPanel, BorderLayout.EAST);


    // Grid Panel -> need to select a red card first before selecting a grid space
    this.gridPanel = new GridPanel(model.numRows(), model.numColumns(), model);
    this.add(gridPanel, BorderLayout.CENTER);

    this.buttonGrid = gridPanel.getButtonGrid();

    this.pack();

    // Make the frame visible.
    this.setVisible(true);

  }

  @Override
  public void displayGameOver(Player winner) {
    JOptionPane.showMessageDialog(this, "Game Over! Winner: " +
                    winner.color(),
            "Game Over", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void updatePlayerHand(Player player) {
    // Get the updated hand of the player
    List<GameCard> updatedHand = player.retrieveHand();

    // Update the left (or right) hand panel with the new set of cards
    // Assuming `leftPanel` is the panel displaying the red player's hand (adjust if necessary)
    // Update the red player's hand panel with the new hand
    this.leftPanel.updateHand(updatedHand);

    // this.buttonGrid = gridPanel.getButtonGrid();
    this.leftPanel.revalidate();
    this.leftPanel.repaint();

    this.rightPanel.updateHand(updatedHand);

    // this.buttonGrid = gridPanel.getButtonGrid();
    this.rightPanel.revalidate();
    this.rightPanel.repaint();

    this.revalidate();
    this.repaint();
  }

  /**
   * Reflects the new changes in the view when a card has been played.
   */
  public void reflectNewChanges() {
    System.out.println("RedPlayerViewReflectNewChanges Called!");
    // Update the grid based on the model.
    for (int row = 0; row < model.numRows(); row++) {
      for (int col = 0; col < model.numColumns(); col++) {
        GameCard card = model.returnGridCopy().returnCell(row, col).returnFilledCellCard();
        if (card != null) {
          updateGrid(row, col, card);
        }
      }
    }
    repaint();
    this.rightPanel.repaint();
    this.leftPanel.repaint();
    this.gridPanel.repaint();
  }

  @Override
  public void updateGrid(int row, int col, GameCard card) {
    if (buttonGrid == null) {
      throw new IllegalStateException("Button grid hasn't been properly initialized yet.");
    }

    this.gridPanel.updateGridStatus(card, row, col);

    this.gridPanel.revalidate();
    this.repaint();
  }

  @Override
  public void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(this, message,
            "Invalid Move", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void addClickListener(ThreeTriosController listener) {
    this.leftPanel.addClickListener(listener);
    this.gridPanel.addClickListener(listener);
  }

  public void setCanSelectGrid() {
    this.gridPanel.canSelectGrid();
  }

  public void notYourTurn() {
    this.leftPanel.swapTurn();
  }

  public void listenAgain() {
    this.leftPanel.listenAgain();
  }
}
