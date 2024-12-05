package provider.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


import javax.swing.*;

import provider.controller.ThreeTriosController;
import provider.model.GameCard;
import provider.model.PlayerColor;

/**
 * Draws the panel of a player's hand, which represents the state of their hand in the Three
 * Trios Game. Players draw cards from the deck to be in their hand.
 */
public class HandPanel extends JPanel {

  private List<CardButton> cardButtonHand;
  private ThreeTriosController controller;
  private PlayerColor currentPlayerColor;
  private boolean cardSelected;
  private PlayerColor color;
  private List<GameCard> hand;
  private boolean turn;


  /**
   * Constructs a HandPanel object that takes in a color and a list of GameCard, the hand.
   *
   * @param color the color to draw the cards in
   * @param hand  the list of GameCard representing the hand of the player
   */
  public HandPanel(PlayerColor currentPlayerColor, PlayerColor color, List<GameCard> hand) {
    // Creating a grid layout of four rows and one column.
    super(new GridLayout(4, 1, 0, 0));
    this.currentPlayerColor = currentPlayerColor;
    this.hand = hand;
    this.color = color;
    this.controller = null;
    this.turn = color == currentPlayerColor;
    // For every card in this hand, add this card as a button to the button list.
    this.cardButtonHand = new ArrayList<>();
    // Add buttons to the hand panel.
    addButtons(color, hand);
  }

  // With the given hand, add buttons for each card.

  /**
   * Adds buttons to the cards in the hand.
   *
   * @param color the color of the player.
   * @param hand  the cards in the players hand currently.
   */
  private void addButtons(PlayerColor color, List<GameCard> hand) {
    for (GameCard card : hand) {
      CardButton cardButton = new CardButton(card, color, true);
      cardButton.setEnabled(false);
      cardButtonHand.add(cardButton);
      this.add(cardButton);
    }
  }


  /**
   * Adds action listeners to each card in this panel.
   */
  public void addActionListeners() {
    // For every button in cardButtonHand, add the handleCardSelection to it.
    for (int curBtnIdx = 0; curBtnIdx < cardButtonHand.size(); curBtnIdx++) {
      GameCard card = hand.get(curBtnIdx);
      CardButton cardButton = cardButtonHand.get(curBtnIdx);

      // Enable the button only if it's the player's turn
      if (this.turn) {
        cardButton.setEnabled(true);
        // Add listener for card selection
        cardButton.addActionListener(e -> handleCardSelection(cardButton, card, hand, color));
      } else {
        // Disable the button when it's not the player's turn
        cardButton.setEnabled(false);
        cardButton.updateSelectionEmptyCard(card, this.currentPlayerColor); // Deselect if necessary
      }
    }
  }

  /**
   * Updates the hand when a card has been played.
   *
   * @param cards list of updated cards in the hand.
   */
  public void updateHand(List<GameCard> cards) {
    for (CardButton button : cardButtonHand) {
      if (button.isSelected()) {
        this.remove(button);
      }
    }
    this.revalidate();
    this.repaint();
  }

  // Handles when a card is selected.
  private void handleCardSelection(CardButton selectedButton, GameCard card,
                                   List<GameCard> hand, PlayerColor color) {
    System.out.println("Card selected: Index = " + hand.indexOf(card) + ", " +
            "Player Color = " + color);

    if (this.turn && selectedButton.isEnabled()) {
      // Update this selected button to be selected.
      this.cardSelected = selectedButton.updateSelectionEmptyCard(card, this.currentPlayerColor);

      // For every card in this hand, if it's not the selected button, deselect it.
      for (CardButton button : cardButtonHand) {
        if (button != selectedButton) {
          button.deselect();
        }
      }

      // Signal to controller that a card has been selected
      if (controller != null && selectedButton.isSelected()) {
        controller.cardSelected(card, hand.indexOf(card));
      }
    }
  }

  /**
   * Adds the listener to the features of this model.
   *
   * @param listener the asynchronous listener for this panel.
   */
  public void addClickListener(ThreeTriosController listener) {
    this.controller = listener;
    // Add action listeners to this hand panel now that the controller has been registered.
    addActionListeners();
  }

  /**
   * Swaps the players turn and updates the action listeners.
   */
  public void swapTurn() {
    this.turn = !this.turn;
    System.out.println("Turn swapped. It's now " + this.turn);
    if (this.currentPlayerColor.toString().equals(PlayerColor.RED.toString())) {
      this.currentPlayerColor = PlayerColor.BLUE;
    } else {
      this.currentPlayerColor = PlayerColor.RED;
    }
    System.out.println("Current player:" + this.currentPlayerColor);
    addActionListeners();
  }

  /**
   * States that this player can listen again since it is now their turn again.
   */
  public void listenAgain() {
    for (CardButton cardButton : cardButtonHand) {
      cardButton.setEnabled(this.turn);
    }
  }
}
