package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Card;
import model.Cell;
import model.CellImpl;
import model.Grid;
import model.Player;
import model.ReadOnlyGameModel;

/**
 * A mock implementation of the ReadOnlyGameModel interface for testing purposes.
 */
public class CornerMockGameModel implements ReadOnlyGameModel {

  private Player currentPlayer;
  private final Set<String> holes;
  private final Set<String> occupiedCells;
  private final Set<String> inspectedCoordinates;
  private final Set<String> potentialFlipsMap;

  public CornerMockGameModel() {
    this.holes = new HashSet<>();
    this.occupiedCells = new HashSet<>();
    this.inspectedCoordinates = new HashSet<>();
    this.potentialFlipsMap = new HashSet<>();
  }

  /**
   * Sets the AI and opponent players for the mock model.
   */
  public void setPlayers(Player aiPlayer, Player opponent) {
    this.currentPlayer = aiPlayer; // Set the initial player for testing
  }

  /**
   * Sets the current player.
   */
  public void setCurrentPlayer(Player player) {
    this.currentPlayer = player;
  }

  /**
   * Marks a cell as a hole.
   */
  public void setHole(int row, int col, boolean isHole) {
    String key = getKey(row, col);
    if (isHole) {
      holes.add(key);
    } else {
      holes.remove(key);
    }
  }

  /**
   * Places a card on the grid.
   */
  public void placeCard(Player player, Card card, int row, int col) {
    String key = getKey(row, col);
    if (holes.contains(key) || occupiedCells.contains(key)) {
      throw new IllegalArgumentException("Cannot place card at occupied or hole position: (" + row + "," + col + ")");
    }
    occupiedCells.add(key);
    // Toggle current player (assuming two-player game)
    togglePlayer();
  }

  /**
   * Determines if a move is legal.
   */
  @Override
  public boolean isLegalMove(Player player, int row, int col) {
    // Only log if the coordinates are within the 3x3 grid bounds
    if ((row == 0 && (col == 0 || col == 2)) || (row == 2 && (col == 0 || col == 2))) {
      String key = getKey(row, col);
      inspectedCoordinates.add(key); // Log this corner inspection
    }
    return player.equals(currentPlayer) && !holes.contains(getKey(row, col)) && !occupiedCells.contains(getKey(row, col));
  }



  /**
   * Gets the potential flips for a move.
   */
  public int getPotentialFlips(Player player, Card card, int row, int col) {
    String key = getKey(row, col);
    // Define potential flips based on the test setup
    // For simplicity, return a fixed number or customize as needed
    if (potentialFlipsMap.contains(key)) {
      return 2; // Example: returning 2 flips
    }
    return 0;
  }

  /**
   * Retrieves a cell. (Optional: Implement if needed)
   */
  public Cell getCell(int row, int col) {
    throw new UnsupportedOperationException("getCell is not supported in MockGameModel.");
  }

  /**
   * Retrieves all inspected coordinates.
   */
  public List<String> getInspectedCoordinates() {
    return new ArrayList<>(inspectedCoordinates);
  }

  /**
   * Clears inspected coordinates.
   */
  public void clearInspectedCoordinates() {
    inspectedCoordinates.clear();
  }

  /**
   * Sets the number of potential flips for a specific position.
   */
  public void setPotentialFlips(int row, int col, int flips) {
    String key = getKey(row, col);
    if (flips > 0) {
      potentialFlipsMap.add(key);
    } else {
      potentialFlipsMap.remove(key);
    }
  }

  /**
   * Helper method to create a unique key for grid positions.
   */
  private String getKey(int row, int col) {
    return row + "," + col;
  }

  /**
   * Toggles the current player. Assumes two-player game.
   */
  private void togglePlayer() {
    // Implement player toggling logic based on your Player implementation
    // For simplicity, this example does nothing. Adjust as needed.
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
  public int getPlayerScore(Player player) {
    return 0;
  }
}

