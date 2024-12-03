package provider.view;

import java.awt.*;

import javax.swing.*;

import provider.model.GameCard;
import provider.model.PlayerColor;

/**
 * Draws a card button in the ThreeTrios game. This button represents the button that can
 * be interacted with in the game and when pressed, responds accordingly to the rules.
 */
public class CardButton extends JButton implements ButtonInterface {

  private int north;
  private int south;
  private int east;
  private int west;
  private boolean isSelected;
  private GameCard card;
  private boolean canSelect;


  /**
   * Constructs a CardButton object.
   * @param card the GameCard that is being referenced to draw
   * @param color the color that will be set as the background
   */
  public CardButton(GameCard card, PlayerColor color, boolean canSelect) {
    if (card != null) {
      this.north = card.north();
      this.south = card.south();
      this.east = card.east();
      this.west = card.west();
      this.isSelected = false;
      this.card = card;

      this.setPreferredSize(new Dimension(80, 120));
      this.setOpaque(true);
      this.setContentAreaFilled(true);
      this.setBorderPainted(true);
      this.setFocusPainted(false);

      // Setting the background color of this card button.
      if (color.equals(PlayerColor.RED)) {
        this.setBackground(Color.PINK);
      } else {
        this.setBackground(Color.getHSBColor(0.55f, 0.25f, 0.9f));
      }

      this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }
    else {
      this.isSelected = false;
      this.canSelect = canSelect;

      this.setPreferredSize(new Dimension(80, 120));
      this.setOpaque(true);
      this.setContentAreaFilled(true);
      this.setFocusPainted(false);
      this.setBackground(Color.getHSBColor(0.16f, 0.3f, 1.0f));
      this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }
    /*
     *     // EXAMPLE OF ADDING ACTION LISTENER. We don't need command callback.
     *     commandButton = new JButton("Execute");
     *     commandButton.addActionListener((ActionEvent e) ->
     *     {
     *       if (commandCallback != null) { //if there is a command callback
     *         commandCallback.accept(input.getText()); //send command to be processed
     *         input.setText(""); //clear the input text field
     *       }
     *     });
     *     buttonPanel.add(commandButton);
     */
  }

  /**
   * This method updates the selected card in the players hand and changes which card appears to
   * be selected once a new one is clicked.
   */
  public boolean updateSelectionEmptyCard(GameCard card, PlayerColor color) {
    // If isSelected is true, change it to false.
    // If isSelected is false, change it to true.
    if ((card.color() == Color.RED && color == PlayerColor.RED) ||
            (card.color() == Color.BLUE && color == PlayerColor.BLUE)) {
      this.isSelected = !isSelected;

      if (isSelected && this.isEnabled()) {
        // Change border and background color when selected
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
      }
      else {
        // Revert to default appearance
        this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
      }
    }
    return this.isSelected;
  }

  /**
   * Updates the card at this button location and retunrs the updated card when a new card has
   * been placed.
   * @param newCard the updated card at this location.
   */
  public void updateCard(GameCard newCard) {
    this.card = newCard;
    this.north = card.north();
    this.south = card.south();
    this.east = card.east();
    this.west = card.west();

    // redisplay the card and repaint it
    this.repaint();

  }

  public void deselect() {
    isSelected = false;
    this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
  }

  /**
   * Paints the button, overrides the paintComponent method for a button.
   * @param g the Graphics object to protect
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    if (this.card != null) {
      g2d.drawString(Integer.toString(north), getWidth() / 2 - 5, 30);
      g2d.drawString(Integer.toString(south), getWidth() / 2 - 5, getHeight() - 30);
      g2d.drawString(Integer.toString(east), getWidth() - 25, getHeight() / 2);  // East
      g2d.drawString(Integer.toString(west), 10, getHeight() / 2);  // West
      if (color() == PlayerColor.RED) {
        this.setBackground(Color.PINK);
      } else {
        this.setBackground(Color.getHSBColor(0.55f, 0.25f, 0.9f));
      }
    }
  }

  public GameCard card() {
    return this.card.returnCard();
  }

  /**
   * Returns the player color of the button.
   * @return the color associated with the button.
   */
  public PlayerColor color() {
    if (this.card.color().equals(Color.RED)) {
      return PlayerColor.RED;
    } else {
      return PlayerColor.BLUE;
    }
  }

  // needs to send an action listener to the view to show that a card has been clicked so that
  // the grid cna be clicked on
  public boolean isSelected() {
    return this.isSelected;
  }

  /**
   * Updates the selection of the button. If it was pressed before- now it is not.
   */
  public void updateSelectionEmptyCard() {
    // If isSelected is true, change it to false.
    // If isSelected is false, change it to true.
    if (this.canSelect && this.isEnabled()) {
      this.isSelected = !isSelected;

      if (isSelected) {
        // Change border and background color when selected
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
      } else {
        // Revert to default appearance
        this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
      }
    }
  }

  public void notEnabled() {
    this.setEnabled(!this.isEnabled());
  }

  public GameCard returnCard() {
    return this.card.returnCard();
  }

}

// what to do:
// need to have the hand not be able to be selected once the player's turn is changed
// model -> controller -> view -> individual buttons on that hnad need to be unable to be clicked