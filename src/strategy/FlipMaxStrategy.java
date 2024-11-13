package strategy;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy that selects the move which flips the maximum number of opponent's cards.
 */
public class FlipMaxStrategy implements MoveStrategy {

  @Override
  public Move determineMove(ReadOnlyGameModel model, Player player) {
    int maxFlips = -1;
    List<Move> bestMoves = new ArrayList<>();

    for (Card card : player.getHand()) {
      for (int row = 0; row < model.getGridRows(); row++) {
        for (int col = 0; col < model.getGridCols(); col++) {
          if (model.isLegalMove(player, row, col)) {
            int flips = model.getPotentialFlips(player, card, row, col);
            if (flips > maxFlips) {
              maxFlips = flips;
              bestMoves.clear();
              bestMoves.add(new Move(card, row, col));
            } else if (flips == maxFlips) {
              bestMoves.add(new Move(card, row, col));
            }
          }
        }
      }
    }

    // Tie-breaking rules
    return selectBestMove(bestMoves);
  }

  private Move selectBestMove(List<Move> moves) {
    if (moves.isEmpty()) {
      return null; // No valid moves
    }
    // Choose the move with the uppermost-leftmost coordinate and the card with the lowest index
    Move bestMove = moves.get(0);
    for (Move move : moves) {
      if (move.getRow() < bestMove.getRow() ||
              (move.getRow() == bestMove.getRow() && move.getCol() < bestMove.getCol())) {
        bestMove = move;
      }
    }
    return bestMove;
  }
}
