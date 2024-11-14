package model;

/**
 * The move class utilized by the strategy pattern for the AI player.
 */
public class Move {
  private final Card card;
  private final int row;
  private final int col;

  /**
   * Move constructor which constructs a move object.
   *
   * @param card StandardCard
   * @param row  row of move
   * @param col  column of move
   */
  public Move(Card card, int row, int col) {
    this.card = card;
    this.row = row;
    this.col = col;
  }

  /**
   * Retrieves the card used in the move.
   *
   * @return Standard card
   */
  public Card getCard() {
    return card;
  }

  /**
   * Retrieves the row the move was made in.
   *
   * @return int number (0-based)
   */
  public int getRow() {
    return row;
  }

  /**
   * Retrieves the column the move was made in.
   *
   * @return int number (0-based)
   */
  public int getCol() {
    return col;
  }
}
