package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Card;
import model.Cell;
import model.GameModel;
import model.Grid;
import model.ModelStatusListener;
import model.Player;

/**
 * Mock game model class used to test the player classes.
 */
public class MockGameModel implements GameModel {
  public List<String> methodCalls = new ArrayList<>();

  private final Player currentPlayer;
  private Grid grid;
  private boolean isLegalMoveResult = true;
  private int potentialFlipsResult = 0;

  /**
   * Mock ThreeTriosModel constructor used to test the player classes.
   *
   * @param currentPlayer  current player
   * @param opponentPlayer opponenet player
   */
  public MockGameModel(Player currentPlayer, Player opponentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  @Override
  public Player getCurrentPlayer() {
    methodCalls.add("getCurrentPlayer");
    return currentPlayer;
  }

  @Override
  public Player getOpponentPlayer() {
    return null;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public String getWinner() {
    return null;
  }

  @Override
  public Grid getGrid() {
    methodCalls.add("getGrid");
    return grid;
  }

  @Override
  public int getGridRows() {
    methodCalls.add("getGridRows");
    return grid.getRows();
  }

  @Override
  public int getGridCols() {
    methodCalls.add("getGridCols");
    return grid.getCols();
  }

  @Override
  public Cell getCellAt(int row, int col) {
    return null;
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    return null;
  }

  @Override
  public Player getCardOwnerAt(int row, int col) {
    return null;
  }

  @Override
  public boolean isLegalMove(Player player, int row, int col) {
    methodCalls.add("isLegalMove: " + player.getColor() + ", (" + row + "," + col + ")");
    return isLegalMoveResult;
  }

  @Override
  public int getPotentialFlips(Player player, Card card, int row, int col) {
    methodCalls.add("getPotentialFlips: " + player.getColor() + ", " + card.getName() + ", " +
            "(" + row + "," + col + ")");
    return potentialFlipsResult;
  }

  @Override
  public int getPlayerScore(Player player) {
    return 0;
  }

  @Override
  public Player getPlayerRed() {
    return null;
  }

  @Override
  public Player getPlayerBlue() {
    return null;
  }

  @Override
  public void setPlayers(Player playerRed, Player playerBlue) {
    // empty
  }

  @Override
  public void placeCard(Player player, Card card, int row, int col) {
    methodCalls.add("placeCard: " + player.getColor() + ", " + card.getName() + ", " +
            "(" + row + "," + col + ")");
  }

  @Override
  public void executeBattlePhase(int row, int col) {
    // empty
  }

  @Override
  public void setCurrentPlayerForTest(Player player) {
    // empty
  }

  @Override
  public void addModelStatusListener(ModelStatusListener listener) {
    // empty
  }

  @Override
  public void switchTurn() {
    // empty
  }

  @Override
  public void startGame() {
    // empty
  }


  // Setters to customize the mock's behavior
  public void setIsLegalMoveResult(boolean result) {
    this.isLegalMoveResult = result;
  }

  public void setPotentialFlipsResult(int result) {
    this.potentialFlipsResult = result;
  }

  public void setGrid(Grid grid) {
    this.grid = grid;
  }

  @Override
  public void initializeGame(Grid grid, List<Card> cards, Random random)
          throws IllegalArgumentException {
    // empty
  }

}
