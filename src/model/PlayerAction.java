package model;

/**
 * Interface for players to interact with the game during their turn.
 */
public interface PlayerAction {
  /**
   * Notify the player that it's their turn. The player must select a card
   * and a cell to play.
   *
   * @param model the read-only model of the game
   */
  void takeTurn(ReadOnlyGameModel model);
}
