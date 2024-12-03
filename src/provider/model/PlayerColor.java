package provider.model;

/**
 * This enumerated type represents the different player colors
 * allowed in the game, along with a string description associated with each
 * of them.
 */
public enum PlayerColor {
  RED("Red"), BLUE("Blue");

  private final String descriptor;

  /**
   * Creates a new PlayerColor object with the description of which color.
   * @param d string representation of this PlayerColor
   */
  PlayerColor(String d) {
    this.descriptor = d;
  }

  /**
   * Returns the descriptor string of this PlayerColor.
   * @return String representing the PlayerColor descriptor.
   */
  public String toString() {
    return descriptor;
  }

}