package model;

/**
 * Interface representing the grid in the Three Trios game.
 * The Grid interface defines the essential operations needed to interact with the game grid,
 * such as checking if a cell is playable, accessing cell contents, and placing cards on the grid.
 */
public interface Grid {

  /**
   * Checks if a cell at the specified row and column is playable.
   * A cell is considered playable if it is not a hole and is currently unoccupied.
   *
   * @param row the row index of the cell to check.
   * @param col the column index of the cell to check.
   * @return true if the cell is playable, false otherwise.
   */
  boolean isPlayable(int row, int col);

  /**
   * Retrieves the type of cell at the specified row and column.
   *
   * @param row the row index of the cell to access.
   * @param col the column index of the cell to access.
   * @return the CellType of the cell at the given row and column.
   */
  Cell getCell(int row, int col);

  /**
   * Places a card at the specified location on the grid and assigns ownership to a player.
   * Invalid placements will be met with a try again message.
   *
   * @param card  the Card to place on the grid.
   * @param row   the row index where the card is to be placed.
   * @param col   the column index where the card is to be placed.
   * @param owner the Player who owns the placed card.
   */
  void placeCard(Card card, int row, int col, Player owner);

  CellType getCellType(int row, int col);

  /**
   * Retrieves the number of columns in the grid.
   *
   * @return the int number of columns
   */
  int getCols();

  /**
   * Retrieves the number of rows in the grid.
   *
   * @return the int number of rows
   */
  int getRows();

  /**
   * Retrieves the number of Card Cells on the grid.
   * @return the number of card cells on the grid
   */
  int getNumberOfCardCells();

  /**
   * Checks whether the grid is full.
   * @return true if full, false otherwise
   */
  boolean isFull();

  /**
   * Checks if the specified row and column is within the bounds of the current grid.
   * @param row row index
   * @param col column index
   * @return true if within grid, false otherwise.
   */
  boolean isWithinBounds(int row, int col);

}
