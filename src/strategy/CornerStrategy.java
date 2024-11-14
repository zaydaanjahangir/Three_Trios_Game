package strategy;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Move;
import model.Player;
import model.ReadOnlyGameModel;

/**
 * Strategy that prefers placing cards in the corner positions of the grid.
 */
public class CornerStrategy implements MoveStrategy {

  /**
   * Determines the best move based on the available corners from upper-leftmost priority.
   *
   * @param model  the read-only model of the game
   * @param player the player for whom the move is being determined
   * @return the best corner move if possible
   */
  @Override
  public Move determineMove(ReadOnlyGameModel model, Player player) {
    List<int[]> corners = getCornerPositions(model);
    List<Move> possibleMoves = new ArrayList<>();

    for (int[] corner : corners) {
      int row = corner[0];
      int col = corner[1];

      model.isLegalMove(player, row, col);

      if (model.isLegalMove(player, row, col)) {
        for (Card card : player.getHand()) {
          possibleMoves.add(new Move(card, row, col));
        }
      }
    }

    // tiebreak
    return selectBestMove(possibleMoves);
  }


  private List<int[]> getCornerPositions(ReadOnlyGameModel model) {
    int maxRow = model.getGridRows() - 1;
    int maxCol = model.getGridCols() - 1;
    List<int[]> corners = new ArrayList<>();
    corners.add(new int[]{0, 0});
    corners.add(new int[]{0, maxCol});
    corners.add(new int[]{maxRow, 0});
    corners.add(new int[]{maxRow, maxCol});
    return corners;
  }

  private Move selectBestMove(List<Move> moves) {
    if (moves.isEmpty()) {
      return null; // No valid moves
    }
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
