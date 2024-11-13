package model;

public class Move {
  private final Card card;
  private final int row;
  private final int col;

  public Move(Card card, int row, int col) {
    this.card = card;
    this.row = row;
    this.col = col;
  }

  public Card getCard() {
    return card;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }
}
