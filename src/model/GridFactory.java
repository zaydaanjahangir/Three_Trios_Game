package model;

/**
 * Factory class for creating new grids.
 */
public class GridFactory {
  public static Grid createGrid(Cell[][] cells) {
    return new StandardGrid(cells);
  }
}

