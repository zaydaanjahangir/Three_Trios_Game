package adapters;

import java.awt.*;

import model.Card;
import model.Cell;
import model.Player;
import model.PlayerImpl;
import model.StandardCard;
import model.Value;
import provider.model.Direction;
import provider.model.GameCard;
import provider.model.PlayerColor;

public class CellAdapter implements provider.model.Cell {
  private final Cell cell;
  private final int row;
  private final int col;


  public CellAdapter(Cell cell, int row, int col) {
    this.cell = cell;
    this.row = row;
    this.col = col;
  }


  @Override
  public void playCardToCell(GameCard card) {
    Card adaptedCard = adaptGameCardToCard(card);
    Player owner = adaptColorToPlayer(card.color());
    cell.placeCard(adaptedCard, owner);
  }

  @Override
  public boolean isFilledCellCardCell() {
    return cell.isOccupied();
  }

  @Override
  public boolean isCellHoleCell() {
    return cell.isHole();
  }

  @Override
  public void addNeighbor(Direction d, GameCard c) {
    // Our model doesn't support neighbors
  }

  @Override
  public Color retreiveColor() {
    if (cell.isOccupied()) {
      String color = cell.getOwner().getColor();
      return color.equals("Red") ? Color.RED : Color.BLUE;
    } else {
      return null;
    }
  }

  @Override
  public String getCellToString() {
    if (cell.isHole()) {
      return " ";
    } else if (cell.isOccupied()) {
      return retreiveColor().equals(Color.RED) ? "R" : "B";
    } else {
      return "_";
    }
  }

  @Override
  public provider.model.Cell returnCell() {
    return new CellAdapter(cell, row, col);
  }

  @Override
  public GameCard returnFilledCellCard() {
    if (cell.isOccupied()) {
      Card card = cell.getCard();
      Player owner = cell.getOwner();
      return new GameCardAdapter(card, owner);
    } else {
      return null;
    }
  }

  @Override
  public int row() {
    return this.row;
  }

  @Override
  public int col() {
    return this.col;
  }

  @Override
  public void updateThisCardToThatNeighbors(Direction direction, provider.model.Cell cell) {
    // Implement neighbor updates if necessary
  }

  @Override
  public void refreshColorIfFilledCardCell() {
    // Implement if needed
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
    return new PlayerImpl(playerColor);
  }
}
