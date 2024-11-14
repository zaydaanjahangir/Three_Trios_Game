package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import model.Cell;
import model.CellImpl;
import model.Grid;
import model.GridFactory;

/**
 * Reads and parses grid data from a configuration file.
 */
public class GridFileReaderImpl implements GridFileReader {

  @Override
  public Grid readGrid(File file) throws IllegalArgumentException {
    try (Scanner scanner = new Scanner(file)) {
      validateNonEmptyFile(scanner);

      int[] dimensions = parseGridDimensions(scanner.nextLine().trim());
      int rows = dimensions[0];
      int cols = dimensions[1];

      Cell[][] cells = parseGridCells(scanner, rows, cols);

      return GridFactory.createGrid(cells);

    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Grid file not found: " + file.getName(), e);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Grid dimensions must be integers.", e);
    }
  }

  private void validateNonEmptyFile(Scanner scanner) {
    if (!scanner.hasNextLine()) {
      throw new IllegalArgumentException("Grid file is empty.");
    }
  }

  private int[] parseGridDimensions(String line) {
    String[] dimensions = line.split("\\s+");
    if (dimensions.length != 2) {
      throw new IllegalArgumentException("Invalid grid dimensions: " + dimensions.length);
    }
    int rows = Integer.parseInt(dimensions[0]);
    int cols = Integer.parseInt(dimensions[1]);
    validatePositiveDimensions(rows, cols);
    return new int[]{rows, cols};
  }

  private void validatePositiveDimensions(int rows, int cols) {
    if (rows <= 0 || cols <= 0) {
      throw new IllegalArgumentException("Grid dimensions must be positive integers.");
    }
  }

  private Cell[][] parseGridCells(Scanner scanner, int rows, int cols) {
    Cell[][] cells = new Cell[rows][cols];
    int rowCount = 0;

    while (scanner.hasNextLine() && rowCount < rows) {
      String line = scanner.nextLine().trim();
      validateRowLength(line, rowCount, cols);
      cells[rowCount] = parseRowCells(line, cols, rowCount);
      rowCount++;
    }

    if (rowCount != rows) {
      throw new IllegalArgumentException(
              "Grid file has insufficient rows. Expected " + rows + ", got " + rowCount);
    }

    return cells;
  }

  private void validateRowLength(String line, int rowCount, int cols) {
    if (line.length() != cols) {
      throw new IllegalArgumentException(
              "Row length mismatch at row " + (rowCount + 1) + ": " + line);
    }
  }

  private Cell[] parseRowCells(String line, int cols, int rowCount) {
    Cell[] rowCells = new Cell[cols];
    for (int col = 0; col < cols; col++) {
      rowCells[col] = createCell(line.charAt(col), rowCount, col);
    }
    return rowCells;
  }

  private Cell createCell(char cellChar, int row, int col) {
    switch (cellChar) {
      case 'X':
        return new CellImpl(true); // Hole
      case 'C':
        return new CellImpl(false); // Card cell
      default:
        throw new IllegalArgumentException(
                "Invalid cell character '" + cellChar + "' at row " + (row + 1) + ", column "
                        + (col + 1));
    }
  }
}
