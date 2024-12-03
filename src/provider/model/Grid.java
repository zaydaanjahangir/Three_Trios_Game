package provider.model;

import java.awt.*;
import java.util.List;

/**
 * Interface for Grid, representing a 2D board used in the ThreeTriosGame.
 * The grid is organized as a row-by-column matrix where each cell can be a CardCell
 * or a HoleCell, and it manages the state and updates of cells during gameplay.

 * - The grid is 0-index based with the origin located at the top left corner of the grid.
 * - The row index increases down, representing the y-axis, and the column index increases to
 *   the right, representing the x-axis.
 * - A cell at coordinates (0,0) refers to the top left cell.
 * - This grid implementation uses a 2D array for storage, where "grid[row][col]" corresponds to a
 *   cell at Cartesian coordinates (row, col).
 * - In this mapping:
 *   - "row" represents the vertical position (y-axis) on the board.
 *   - "col" represents the horizontal position (x-axis) on the board.
 */
public interface Grid {

  /**
   * Update this grid given the string representation of a grid. The string representation that is
   * passed in should have an "X" representing a HoleCell and a "C" representing a CardCell.
   * E.g. if the number of columns in a single row is 5, a fully CardCell row would
   *      look like: "CCCCC".
   * @param grid a string representation of what the grid should look like
   */
  void createGrid(String grid);

  /**
   * Plays a card to the grid and makes changes in the cell to update the grid.
   * @param card the card that was chosen to play to the grid
   * @param row integer number representing the row to play to the grid, 0-index based
   * @param col integer number representing the column to play to the grid, 0-index based
   * @throws IllegalArgumentException if the card is null
   * @throws IllegalArgumentException if the cell is already occupied
   */
  void playToGrid(GameCard card, int row, int col);

  /**
   * Return the integer number of card cells in this grid.
   * @return integer number representing the number of card cells on this grid
   */
  int numCardCells();

  /**
   * Determines if all card cells are filled on the grid.
   * @return boolean, true if all the card cells are filled on the grid
   */
  boolean areAllCardCellsFilled();

  /**
   * Returns the count of the passed in color on this grid.
   * @param color the color to find in the grid
   * @return an integer number representing the count of this color on the grid
   */
  int countColorOnGrid(Color color);

  /**
   * Returns a string representation of the Cell on this grid at a specific row and column.
   * If it's a HoleCell, it will return " ". If it's an empty CardCell, it will return "_".
   * If it's a filled CardCell, it'll return the string representation of the owner, "R" or "B".
   * @param row the integer number representing the row on the grid
   * @param col the integer number representing the column on the grid
   * @return a string of the requested Cell in its string format.
   */
  String getCellToString(int row, int col);

  /**
   * Returns a copy of the cell at the given row and column.
   * @return Cell
   */
  Cell returnCell(int row, int col);

  /**
   * Returns the number of rows on this grid.
   * @return integer number representing the number of rows in this grid.
   */
  int numRows();

  /**
   * Return the number of columns.
   * @return integer number representing the number of columns in this grid.
   */
  int numCols();

  /**
   * Counts the neighbors of this cell. A neighbor of a cell is any card cell, empty or occupied
   * to the north, south, east, or west of a cell.
   * @param cell the cell whose neighbors want to be counted
   * @return an integer number representing the number of neighbors this cell has
   */
  int countNeighbors(Cell cell);

  /**
   * Returns a list of all the CardCells on the grid. Mutating the Cells inside this returned list
   * will not mutate the state of the game.
   * @return a list of Cells in the grid that are CardCells
   */
  List<Cell> cardCellList();

  /**
   * Connects the neighbors of each cell inside the grid.
   */
  void connectNeighbors();

}
