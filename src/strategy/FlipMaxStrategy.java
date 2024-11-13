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

//    System.out.println("Starting determineMove for player: " + player.getColor());
//    System.out.println("Player's hand: " + player.getHand());

    for (Card card : player.getHand()) {
//      System.out.println("Evaluating card: " + card.getName());

      for (int row = 0; row < model.getGridRows(); row++) {
        for (int col = 0; col < model.getGridCols(); col++) {
          if (model.isLegalMove(player, row, col)) {
            int flips = model.getPotentialFlips(player, card, row, col);
//            System.out.println("Legal move at (" + row + ", " + col + ") with card " + card.getName() + " flips: " + flips);

            if (flips > maxFlips) {
              maxFlips = flips;
              bestMoves.clear();
              bestMoves.add(new Move(card, row, col));
//              System.out.println("New maxFlips found: " + maxFlips + ". Updating bestMoves.");
            } else if (flips == maxFlips) {
              bestMoves.add(new Move(card, row, col));
//              System.out.println("Found move with equal flips: " + flips + ". Adding to bestMoves.");
            }
          } else {
//            System.out.println("Position (" + row + ", " + col + ") is not a legal move for card " + card.getName());
          }
        }
      }
    }

    System.out.println("Best moves found: " + bestMoves);
//    System.out.println("Selecting best move using tie-breaking rules.");

    // Tie-breaking rules
    Move selectedMove = selectBestMove(bestMoves);
    if (selectedMove != null) {
//      System.out.println("Selected move: (" + selectedMove.getRow() + ", " + selectedMove.getCol() + ") with card " + selectedMove.getCard().getName());
    } else {
//      System.out.println("No valid moves found.");
    }
    return selectedMove;
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
