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
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Card;
import model.ReadOnlyGameModel;

public class ThreeTriosView {
  private final ReadOnlyGameModel model;
  private JFrame frame;
  private JPanel gridPanel;
  private JPanel leftHandPanel;
  private JPanel rightHandPanel;
  private Card selectedCard;

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
    gridPanel = new JPanel(new GridLayout(rows, cols, 0, 0));

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        JPanel cellPanel = new JPanel();
        cellPanel.setPreferredSize(new Dimension(50, 50));
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

    leftHandPanel = new JPanel(new GridLayout(player1.size(), 1));
    leftHandPanel.setPreferredSize(new Dimension(50, 50 * player1.size()));

    for (Card card : player1) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(Color.PINK);
      cardPanel.addMouseListener(new CardClickListener(card));
      leftHandPanel.add(cardPanel);
    }
    frame.add(leftHandPanel, BorderLayout.WEST);

    rightHandPanel = new JPanel(new GridLayout(player2.size(), 1));
    rightHandPanel.setPreferredSize(new Dimension(50, 50 * player2.size()));

    for (Card card : player2) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(Color.CYAN);
      cardPanel.addMouseListener(new CardClickListener(card));
      rightHandPanel.add(cardPanel);
    }
    frame.add(rightHandPanel, BorderLayout.EAST);
  }

  private class CardClickListener extends MouseAdapter {
    private final Card card;

    public CardClickListener(Card card) {
      this.card = card;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      selectedCard = card;
      System.out.println("Selected card: " + card.getName());
    }
  }

  private class CellClickListener extends MouseAdapterC {
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
      if (selectedCard != null && cellPanel.getBackground() == Color.YELLOW) {
        cellPanel.setBackground(selectedCard.getName().contains("Player1") ? Color.PINK : Color.CYAN);
        JLabel cardLabel = new JLabel(String.format("%s %s %s %s", selectedCard.getNorthValue(), selectedCard.getEastValue(), selectedCard.getSouthValue(), selectedCard.getWestValue()));
        cellPanel.add(cardLabel);
        cellPanel.revalidate();
        cellPanel.repaint();

        System.out.println("Placed " + selectedCard.getName() + " at (" + row + ", " + col + ")");
        selectedCard = null;
      } else if (selectedCard == null) {
        System.out.println("No card selected.");
      } else {
        System.out.println("Cannot place a card here.");
      }
    }
  }
  public void setVisible(boolean visible) {
    frame.setVisible(visible);
  }
}
