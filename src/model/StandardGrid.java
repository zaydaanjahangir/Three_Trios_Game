package model;

/**
 * Implementation of the Grid interface which represents the Grid used to play Three Trios.
 */
public class StandardGrid implements Grid {
  private final int rows;
  private final int cols;
  private final Cell[][] cells;

  /**
   * Constructor for the grid.
   * @param cells Given 2D array used to represent the grid
   */
  public StandardGrid(Cell[][] cells) {
    this.cells = cells;
    this.rows = cells.length;
    this.cols = cells[0].length;
  }

  @Override
  public boolean isPlayable(int row, int col) {
    if (row >= getRows() || col >= getCols() || row < 0 || col < 0) {
      throw new IllegalArgumentException("Invalid indexes"); // debugging
    }
    // return not occupied and not a hole (both must be true to be playable)
    return !(cells[row][col].isOccupied()) && !(cells[row][col].equals(CellType.HOLE));
  }

  @Override
  public Cell getCell(int row, int col) {
    return cells[row][col];
  }

  @Override
  public CellType getCellType(int row, int col) {
    Cell cell = getCell(row, col);
    return cell.isHole() ? CellType.HOLE : CellType.CARD_CELL;
  }

  @Override
  public void placeCard(Card card, int row, int col, Player owner) {
    if (!isWithinBounds(row, col)) {
      throw new IllegalArgumentException("Position out of bounds.");
    }
    Cell cell = cells[row][col];
    if (cell.isHole()) {
      throw new IllegalArgumentException("Cannot place a card in a hole.");
    }
    if (cell.isOccupied()) {
      throw new IllegalArgumentException("Cell is already occupied.");
    }
    cell.placeCard(card, owner);
  }

  @Override
  public int getCols() {
    return this.cols;
  }

  @Override
  public int getRows() {
    return this.rows;
  }

  @Override
  public int getNumberOfCardCells() {
    int cardCellCount = 0;
    for (int row = 0; row < getRows(); row++) {
      for (int col = 0; col < getCols(); col++) {
        if (getCellType(row, col) == CellType.CARD_CELL) {
          cardCellCount++;
        }
      }
    }
    return cardCellCount;
  }


  @Override
  public boolean isFull() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Cell cell = cells[row][col];
        // Check if the cell is a card cell and is unoccupied
        if (!cell.isHole() && !cell.isOccupied()) {
          return false;
        }
      }
    }
    return true; // All playable cells are occupied
  }

  @Override
  public boolean isWithinBounds(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }

}

