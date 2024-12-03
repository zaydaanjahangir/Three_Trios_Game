package provider.view;

import provider.model.GameCard;

/**
 * Interface for the button to add additional functionality.
 */
public interface ButtonInterface {

  /**
   * Returns the game card at the buttons location.
   * @return a game card at that button location.
   */
  GameCard returnCard();

  /**
   * Checks if this button is not enabled.
   */
  void notEnabled();

}
