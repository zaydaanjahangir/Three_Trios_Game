package model;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;


/**
 * Main model class for the Three Trios game.
 * Manages the game state, players, and grid.
 * Class Invariant: The number of cards in play plus the number of cards in players' hands
 * always equals the total number of card cells on the grid.
 */
public class ThreeTriosGameModel implements GameModel {

  private Grid grid;
  private Player playerRed;
  private Player playerBlue;
  private Player currentPlayer;

  @Override
  public void initializeGame(Grid grid, List<Card> cards, Random random)
          throws IllegalArgumentException {
    this.grid = grid;

    int totalCardCells = grid.getNumberOfCardCells();
    int requiredCardCount = totalCardCells + 1;

    if (cards.size() < requiredCardCount) {
      throw new IllegalArgumentException("Insufficient number of cards. Required: "
              + requiredCardCount + ", but found: " + cards.size());
    }

    if (random != null) {
      Collections.shuffle(cards, random);
    } else {
      System.out.println("Shuffling is currently under maintenance for testing.");
    }

    this.playerRed = new PlayerImpl("Red");
    this.playerBlue = new PlayerImpl("Blue");
    int cardsPerPlayer = (totalCardCells + 1) / 2;


    for (int i = 0; i < cardsPerPlayer; i++) {
      playerRed.addCardToHand(cards.get(i));
    }
    for (int i = cardsPerPlayer; i < 2 * cardsPerPlayer; i++) {
      playerBlue.addCardToHand(cards.get(i));
    }

    this.currentPlayer = playerRed;
    System.out.println("Initializing game with current player: " + currentPlayer.getColor());
  }

  public void setCurrentPlayerForTest(Player player) {
    this.currentPlayer = player;
  }


  @Override
  public boolean isGameOver() {
    return grid.isFull();
  }

