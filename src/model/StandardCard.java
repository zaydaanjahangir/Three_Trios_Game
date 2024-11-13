package model;

/**
 * Implementation of the Card interface which represent the cards used to play Three Trios.
 */
public class StandardCard implements Card {
  private String name;
  private Value northValue;
  private Value southValue;
  private Value eastValue;
  private Value westValue;

  /**
   * Constructor for creating a StandardCard type.
   *
   * @param name  name of the card
   * @param north north value
   * @param south south value
   * @param east  east value
   * @param west  west value
   */
  public StandardCard(String name, Value north, Value south, Value east, Value west) {
    this.name = name;
    this.northValue = north;
    this.southValue = south;
    this.eastValue = east;
    this.westValue = west;
  }

  @Override
  public boolean compareAgainst(Card opponent, Direction direction) {
    Value thisValue;
    Value opponentValue;
    switch (direction) {
      case NORTH:
        thisValue = this.getNorthValue();
        opponentValue = opponent.getSouthValue();
        break;
      case SOUTH:
        thisValue = this.getSouthValue();
        opponentValue = opponent.getNorthValue();
        break;
      case EAST:
        thisValue = this.getEastValue();
        opponentValue = opponent.getWestValue();
        break;
      case WEST:
        thisValue = this.getWestValue();
        opponentValue = opponent.getEastValue();
        break;
      default:
        throw new IllegalArgumentException("Invalid direction");
    }
    return thisValue.getIntValue() > opponentValue.getIntValue();
  }


  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Value getNorthValue() {
    return this.northValue;
  }

  @Override
  public Value getSouthValue() {
    return this.southValue;
  }

  @Override
  public Value getEastValue() {
    return this.eastValue;
  }

  @Override
  public Value getWestValue() {
    return this.westValue;
  }
}

