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
import model.Player;
import model.ReadOnlyGameModel;

/**
 * The view class of the ThreeTriosGame contributing the visual aspect via JPanel.
 */
public class ThreeTriosView implements ThreeTriosViewInterface {
  private final ReadOnlyGameModel model;
  private JFrame frame;
  private Card selectedCard;
  private CardPanel selectedCardPanel;
  private Features features;

  public ThreeTriosView(ReadOnlyGameModel model) {
    this.model = model;
    initialize();
  }

  private void initialize() {
    frame = new JFrame("Three Trios Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    String currentPlayerColor = model.getCurrentPlayer().getColor();
    frame.setTitle("Three Trios Game - " + currentPlayerColor + "'s Turn");

    setupGridPanel();
    setupHandPanels();

    frame.pack();
    frame.setVisible(true);
  }

  private void setupGridPanel() {
    int rows = model.getGrid().getRows();
    int cols = model.getGrid().getCols();
    JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 0, 0));

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        JPanel cellPanel = new JPanel();
        cellPanel.setPreferredSize(new Dimension(150, 150));
        cellPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

        if (model.getGrid().getCell(row, col).isHole()) {
          cellPanel.setBackground(Color.GRAY);
        } else {
          cellPanel.setBackground(Color.YELLOW);
          cellPanel.addMouseListener(new CellClickListener(row, col, cellPanel));
        }

        gridPanel.add(cellPanel);
      }
    }
    frame.add(gridPanel, BorderLayout.CENTER);
  }

  private void setupHandPanels() {
    Player currentPlayer = model.getCurrentPlayer();

    List<Card> player1Hand = model.getPlayerHand(model.getPlayerRed());
    List<Card> player2Hand = model.getPlayerHand(model.getPlayerBlue());

    // Left hand panel for player Red
    JPanel leftHandPanel = new JPanel(new GridLayout(player1Hand.size(), 1));
    leftHandPanel.setPreferredSize(new Dimension(100, 50 * player1Hand.size()));
    boolean isPlayer1Turn = currentPlayer.equals(model.getPlayerRed());

    for (Card card : player1Hand) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(Color.PINK);
      if (isPlayer1Turn) {
        cardPanel.addMouseListener(new CardClickListener(card, cardPanel));
      }
      leftHandPanel.add(cardPanel);
    }
    frame.add(leftHandPanel, BorderLayout.WEST);

    // Right hand panel for player Blue
    JPanel rightHandPanel = new JPanel(new GridLayout(player2Hand.size(), 1));
    rightHandPanel.setPreferredSize(new Dimension(100, 50 * player2Hand.size()));
    boolean isPlayer2Turn = currentPlayer.equals(model.getPlayerBlue());

    for (Card card : player2Hand) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(Color.CYAN);
      if (isPlayer2Turn) {
        cardPanel.addMouseListener(new CardClickListener(card, cardPanel));
      }
      rightHandPanel.add(cardPanel);
    }
    frame.add(rightHandPanel, BorderLayout.EAST);
  }


  @Override
  public void addFeatures(Features features) {
    this.features = features;
  }

  @Override
  public void updateView() {
    frame.getContentPane().removeAll(); // Clear existing components
    initialize();                       // Rebuild the UI components based on the updated model
    frame.revalidate();
    frame.repaint();
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


  private class CardClickListener extends MouseAdapter {
    private final Card card;
    private final CardPanel cardPanel;

    public CardClickListener(Card card, CardPanel cardPanel) {
      this.card = card;
      this.cardPanel = cardPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (features != null) {
        features.cardSelected(card); // Notify the controller of the selected card
        System.out.println("Selected card: " + card.getName()); // Optional debug output
      }
    }
  }

  private class CellClickListener extends MouseAdapter {
    private final int row;
    private final int col;
    private final JPanel cellPanel;

    public CellClickListener(int row, int col, JPanel cellPanel) {
      this.row = row;
      this.col = col;
      this.cellPanel = cellPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (features != null) {
        features.cellSelected(row, col);
        System.out.println("Clicked on cell: (" + row + ", " + col + ")"); // debug output
      }
    }
  }

  public void setVisible(boolean visible) {
    frame.setVisible(visible);
  }
}



