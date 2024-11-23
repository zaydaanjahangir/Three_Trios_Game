package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mock implementation of the GameModel interface for testing purposes.
 */
public class MockGameModel implements GameModel {
  // log of the methods that were called
  public List<String> methodCalls = new ArrayList<>();
  private Player currentPlayer;
  private boolean gameOver = false;
  private String winner = "";
  public MockGameModel() {
    this.currentPlayer = new PlayerImpl("Red");
  }

  @Override
  public void initializeGame(Grid grid, List<Card> cards, Random random) {
    methodCalls.add("initializeGame");
  }

  @Override
  public void setPlayers(Player playerRed, Player playerBlue) {
    methodCalls.add("setPlayers");
  }


  @Override
  public void placeCard(Player player, Card card, int row, int col) {
    methodCalls.add("placeCard: " + player.getColor() + ", " + card.getName() + ", " +
            "(" + row + "," + col + ")");
  }

  @Override
  public void executeBattlePhase(int row, int col) {

  }

  @Override
  public void setCurrentPlayerForTest(Player player) {

  }

  @Override
  public void addModelStatusListener(ModelStatusListener listener) {

  }

  @Override
  public void switchTurn() {
    methodCalls.add("switchTurn");
    // Simple turn switch logic for testing
    if (currentPlayer.getColor().equals("Red")) {
      currentPlayer = new PlayerImpl("Blue");
    } else {
      currentPlayer = new PlayerImpl("Red");
    }
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
  public int getGridRows() {
    return 0;
  }

  @Override
  public int getGridCols() {
    return 0;
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
    return false;
  }

  @Override
  public int getPotentialFlips(Player player, Card card, int row, int col) {
    return 0;
  }

  @Override
  public int getPlayerScore(Player player) {
    return 0;
  }

  @Override
  public Grid getGrid() {
    methodCalls.add("getGrid");
    return null;
  }

  @Override
  public Player getPlayerRed() {
    methodCalls.add("getPlayerRed");
    return new PlayerImpl("Red");
  }

  @Override
  public Player getPlayerBlue() {
    methodCalls.add("getPlayerBlue");
    return new PlayerImpl("Blue");
  }

  @Override
  public boolean isGameOver() {
    methodCalls.add("isGameOver");
    return gameOver;
  }

  @Override
  public String getWinner() {
    methodCalls.add("getWinner");
    return winner;
  }

  @Override
  public void startGame() {
    methodCalls.add("startGame");
  }

  public void setGameOver(boolean gameOver, String winner) {
    this.gameOver = gameOver;
    this.winner = winner;
  }

  public void setCurrentPlayer(Player player) {
    this.currentPlayer = player;
  }
}
