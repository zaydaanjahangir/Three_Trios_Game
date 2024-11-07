package model;

/**
 * Factory for creating Card instances.
 */
public class CardFactory {

  /**
   * Creates a standard card with given name and values.
   *
   * @param name  the card's name.
   * @param north value on the north side.
   * @param south value on the south side.
   * @param east  value on the east side.
   * @param west  value on the west side.
   * @return a new Card instance.
   */
  public static Card createCard(String name, Value north, Value south, Value east, Value west) {
    return new StandardCard(name, north, south, east, west);
  }
}


