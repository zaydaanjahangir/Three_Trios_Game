package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import adapters.CellAdapter;
import adapters.GameCardAdapter;
import adapters.GridAdapter;
import adapters.PlayerAdapter;
import provider.model.GameCard;
import provider.model.PlayerColor;
import provider.model.TieBreakerException;


/**
 * Main model class for the Three Trios game.
 * Manages the game state, players, and grid.
 * Class Invariant: The number of cards in play plus the number of cards in players' hands
 * always equals the total number of card cells on the grid.
 */
public class ThreeTriosGameModel implements GameModel, provider.model.ReadOnlyThreeTriosModel {

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

    int cardsPerPlayer = (totalCardCells + 1) / 2;

    if (playerRed == null || playerBlue == null) {
      throw new IllegalStateException("Players not set. Use setPlayers() before initializing " +
              "the game.");
    }

    for (int i = 0; i < cardsPerPlayer; i++) {
      playerRed.addCardToHand(cards.get(i));
    }
    for (int i = cardsPerPlayer; i < 2 * cardsPerPlayer; i++) {
      playerBlue.addCardToHand(cards.get(i));
    }

    this.currentPlayer = playerRed;
  }


  public void setCurrentPlayerForTest(Player player) {
    this.currentPlayer = player;
  }

  public void setPlayers(Player playerRed, Player playerBlue) {
    this.playerRed = playerRed;
    this.playerBlue = playerBlue;
  }

  @Override
  public void addModelStatusListener(ModelStatusListener listener) {
    // empty
  }

  @Override
  public boolean isGameOver() {
    return grid.isFull();
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  @Override
  public provider.model.Player findWinner() throws Exception {
    if (!isGameOver()) {
      throw new Exception("Game is not over yet.");
    }

    String winner = getWinner();

    if (winner.contains("Red wins!")) {
      return new PlayerAdapter(getPlayerRed());
    } else if (winner.contains("Blue wins!")) {
      return new PlayerAdapter(getPlayerBlue());
    } else {
      throw new TieBreakerException("It's a tie!");
    }
  }

  @Override
  public String getCellToString(int row, int col) {
    Cell cell = getCellAt(row, col);
    if (cell.isHole()) {
      return " "; // Hole
    } else if (cell.isOccupied()) {
      Player owner = cell.getOwner();
      return owner.getColor().equals("Red") ? "R" : "B";
    } else {
      return "_"; // Empty cell
    }
  }

  @Override
  public String handToString() {
    StringBuilder sb = new StringBuilder();
    List<Card> hand = getCurrentPlayer().getHand();
    for (Card card : hand) {
      sb.append(card.toString()).append("\n");
    }
    return sb.toString();
  }

  @Override
  public provider.model.Player currentTurn() {
    return new PlayerAdapter(getCurrentPlayer());
  }

  @Override
  public int numRows() {
    return this.getGridRows();
  }

  @Override
  public int numColumns() {
    return this.getGridCols();
  }

  @Override
  public provider.model.Grid returnGridCopy() {
    return new GridAdapter(getGrid());
  }

  @Override
  public provider.model.Cell returnCell(int row, int col) {
    Cell cell = getCellAt(row, col);
    return new CellAdapter(cell, row, col);
  }

  @Override
  public List<GameCard> returnPlayerHand(PlayerColor color) {
    Player player = (color == PlayerColor.RED) ? getPlayerRed() : getPlayerBlue();
    List<Card> hand = player.getHand();
    List<GameCard> gameCards = new ArrayList<>();
    for (Card card : hand) {
      gameCards.add(new GameCardAdapter(card, player));
    }
    return gameCards;
  }

  @Override
  public int calculateScore(PlayerColor color) {
    Player player = (color == PlayerColor.RED) ? getPlayerRed() : getPlayerBlue();
    return getPlayerScore(player);
  }

  @Override
  public int howManyCardsCanBeFlipped(GameCard gameCard, int row, int col, PlayerColor color) {
    Player player = (color == PlayerColor.RED) ? getPlayerRed() : getPlayerBlue();
    Card card = adaptGameCardToCard(gameCard);
    return getPotentialFlips(player, card, row, col);
  }

  private Card adaptGameCardToCard(provider.model.GameCard gameCard) {
    model.Value north = adaptValue(gameCard.north());
    model.Value south = adaptValue(gameCard.south());
    model.Value east = adaptValue(gameCard.east());
    model.Value west = adaptValue(gameCard.west());
    String name = gameCard.toString();
    return new StandardCard(name, north, south, east, west);
  }

  private model.Value adaptValue(int value) {
    switch (value) {
      case 1:
        return Value.ONE;
      case 2:
        return Value.TWO;
      case 3:
        return Value.THREE;
      case 4:
        return Value.FOUR;
      case 5:
        return Value.FIVE;
      case 6:
        return Value.SIX;
      case 7:
        return Value.SEVEN;
      case 8:
        return Value.EIGHT;
      case 9:
        return Value.NINE;
      case 10:
        return Value.A;
      default:
        throw new IllegalArgumentException("Invalid value");
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////

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
    if (!player.equals(currentPlayer)) {
      throw new IllegalArgumentException("It's not your turn.");
    }

    if (!grid.isPlayable(row, col)) {
      throw new IllegalArgumentException("Cell is not playable.");
    }

    if (!player.getHand().contains(card)) {
      throw new IllegalArgumentException("You don't have that card.");
    }

    grid.placeCard(card, row, col, player);
    player.removeCardFromHand(card);
    executeBattlePhase(row, col);
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

    // debugging
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

  @Override
  public void switchTurn() {
    currentPlayer = currentPlayer.equals(playerRed) ? playerBlue : playerRed;
  }

  @Override
  public void startGame() {
    // empty
  }

  @Override
  public Player getPlayerRed() {
    return this.playerRed;
  }

  @Override
  public Player getPlayerBlue() {
    return this.playerBlue;
  }

}
