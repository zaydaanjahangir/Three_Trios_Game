package strategy;

import model.ReadOnlyGameModel;
import model.Player;
import model.Move;

/**
 * Interface representing a strategy for selecting a move in the Three Trios game.
 */
public interface MoveStrategy {
  /**
   * Determines the best move for the given player based on the strategy.
   *
   * @param model  the read-only model of the game
   * @param player the player for whom the move is being determined
   * @return the chosen move
   */
  Move determineMove(ReadOnlyGameModel model, Player player);
}