  @Override
  public String getWinner() {
    if (!isGameOver()) {
      return "Game is not over yet.";
    }

    int redScore = playerRed.getHand().size();
    int blueScore = playerBlue.getHand().size();

    int rows = grid.getRows();
    int cols = grid.getCols();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Cell cell = grid.getCell(row, col);
        if (cell != null && !cell.isHole() && cell.isOccupied()) {
          Player owner = cell.getOwner();
          if (owner == playerRed) {
            redScore++;
          } else if (owner == playerBlue) {
            blueScore++;
          }
        }
      }
    }

    if (redScore > blueScore) {
      return "Red wins!";
    } else if (blueScore > redScore) {
      return "Blue wins!";
    } else {
      return "It's a tie!";
    }
  }

  @Override
  public void placeCard(Player player, Card card, int row, int col) {
    if (player != currentPlayer) {
      System.out.println(player);
      throw new IllegalArgumentException("It's not your turn.");
    }

    if (!grid.isPlayable(row, col)) {
      throw new IllegalArgumentException("Cell is not playable.");
    }

    if (!player.getHand().contains(card)) {
      throw new IllegalArgumentException("Player does not have the specified card.");
    }

    grid.placeCard(card, row, col, player);
    player.removeCardFromHand(card);
    executeBattlePhase(row, col);

    if (currentPlayer == playerRed) {
      currentPlayer = playerBlue;
    } else {
      currentPlayer = playerRed;
    }
  }

  @Override
  public void executeBattlePhase(int row, int col) {

    Deque<int[]> toProcess = new ArrayDeque<>();
    toProcess.add(new int[]{row, col});

    while (!toProcess.isEmpty()) {
      int[] position = toProcess.poll();
      int currentRow = position[0];
      int currentCol = position[1];

      Cell currentCell = grid.getCell(currentRow, currentCol);
      Player currentOwner = currentCell.getOwner();
      Card currentCard = currentCell.getCard();

      int[][] directions = {
              {-1, 0}, // North
              {1, 0},  // South
              {0, 1},  // East
              {0, -1}  // West
      };
      Direction[] dirEnums = {
          Direction.NORTH,
          Direction.SOUTH,
          Direction.EAST,
          Direction.WEST
      };

      for (int i = 0; i < directions.length; i++) {
        int[] dir = directions[i];
        int adjRow = currentRow + dir[0];
        int adjCol = currentCol + dir[1];
        Direction direction = dirEnums[i];

        if (grid.isWithinBounds(adjRow, adjCol)) {
          Cell adjCell = grid.getCell(adjRow, adjCol);
          if (adjCell != null && !adjCell.isHole() && adjCell.isOccupied()) {
            Player adjOwner = adjCell.getOwner();
            if (adjOwner != currentOwner) {
              Card adjCard = adjCell.getCard();
              boolean shouldFlip = currentCard.compareAgainst(adjCard, direction);
              if (shouldFlip) {
                adjCell.setOwner(currentOwner);
                toProcess.add(new int[]{adjRow, adjCol});
              }
            }
          }
        }
      }
    }
  }

  @Override
  public Grid getGrid() {
    return this.grid;
  }

  @Override
  public Player getCurrentPlayer() {
    return this.currentPlayer;
  }

  @Override
  public Player getOpponentPlayer() {
    return (currentPlayer == playerRed) ? playerBlue : playerRed;
  }

  @Override
  public int getGridRows() {
    return grid.getRows();
  }

  @Override
  public int getGridCols() {
    return grid.getCols();
  }

  @Override
  public Cell getCellAt(int row, int col) {
    return grid.getCell(row, col);
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    return player.getHand();
  }

  @Override
  public Player getCardOwnerAt(int row, int col) {
    Cell cell = grid.getCell(row, col);
    if (cell.isOccupied()) {
      return cell.getOwner();
    } else {
      return null; // maybe throw an exception
    }
  }

  @Override
  public boolean isLegalMove(Player player, int row, int col) {
    boolean isPlayable = grid.isPlayable(row, col);
    boolean isCurrentPlayer = player.equals(currentPlayer);  // Use equals for comparison
    boolean isOccupied = grid.getCell(row, col).isOccupied();

    //System.out.println("isLegalMove called for player: " + player.getColor() + " at (" + row + ",
    //" + col + ")");
    //System.out.println("isPlayable: " + isPlayable + ", isCurrentPlayer: " + isCurrentPlayer + ",
    //isOccupied: " + isOccupied);

    return isCurrentPlayer && isPlayable && !isOccupied;
  }


  @Override
  public int getPlayerScore(Player player) {
    int score = player.getHand().size();
    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        Cell cell = grid.getCell(row, col);
        if (cell.isOccupied() && cell.getOwner() == player) {
          score++;
        }
      }
    }
    return score;
  }

  @Override
  public int getPotentialFlips(Player player, Card card, int row, int col) {
    // Check if the move is legal, else return 0
    if (!grid.isWithinBounds(row, col) || !grid.isPlayable(row, col) || grid.getCell(row, col)
            .isOccupied()) {
      return 0;
    }

    int flips = 0;
    Player currentPlayer = player;
    int[][] directions = {
            {-1, 0}, // North
            {1, 0},  // South
            {0, 1},  // East
            {0, -1}  // West
    };

    Direction[] dirEnums = {
        Direction.NORTH,
        Direction.SOUTH,
        Direction.EAST,
        Direction.WEST
    };

    // Check adjacent cells
    for (int i = 0; i < directions.length; i++) {
      int[] dir = directions[i];
      int adjRow = row + dir[0];
      int adjCol = col + dir[1];
      Direction direction = dirEnums[i];

      if (grid.isWithinBounds(adjRow, adjCol)) {
        Cell adjCell = grid.getCell(adjRow, adjCol);
        if (adjCell != null && !adjCell.isHole() && adjCell.isOccupied()) {
          Player adjOwner = adjCell.getOwner();
          if (!adjOwner.equals(currentPlayer)) {
            Card adjCard = adjCell.getCard();
            boolean shouldFlip = card.compareAgainst(adjCard, direction);
            if (shouldFlip) {
              flips++;
            }
          }
        }
      }
    }
    return flips;
  }


}
