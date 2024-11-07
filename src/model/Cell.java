package model;

/**
 * Represents a cell on the game grid.
 */
public interface Cell {

  /**
   * Checks if the cell is occupied.
   *
   * @return true if occupied, false otherwise.
   */
  boolean isOccupied();

  /**
   * Places a card in this cell.
   *
   * @param card  the card to place.
   * @param owner the player placing the card.
   */
  void placeCard(Card card, Player owner);

  /**
   * Checks whether the Cell is a hole.
   *
   * @return true if a hole, false otherwise.
   */
  boolean isHole();

  /**
   * Gets the owner of the card at the cell.
   *
   * @return the Player color the owner
   */
  Player getOwner();

  /**
   * Gets the card at the cell.
   *
   * @return a Card object
   */
  Card getCard();

  /**
   * Sets the owner of the card at the cell.
   *
   * @param owner the new owner to own the cell
   */
  void setOwner(Player owner);
}

