package model;

/**
 * Represents a cell on the game grid.
 * A cell can be a hole or a playable cell that may contain a card.
 */
public class CellImpl implements Cell {
  private final boolean isHole;
  private Card card;
  private Player owner;

  /**
   * Constructor for the implementation of a Cell.
   *
   * @param isHole true if the cell is a hole, false otherwise
   */
  public CellImpl(boolean isHole) {
    this.isHole = isHole;
    this.card = null;
    this.owner = null;
  }

  @Override
  public boolean isOccupied() {
    return card != null;
  }

  @Override
  public void placeCard(Card card, Player owner) {
    if (isHole) {
      throw new IllegalStateException("Cannot place a card in a hole.");
    }
    if (isOccupied()) {
      throw new IllegalStateException("Cell is already occupied.");
    }
    this.card = card;
    this.owner = owner;
  }

  public boolean isHole() {
    return isHole;
  }

  public Card getCard() {
    return card;
  }

  public Player getOwner() {
    return owner;
  }

  @Override
  public void setOwner(Player owner) {
    if (!isOccupied()) {
      throw new IllegalStateException("Cannot set owner on an unoccupied cell.");
    }
    this.owner = owner;
  }

}
