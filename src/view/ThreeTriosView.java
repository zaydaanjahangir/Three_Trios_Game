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
  private CardPanel selectedCardPanel;

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

    leftHandPanel = new JPanel(new GridLayout(player1.size(), 1));
    leftHandPanel.setPreferredSize(new Dimension(100, 50 * player1.size()));

    for (Card card : player1) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(Color.PINK);
      cardPanel.addMouseListener(new CardClickListener(card, cardPanel));
      leftHandPanel.add(cardPanel);
    }
    frame.add(leftHandPanel, BorderLayout.WEST);

    rightHandPanel = new JPanel(new GridLayout(player2.size(), 1));
    rightHandPanel.setPreferredSize(new Dimension(100, 50 * player2.size()));

    for (Card card : player2) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBackground(Color.CYAN);
      cardPanel.addMouseListener(new CardClickListener(card, cardPanel));
      rightHandPanel.add(cardPanel);
    }
    frame.add(rightHandPanel, BorderLayout.EAST);
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
        if (selectedCard == card) {
          if (selectedCardPanel != null) {
            selectedCardPanel.setBorder(null);
          }
          selectedCard = null;
          selectedCardPanel = null;
          System.out.println("Deselected card: " + card.getName());
        } else {
          if (selectedCardPanel != null) {
            selectedCardPanel.setBorder(null);
          }
          selectedCard = card;
          selectedCardPanel = cardPanel;
          cardPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
          System.out.println("Selected card: " + card.getName());
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
        if (selectedCard != null && cellPanel.getBackground() == Color.YELLOW) {
          cellPanel.setBackground(selectedCardPanel.getBackground());
          cellPanel.setLayout(new BorderLayout());

          JLabel northLabel = new JLabel(String.valueOf(selectedCard.getNorthValue()), JLabel.CENTER);
          JLabel southLabel = new JLabel(String.valueOf(selectedCard.getSouthValue()), JLabel.CENTER);
          JLabel eastLabel = new JLabel(String.valueOf(selectedCard.getEastValue()));
          JLabel westLabel = new JLabel(String.valueOf(selectedCard.getWestValue()));

          cellPanel.add(northLabel, BorderLayout.NORTH);
          cellPanel.add(eastLabel, BorderLayout.EAST);
          cellPanel.add(southLabel, BorderLayout.SOUTH);
          cellPanel.add(westLabel, BorderLayout.WEST);

          cellPanel.revalidate();
          cellPanel.repaint();

          System.out.println("Placed " + selectedCard.getName() + " at (" + row + ", " + col + ")");

          JPanel parentPanel = (JPanel) selectedCardPanel.getParent();
          parentPanel.remove(selectedCardPanel);
          parentPanel.revalidate();
          parentPanel.repaint();

          selectedCard = null;
          selectedCardPanel = null;
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



