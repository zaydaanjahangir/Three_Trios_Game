package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Card;

/**
 * A custom JPanel to display a card with its values on the north, south, east, and west sides.
 */
public class CardPanel extends JPanel {
  /**
   * Constructor for the CardPanel class which sets everything up.
   * @param card StandardCard.
   */
  public CardPanel(Card card) {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    setBorder(new javax.swing.border.LineBorder(Color.BLACK, 1));

    Font font = new Font("SansSerif", Font.BOLD, 12);

    // North Value
    JLabel northLabel = new JLabel(card.getNorthValue().toString());
    northLabel.setFont(font);
    GridBagConstraints northConstraints = new GridBagConstraints();
    northConstraints.gridx = 1;
    northConstraints.gridy = 0;
    add(northLabel, northConstraints);

    // South Value
    JLabel southLabel = new JLabel(card.getSouthValue().toString());
    southLabel.setFont(font);
    GridBagConstraints southConstraints = new GridBagConstraints();
    southConstraints.gridx = 1;
    southConstraints.gridy = 2;
    add(southLabel, southConstraints);

    // East Value
    JLabel eastLabel = new JLabel(card.getEastValue().toString());
    eastLabel.setFont(font);
    GridBagConstraints eastConstraints = new GridBagConstraints();
    eastConstraints.gridx = 2;
    eastConstraints.gridy = 1;
    add(eastLabel, eastConstraints);

    // West Value
    JLabel westLabel = new JLabel(card.getWestValue().toString());
    westLabel.setFont(font);
    GridBagConstraints westConstraints = new GridBagConstraints();
    westConstraints.gridx = 0;
    westConstraints.gridy = 1;
    add(westLabel, westConstraints);
  }
}
