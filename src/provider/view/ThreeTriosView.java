package provider.view;

/**
 * Behaviors needed for a view of the RedSeven implementation
 * that transmits information to the user.
 */
public interface ThreeTriosView {

  /**
   * Creates a String with state of the game.
   * This rendering includes
   * <ul>
   *   <li>The current player </li>
   *   <li>Each row from R1 to Rn where n is the number of rows, where each row has a marker for
   *   each of the number of columns printed out. _ marks an empty card cell, " " marks a hole
   *   cell and R marks red player and B marks blue player</li>
   *   <li>The hand, where all cards are printed out on a separate line with their name and
   *   values for each of the directions. The hand is printed out based on who the current
   *   player is.</li>
   * </ul>
   * An example below for a 5 row x 6 column game, 8-hand game in-progress
   * Player: BLUE
   * BB   _
   * _B   _
   * _ R  _
   * _  _ _
   * _   _R
   * Hand:
   * CorruptKing 7 3 9 A
   * AngryDragon 2 8 9 9
   * WindBird 7 2 5 3
   * HeroKnight A 2 4 4
   * WorldDragon 8 3 5 7
   * SkyWhale 4 5 9 9
   *
   * @return a string rendering of the game that is displayed to the user
   */
  String toString();
}