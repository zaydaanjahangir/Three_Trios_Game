// File: strategy/MockThreeTriosModel.java
package strategy;

import model.*;

import java.util.List;

/**
 * Mock implementation of ReadonlyThreeTriosModel for testing strategies.
 */
public class MockThreeTriosModel implements ReadOnlyGameModel {
  // Implement necessary methods to simulate game state


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
    return null;
  }

  @Override
  public Player getCurrentPlayer() {
    return null;
  }

  @Override
  public Player getOpponentPlayer() {
    return null;
  }

  @Override
  public int getGridRows() {
    return 3;
  }

  @Override
  public int getGridCols() {
    return 3;
  }

  @Override
  public Cell getCellAt(int row, int col) {
    // Return mock cells based on desired test scenario
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
    // Return true or false based on test scenario
    return true;
  }

  @Override
  public int getPotentialFlips(Player player, Card card, int row, int col) {
    // Return predetermined values to control the strategy's decisions
    return 1; // For example
  }

  @Override
  public int getPlayerScore(Player player) {
    return 0;
  }

  // Implement other methods as needed
}
