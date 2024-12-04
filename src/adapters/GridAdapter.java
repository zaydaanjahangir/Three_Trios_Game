package adapters;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.CellType;
import model.Grid;
import model.Player;
import model.PlayerImpl;
import model.StandardCard;
import model.Value;
import provider.model.Cell;
import provider.model.GameCard;

public class GridAdapter implements provider.model.Grid {
  private final Grid grid;

  public GridAdapter(Grid grid) {
    this.grid = grid;
  }


  @Override
  public void createGrid(String grid) {
    // Only implement if needed
  }

  @Override
  public void playToGrid(GameCard card, int row, int col) {
    Card adaptedCard = adaptGameCardToCard(card);
    Player owner = adaptColorToPlayer(card.color());
    grid.placeCard(adaptedCard, row, col, owner);
  }

  @Override
  public int numCardCells() {
    return grid.getNumberOfCardCells();
  }

  @Override
  public boolean areAllCardCellsFilled() {
    return grid.isFull();
  }

  @Override
  public int countColorOnGrid(Color color) {
    int count = 0;
    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        model.Cell cell = grid.getCell(row, col);
        if (cell.isOccupied() && cell.getOwner().getColor().equals(colorToString(color))) {
          count++;
        }
      }
    }
    return count;
  }

  @Override
  public String getCellToString(int row, int col) {
    return grid.getCellToString(row, col);
  }

  @Override
  public Cell returnCell(int row, int col) {
    model.Cell cell = grid.getCell(row, col);
    return new CellAdapter(cell);
  }

  @Override
  public int numRows() {
    return grid.getRows();
  }

  @Override
  public int numCols() {
    return grid.getCols();
  }

  @Override
  public int countNeighbors(Cell cell) {
    // No neighbors used in our grid
    return 0;
  }

  @Override
  public List<Cell> cardCellList() {
    //
  }

  @Override
  public void connectNeighbors() {
    // No neighbors used in our implementation
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

  private Player adaptColorToPlayer(Color color) {
    String playerColor = color.equals(Color.RED) ? "Red" : "Blue";
    return new PlayerImpl(playerColor); // Create or get the corresponding Player instance
  }

  private String colorToString(Color color) {
    return color.equals(Color.RED) ? "Red" : "Blue";
  }
}
