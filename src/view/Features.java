package view;

import model.Card;

public interface Features {
  void cardSelected(Card card);
  void cellSelected(int row, int col);
}
