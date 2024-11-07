package model;

import java.io.File;

/**
 * Interface for reading and validating grid configurations from a file.
 */
public interface GridFileReader {

  /**
   * Reads and parses the grid from the specified file.
   * Throws an exception if the file is invalid.
   *
   * @param file the File object representing the grid configuration file.
   * @return a Grid object representing the parsed grid.
   * @throws IllegalArgumentException if the file contains invalid data.
   */
  Grid readGrid(File file) throws IllegalArgumentException;
}
