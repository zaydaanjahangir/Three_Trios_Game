package model;

import java.io.File;
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
  public void initializeGame(File gridConfig, File cardConfig, Random random)
          throws IllegalArgumentException {
    GridFileReader gridReader = new GridFileReaderImpl();
    this.grid = gridReader.readGrid(gridConfig);
    int totalCardCells = grid.getNumberOfCardCells();

    CardFileReader cardReader = new CardFileReaderImpl();
    List<Card> cards = cardReader.readCards(cardConfig);

    int requiredCardCount = totalCardCells + 1;
    if (cards.size() < requiredCardCount) {
      throw new IllegalArgumentException("Insufficient number of cards. Required: "
              + requiredCardCount + ", but found: " + cards.size());
    }

    if (random != null) {
      Collections.shuffle(cards, random);
    } else {
      Collections.shuffle(cards);
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
    currentPlayer = (currentPlayer == playerRed) ? playerBlue : playerRed;
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


}
