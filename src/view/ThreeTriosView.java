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
  private JPanel leftHandPanel;
  private JPanel rightHandPanel;

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
    leftHandPanel = new JPanel();
    rightHandPanel = new JPanel();

    frame.add(gridPanel, BorderLayout.CENTER);
    frame.add(leftHandPanel, BorderLayout.WEST);
    frame.add(rightHandPanel, BorderLayout.EAST);

    frame.setSize(800, 600);

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

    // Update hand panels
    leftHandPanel.removeAll();
    rightHandPanel.removeAll();
    setupHandPanels();

    frame.revalidate();
    frame.repaint();
  }


  private void setupGridPanel() {
    int rows = model.getGrid().getRows();
    int cols = model.getGrid().getCols();
    gridPanel.setLayout(new GridLayout(rows, cols, 0, 0));
    gridPanel.setPreferredSize(new Dimension(500, 500)); // Set preferred size

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        JPanel cellPanel = new JPanel();
        cellPanel.setPreferredSize(new Dimension(100, 100));
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

  private void setupHandPanels() {
    Player currentPlayer = model.getCurrentPlayer();
    Player playerRed = model.getPlayerRed();
    Player playerBlue = model.getPlayerBlue();

    // Get hands of both players
    List<Card> playerRedHand = playerRed.getHand();
    List<Card> playerBlueHand = playerBlue.getHand();

    // Left hand panel for player Red
    leftHandPanel.setLayout(new GridLayout(playerRedHand.size(), 1));
    leftHandPanel.setPreferredSize(new Dimension(150, 150 * playerRedHand.size()));
    boolean isRedTurn = currentPlayer.equals(playerRed);

    for (Card card : playerRedHand) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(Color.PINK);
      if (player.equals(playerRed) && isRedTurn) {
        // Only add listener if this view's player is Red and it's Red's turn
        cardPanel.addMouseListener(new CardClickListener(card));
      }
      leftHandPanel.add(cardPanel);
    }

    // Right hand panel for player Blue
    rightHandPanel.setLayout(new GridLayout(playerBlueHand.size(), 1));
    rightHandPanel.setPreferredSize(new Dimension(150, 150 * playerBlueHand.size()));
    boolean isBlueTurn = currentPlayer.equals(playerBlue);

    for (Card card : playerBlueHand) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(Color.CYAN);
      if (player.equals(playerBlue) && isBlueTurn) {
        // Only add listener if this view's player is Blue and it's Blue's turn
        cardPanel.addMouseListener(new CardClickListener(card));
      }
      rightHandPanel.add(cardPanel);
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
