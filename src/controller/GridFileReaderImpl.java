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
      if (!scanner.hasNextLine()) {
        throw new IllegalArgumentException("Grid file is empty.");
      }

      String[] dimensions = scanner.nextLine().trim().split("\\s+");
      if (dimensions.length != 2) {
        throw new IllegalArgumentException("Invalid grid dimensions: " + dimensions.length);
      }


      int rows = Integer.parseInt(dimensions[0]);
      int cols = Integer.parseInt(dimensions[1]);

      if (rows <= 0 || cols <= 0) {
        throw new IllegalArgumentException("Grid dimensions must be positive integers.");
      }

      Cell[][] cells = new Cell[rows][cols];
      int rowCount = 0; // for debugging

      while (scanner.hasNextLine() && rowCount < rows) {
        String line = scanner.nextLine().trim();
        if (line.length() != cols) {
          throw new IllegalArgumentException(
                  "Row length mismatch at row " + (rowCount + 1) + ": " + line);
        }

        for (int col = 0; col < cols; col++) {
          char cellChar = line.charAt(col);
          switch (cellChar) {
            case 'X':
              cells[rowCount][col] = new CellImpl(true); // Hole
              break;
            case 'C':
              cells[rowCount][col] = new CellImpl(false); // Card cell
              break;
            default:
              throw new IllegalArgumentException(
                      "Invalid cell character '" + cellChar + "' at row " +
                              (rowCount + 1) + ", column " + (col + 1));
          }
        }
        rowCount++;
      }

      if (rowCount != rows) {
        throw new IllegalArgumentException(
                "Grid file has insufficient rows. Expected " + rows + ", got " + rowCount);
      }

      return GridFactory.createGrid(cells);

    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Grid file not found: " + file.getName(), e);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Grid dimensions must be integers.", e);
    }
  }
}

