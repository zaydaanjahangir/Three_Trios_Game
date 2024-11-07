package model;

import java.io.File;
import java.util.List;

/**
 * Reads and validates card configurations from a file.
 */
public interface CardFileReader {

  /**
   * Reads cards from a file.
   *
   * @param file the card configuration file.
   * @return a list of cards.
   * @throws IllegalArgumentException if the file is invalid.
   */
  List<Card> readCards(File file) throws IllegalArgumentException;
}

