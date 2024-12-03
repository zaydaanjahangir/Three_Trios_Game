package provider.model;

/**
 * This enumerated type represents the different directions
 * allowed in the game, along with a string description associated with each
 * of them.
 */
public enum Direction {
  NORTH("North"), SOUTH("South"), EAST("East"), WEST("West");

  private final String descriptor;

  /**
   * Creates a new Direction object with the description of its direction.
   * @param d string representation of this direction
   */
  Direction(String d) {
    this.descriptor = d;
  }

  /**
   * Returns the descriptor string of this Direction.
   * @return String representing the Direction descriptor.
   */
  public String toString() {
    return descriptor;
  }

}
