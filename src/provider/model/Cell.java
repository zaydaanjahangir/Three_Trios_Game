package provider.model;

import java.awt.*;

/**
 * Represents a Cell in a ThreeTriosGame. Each Cell might have its own specific behavior depending
 * on the type of Cell. A Hole Cell cannot hold a card, but a CardCell can.
 */
public interface Cell {
  /**
   * Attempts to play a card onto the cell. If the card is a Hole Cell, it should throw an
   * exception.
   * @param card the card that is attempted to be played on the grid
   * @throws IllegalStateException if an attempt is made to play a card to a HoleCell.
   */
  void playCardToCell(GameCard card);

  /**
   * Checks if this cell is a filled CardCell.
   * @return true if the cell is a filled CardCell, false if it is a HoleCell or if it is an empty
   *         CardCell
   */
  boolean isFilledCellCardCell();

  /**
   * Determines if the cell is a HoleCell.
   * @return true if the cell is a HoleCell
   */
  boolean isCellHoleCell();

  /**
   * Adds this neighboring card to this cell's card's neighbor list in a specified direction.
   * @param d the Direction of the neighboring cell relative to this cell
   * @param c the GameCard that is being added to the cell's card's neighbors
   */
  void addNeighbor(Direction d, GameCard c);

  /**
   * Retrieves the color of this cell. Will be used to determine the cell types.
   * @return the Color of this specific cell
   */
  Color retreiveColor();

  /**
   * Returns the string representation of a Cell.
   * For a HoleCell, it will return " ". For a CardCell, it will return a "_".
   * For a CardCell that is filled, it will be either "B" or "R" depending on the card's owner.
   * @return a string representation of this cell
   */
  String getCellToString();

  /**
   * Returns a COPY of this cell. Mutating this card will have no effect on the game.
   * @return Cell a copy of this cell
   */
  Cell returnCell();

  /**
   * Returns the GameCard of an occupied CardCell, returns null if this cell is a HoleCell
   * or is an empty CardCell.
   * @return GameCard copy of the card that occupies the CardCell
   */
  GameCard returnFilledCellCard();

  /**
   * Returns the row of this cell.
   * @return integer number representing the row of this cell
   */
  int row();

  /**
   * Returns the column of this cell.
   * @return integer number representing the column of this cell
   */
  int col();

  /**
   * Updates the neighbors of the given cell.
   * @param direction the direction to be put in the neighbors
   * @param cell the cell whose neighbors are being updated with this cell
   */
  void updateThisCardToThatNeighbors(Direction direction, Cell cell);

  /**
   * Refreshes the color of the cell to be the same color of the card it holds, if it has a card.
   */
  void refreshColorIfFilledCardCell();
}
