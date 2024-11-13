package strategy;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy that prefers placing cards in the corner positions of the grid.
 */
public class CornerStrategy implements MoveStrategy {

  @Override
  public Move determineMove(ReadOnlyGameModel model, Player player) {
    List<int[]> corners = getCornerPositions(model);
    List<Move> possibleMoves = new ArrayList<>();

    for (int[] corner : corners) {
      int row = corner[0];
      int col = corner[1];

      // Call isLegalMove for every corner to ensure inspection
      model.isLegalMove(player, row, col); // This should log inspection in the mock

      if (model.isLegalMove(player, row, col)) {
        for (Card card : player.getHand()) {
          possibleMoves.add(new Move(card, row, col));
        }
      }
    }

    // Apply tie-breaking rules to select the best move
    return selectBestMove(possibleMoves);
  }


  private List<int[]> getCornerPositions(ReadOnlyGameModel model) {
    int maxRow = model.getGridRows() - 1;
    int maxCol = model.getGridCols() - 1;
    List<int[]> corners = new ArrayList<>();
    corners.add(new int[]{0, 0});             // Top-left
    corners.add(new int[]{0, maxCol});        // Top-right
    corners.add(new int[]{maxRow, 0});        // Bottom-left
    corners.add(new int[]{maxRow, maxCol});   // Bottom-right
    return corners;
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
