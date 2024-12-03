package provider.view;

import java.awt.*;

import javax.swing.*;

import provider.controller.ThreeTriosController;
import provider.model.Cell;
import provider.model.GameCard;
import provider.model.Grid;
import provider.model.PlayerColor;
import provider.model.ReadOnlyThreeTriosModel;

/**
 * GridPanel represents the center game board of Three Trios which can be interacted with
 * to play the game. The game state of the grid should be drawn here. A GridPanel contains
 * a 2D array of JButtons which can be CardButtons (filled card cells), EmptyCardCellButtons,
 * and HoleButtons.
 */
public class GridPanel extends JPanel {

  private ThreeTriosController controller;
  private final ReadOnlyThreeTriosModel model;
  private final JButton[][] buttonGrid;
  private final int rows;
  private final int cols;
  private boolean canSelectGrid;


  /**
   * Constructs a GridPanel object.
   * @param row the integer number of rows for the grid
   * @param col the integer number of columns for the grid
   * @param model the ReadOnlyThreeTriosModel to reference information from
   */
  public GridPanel(int row, int col, ReadOnlyThreeTriosModel model) {
    super(new GridLayout(row, col, 0, 0));
    this.model = model;
    this.buttonGrid = new JButton[row][col];
    this.controller = null;
    this.rows = row;
    this.cols = col;
    Grid gridCopy = model.returnGridCopy();
    this.canSelectGrid = false;
  }

  public JButton[][] getButtonGrid() {
    return buttonGrid;
  }

  private void handleGridSelection(int row, int col) {
    buttonGrid[row][col].setEnabled(this.canSelectGrid);
    System.out.println("Grid was clicked at " + row + ", " + col);
    // Deselect all buttons first
    for (int i = 0; i < buttonGrid.length; i++) {
      for (int j = 0; j < buttonGrid[i].length; j++) {
        JButton button = buttonGrid[i][j];
        if (button instanceof CardButton) {
          ((CardButton) button).deselect();
        }
      }
    }
    // Now, select the clicked button
    JButton selectedButton = buttonGrid[row][col];
    if (selectedButton instanceof CardButton) {
      ((CardButton) selectedButton).updateSelectionEmptyCard();
    }

    // Signal to the controller that the cell was selected
    System.out.println(controller != null);
    if (controller != null) {
      System.out.println("Controller should handle the click now.");
      controller.handleCellClick(row, col);
    }
  }


  /**
   * Updates the grid status of the grid.
   * @param card the card being placed
   * @param row the given row where the card is placed
   * @param col the given column where the card is placed.
   */

  public void updateGridStatus(GameCard card, int row, int col) {
    JButton gridButton = buttonGrid[row][col];

    Grid gridCopy = model.returnGridCopy();
    Cell cell = gridCopy.returnCell(row, col);
    /*
    if (!cell.isFilledCellCardCell() && !cell.isCellHoleCell()) {
      controller.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
              "Place Card"));
    }*/

    // Find the button at the specified row and column in the grid

    // Update the button in the grid to show the card placed there
    if (gridButton instanceof CardButton) {
      CardButton cardButton = (CardButton) gridButton;
      cardButton.updateCard(card);
    }
    //gridButton.setEnabled(false); // Disable the button to indicate the cell is occupied

    // need to reassign the current cell as a cardButton so that it can display the card
  }

  /**
   * Adds the listener to the features of this model.
   * @param listener the asynchronous listener for this panel.
   */
  public void addClickListener(ThreeTriosController listener) {
    controller = listener;
    System.out.print("Controller has been added as a click listener.");
    // Now that the controller has been added, the action listeners can be set.
    if (!this.canSelectGrid) {
      for (int gridRows = 0; gridRows < rows; gridRows++) {
        for (int gridCols = 0; gridCols < cols; gridCols++) {
          Cell currentCell = model.returnGridCopy().returnCell(gridRows, gridCols);

          // HOLE CELL
          if (currentCell.isCellHoleCell()) {
            buttonGrid[gridRows][gridCols] = new HoleButton();
            //buttonGrid[gridRows][gridCols].setActionCommand("Hole Cell Button");
            //this.add(buttonGrid[gridRows][gridCols]);
            // Adding this button to the GridPanel
            buttonGrid[gridRows][gridCols].setEnabled(false);
            this.add(buttonGrid[gridRows][gridCols]);

          }
          // EMPTY CARD CELL
          else if ((!currentCell.isCellHoleCell()) && (!currentCell.isFilledCellCardCell())) {
            buttonGrid[gridRows][gridCols] = new CardButton(null,
                    model.currentTurn().color(), this.canSelectGrid);
            // buttonGrid[gridRows][gridCols].setActionCommand("Empty Cell Button");
            //this.add(buttonGrid[gridRows][gridCols]);
            final int rowIndex = gridRows;  // Store row index for later use in the listener
            final int colIndex = gridCols;  // Store column index for later use in the listener
            buttonGrid[gridRows][gridCols].addActionListener(e -> handleGridSelection(
                    rowIndex, colIndex));
            buttonGrid[gridRows][gridCols].setEnabled(this.canSelectGrid);
            // Adding this button to the GridPanel
            this.add(buttonGrid[gridRows][gridCols]);
          }
          // FILLED CARD CELL
          else {
            // Get the card's color, not the cell color.
            Color cellColor = currentCell.returnFilledCellCard().color();
            if (cellColor.equals(Color.RED)) {
              buttonGrid[gridRows][gridCols] = new CardButton(currentCell
                      .returnFilledCellCard(), PlayerColor.RED, this.canSelectGrid);
              // Adding this button to the GridPanel
              buttonGrid[gridRows][gridCols].setEnabled(false);
              this.add(buttonGrid[gridRows][gridCols]);
            } else {
              buttonGrid[gridRows][gridCols] = new CardButton(currentCell
                      .returnFilledCellCard(), PlayerColor.BLUE, this.canSelectGrid);
              // Adding this button to the GridPanel
              buttonGrid[gridRows][gridCols].setEnabled(false);
              this.add(buttonGrid[gridRows][gridCols]);
            }
          }
        }
      }
    }
    else {
      for (int gridRows = 0; gridRows < rows; gridRows++) {
        for (int gridCols = 0; gridCols < cols; gridCols++) {
          JButton gridButton = buttonGrid[gridRows][gridCols];
          if (gridButton instanceof CardButton) {
            CardButton button = (CardButton) gridButton;
            button.setEnabled(true);
            button.updateSelectionEmptyCard();
          }
        }
      }
    }
  }


  /**
   * Checking if a cell on the grid can be selected based on if a card in the hand has been
   * selected.
   */
  public void canSelectGrid() {
    this.canSelectGrid = true;
    // need to be able to update the buttons here
    // now that it can be selected and the grid is already formed need to be able to rehandle the
    // clicks
    addClickListener(controller);
  }

}
