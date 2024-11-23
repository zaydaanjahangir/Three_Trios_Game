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
    List<Card> player1 = model.getCurrentPlayer().getHand();
    List<Card> player2 = model.getOpponentPlayer().getHand();

    JPanel leftHandPanel = new JPanel(new GridLayout(player1.size(), 1));
    leftHandPanel.setPreferredSize(new Dimension(100, 50 * player1.size()));

    for (Card card : player1) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(Color.PINK);
      cardPanel.addMouseListener(new CardClickListener(card, cardPanel));
      leftHandPanel.add(cardPanel);
    }
    frame.add(leftHandPanel, BorderLayout.WEST);

    JPanel rightHandPanel = new JPanel(new GridLayout(player2.size(), 1));
    rightHandPanel.setPreferredSize(new Dimension(100, 50 * player2.size()));

    for (Card card : player2) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(Color.CYAN);
      cardPanel.addMouseListener(new CardClickListener(card, cardPanel));
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
    frame.getContentPane().removeAll();       // Clear the frame
    setupGridPanel();                         // Rebuild the grid panel
    setupHandPanels();                        // Rebuild the hand panels
    frame.revalidate();                       // Refresh the frame
    frame.repaint();                          // Redraw the frame
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



