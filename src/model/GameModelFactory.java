package model;

import static model.GameMode.PLAYER_VS_AI;
import static model.GameMode.PLAYER_VS_PLAYER;

/**
 * Factory design pattern class to create game models.
 */
public class GameModelFactory {

  /**
   * Method to create game models based on the gameMode given.
   *
   * @param mode GameMode type
   * @return the game model
   */
  public static GameModel createGameModel(GameMode mode) {
    if (mode.equals(PLAYER_VS_PLAYER)) {
      return new ThreeTriosGameModel();
    } else if (mode.equals(PLAYER_VS_AI)) {
      // return ai game model
    } else {
      throw new IllegalArgumentException("Invalid game mode: " + mode);
    }
    throw new IllegalArgumentException("Invalid game mode: " + mode);
  }
}

