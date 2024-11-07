package view;

import model.Card;
import model.Cell;
import model.GameModel;
import model.Grid;
import model.Player;
import model.Value;

/**
 * Provides a text-based visual to the state of the game.
 */
public class TextView {
  private final GameModel model;

  /**
   * Constructor for creating the view.
   *
   * @param model GameModel
   */
  public TextView(GameModel model) {
    this.model = model;
  }

  /**
   * Generates the textual representation of the current game state.
   *
   * @return a String representing the game state.
   */
  public String render() {
    StringBuilder output = new StringBuilder();

    Player currentPlayer = model.getCurrentPlayer();
    output.append("Player: ").append(currentPlayer.getColor().toUpperCase()).append("\n");

    output.append(renderGrid());

    output.append("Hand:\n");
    output.append(renderHand(currentPlayer));
    return output.toString();
  }

  private String renderGrid() {
    StringBuilder gridOutput = new StringBuilder();
    Grid grid = model.getGrid();
    int rows = grid.getRows();
    int cols = grid.getCols();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Cell cell = grid.getCell(row, col);
        if (cell.isHole()) {
          gridOutput.append(' '); // Hole
        } else if (!cell.isOccupied()) {
          gridOutput.append('_'); // Empty cell
        } else {
          Player owner = cell.getOwner();
          if (owner.getColor().equalsIgnoreCase("Red")) {
            gridOutput.append('R');
          } else if (owner.getColor().equalsIgnoreCase("Blue")) {
            gridOutput.append('B');
          } else {
            gridOutput.append('?'); // Unknown owner
          }
        }
      }
      gridOutput.append('\n');
    }
    return gridOutput.toString();
  }

  private String renderHand(Player player) {
    StringBuilder handOutput = new StringBuilder();
    for (Card card : player.getHand()) {
      handOutput.append(card.getName()).append(' ')
              .append(valueToString(card.getNorthValue())).append(' ')
              .append(valueToString(card.getSouthValue())).append(' ')
              .append(valueToString(card.getEastValue())).append(' ')
              .append(valueToString(card.getWestValue())).append('\n');
    }
    return handOutput.toString();
  }

  private String valueToString(Value value) {
    if (value == Value.A) {
      return "A";
    } else {
      return String.valueOf(value.getIntValue());
    }
  }
}
