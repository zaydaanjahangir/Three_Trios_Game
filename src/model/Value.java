package model;

/**
 * Enum representing attack values for a card.
 * Values range from ONE to A (representing 1 to 10).
 */
public enum Value {
  ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5),
  SIX(6), SEVEN(7), EIGHT(8), NINE(9), A(10);

  private final int intValue;

  Value(int intValue) {
    this.intValue = intValue;
  }

  /**
   * Gets the integer value of the attack value.
   *
   * @return the integer representation of the attack value.
   */
  public int getIntValue() {
    return intValue;
  }

  @Override
  public String toString() {
    return this == A ? "A" : String.valueOf(intValue);
  }
}
