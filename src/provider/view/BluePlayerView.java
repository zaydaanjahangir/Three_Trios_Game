package provider.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


import provider.controller.ThreeTriosController;
import provider.model.GameCard;
import provider.model.Player;
import provider.model.PlayerColor;
import provider.model.ReadOnlyThreeTriosModel;

/**
 * Displays the blue player's view and updates based on either players game clicks.
 */
public class BluePlayerView extends ThreeTriosGraphicsView {

  private ReadOnlyThreeTriosModel model;
  private JButton[][] buttonGrid;
  private ThreeTriosController controller;

  /**
   * Constructs a ThreeTriosGraphicsView object.
   *
   * @param model the ReadOnlyThreeTriosModel to get information about the game state from
   */
  public BluePlayerView(ReadOnlyThreeTriosModel model) {

    super(model);
    this.model = model;
    this.setTitle("Blue Player View");
    this.setSize(1000, 600);
    this.setMinimumSize(new Dimension(700, 600));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    // Red Player Panel -> can't touch or select these cards
    this.leftPanel = new HandPanel(model.currentTurn().color(), PlayerColor.RED,
            model.returnPlayerHand(PlayerColor.RED));
    this.add(leftPanel, BorderLayout.WEST);

    // Blue Player Panel
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
   * Updates the view to reflect the new changes updated.
   */
  public void reflectNewChanges() {
    System.out.println("BluePlayerViewReflectNewChanges Called!");
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
    controller = listener;
    this.rightPanel.addClickListener(listener);
    this.gridPanel.addClickListener(listener);
  }

  /**
   * Determines if the grid cna be selected yet if a card has been selected from the hand.
   */
  public void setCanSelectGrid() {
    this.gridPanel.canSelectGrid();
  }

  /**
   * Changes the turn and states that its not their turn.
   */
  public void notYourTurn() {
    this.rightPanel.swapTurn();
  }

  @Override
  public void listenAgain() {
    addClickListener(controller);
  }

}
