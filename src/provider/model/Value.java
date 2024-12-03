package provider.model;

/**
 * This enumerated type represents the different values
 * allowed in the game, along with a string description associated with each
 * of them.
 */
public enum Value {
  ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"),
  SEVEN("7"), EIGHT("8"), NINE("9"), A("A");

  private final String descriptor;

  /**
   * Creates a new Value object with the description of its value.
   * @param d string representation of this value
   */
  Value(String d) {

    this.descriptor = d;
  }

  /**
   * Returns this value in string format.
   * @return a string of this value
   */
  public String value() {
    return this.descriptor;
  }

  /**
   * Converts this value to integer format.
   * @return an integer of this value
   */
  public int toInt() {
    if (this.descriptor.equals("A")) {
      return 10;
    }
    else {
      return Integer.parseInt(this.descriptor);
    }
  }

  /**
   * Converts this string descriptor to its respective Value.
   * @param descriptor a string representation of the descriptor of a possible Value
   * @return a Value matched to the passed in string
   */
  public static Value toValue(String descriptor) {
    switch (descriptor) {
      // turn this into a factory!!
      case "1":
        return Value.ONE;
      case "2":
        return Value.TWO;
      case "3":
        return Value.THREE;
      case "4":
        return Value.FOUR;
      case "5":
        return Value.FIVE;
      case "6":
        return Value.SIX;
      case "7":
        return Value.SEVEN;
      case "8":
        return Value.EIGHT;
      case "9":
        return Value.NINE;
      case "A":
        return Value.A;
      default:
        throw new IllegalArgumentException("Invalid enum");
    }
  }
}