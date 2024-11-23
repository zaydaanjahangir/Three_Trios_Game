package model;

public interface ModelStatusListener {
  void turnChanged(Player currentPlayer);
  void gameOver(String result);
}
