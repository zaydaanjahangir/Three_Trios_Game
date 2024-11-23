package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Features;
import model.Card;
import model.Cell;
import model.Player;
import model.ReadOnlyGameModel;

/**
 * The view class of the Three Trios game, providing the visual interface for a player.
 */
public class ThreeTriosView implements ThreeTriosViewInterface {
  private final ReadOnlyGameModel model;
  private final Player player;
  private Features features;
  private JFrame frame;
  private JPanel gridPanel;
  private JPanel handPanel;

  public ThreeTriosView(ReadOnlyGameModel model, Player player) {
    this.model = model;
    this.player = player;
    initialize();
  }

  private void initialize() {
    frame = new JFrame("Three Trios Game - " + player.getColor());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    gridPanel = new JPanel();
    handPanel = new JPanel();

    frame.add(gridPanel, BorderLayout.CENTER);
    frame.add(handPanel, BorderLayout.SOUTH);

    frame.pack();
    frame.setVisible(true);

    updateView();
  }

  @Override
  public void updateView() {
    // Update the frame title to reflect whose turn it is
    String currentPlayerColor = model.getCurrentPlayer().getColor();
    frame.setTitle("Three Trios Game - " + player.getColor() +
            (player.getColor().equals(currentPlayerColor) ? " (Your Turn)" : " (Waiting)"));

    // Update grid panel
    gridPanel.removeAll();
    setupGridPanel();

    // Update hand panel
    handPanel.removeAll();
    setupHandPanel();

    frame.revalidate();
    frame.repaint();
  }

  private void setupGridPanel() {
    int rows = model.getGrid().getRows();
    int cols = model.getGrid().getCols();
    gridPanel.setLayout(new GridLayout(rows, cols, 0, 0));

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        JPanel cellPanel = new JPanel();
        cellPanel.setPreferredSize(new Dimension(150, 150));
        cellPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

        Cell cell = model.getGrid().getCell(row, col);
        if (cell.isHole()) {
          cellPanel.setBackground(Color.GRAY);
        } else if (cell.isOccupied()) {
          Player owner = cell.getOwner();
          cellPanel.setBackground(owner.getColor().equals("Red") ? Color.PINK : Color.CYAN);
          // Optionally display card info here
        } else {
          cellPanel.setBackground(Color.YELLOW);
          if (model.getCurrentPlayer().equals(player)) {
            cellPanel.addMouseListener(new CellClickListener(row, col));
          }
        }

        gridPanel.add(cellPanel);
      }
    }
  }

  private void setupHandPanel() {
    List<Card> hand = player.getHand();
    handPanel.setLayout(new GridLayout(1, hand.size()));
    boolean isPlayerTurn = model.getCurrentPlayer().equals(player);

    for (Card card : hand) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(player.getColor().equals("Red") ? Color.PINK : Color.CYAN);
      if (isPlayerTurn) {
        cardPanel.addMouseListener(new CardClickListener(card));
      }
      handPanel.add(cardPanel);
    }
  }

  @Override
  public void addFeatures(Features features) {
    this.features = features;
  }

  @Override
  public void showErrorMessage(String message) {
    javax.swing.JOptionPane.showMessageDialog(frame, message, "Error",
            javax.swing.JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void showGameOverMessage(String message) {
    javax.swing.JOptionPane.showMessageDialog(frame, message, "Game Over",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void setVisible(boolean visible) {
    frame.setVisible(visible);
  }

  private class CardClickListener extends MouseAdapter {
    private final Card card;

    public CardClickListener(Card card) {
      this.card = card;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (features != null) {
        features.cardSelected(card);
      }
    }
  }

  private class CellClickListener extends MouseAdapter {
    private final int row;
    private final int col;

    public CellClickListener(int row, int col) {
      this.row = row;
      this.col = col;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (features != null) {
        features.cellSelected(row, col);
      }
    }
  }
}
