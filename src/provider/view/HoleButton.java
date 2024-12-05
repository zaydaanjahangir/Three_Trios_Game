package provider.view;


import java.awt.*;

import javax.swing.*;

/**
 * Draws a Hole button in the ThreeTrios game. This button represents the button when
 * interacted with in the game and when pressed, should not do anything.
 */
public class HoleButton extends JButton {

  /**
   * Constructs an HoleButton object, where the cell color is gray.
   */
  public HoleButton() {
    this.setPreferredSize(new Dimension(80, 120));
    this.setOpaque(true);
    this.setContentAreaFilled(true);
    this.setFocusPainted(false);
    this.setBackground(Color.GRAY);
    this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
  }
}
