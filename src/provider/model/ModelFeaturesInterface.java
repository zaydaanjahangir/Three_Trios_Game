package provider.model;

/**
 * Model Features Interface used to notify the controller or another part of the design that the
 * model has completed a move, changed the player or the game has ended.
 */
public interface ModelFeaturesInterface {
  /**
   * Registers the TTEventListener to this model so that the listener can be notified.
   * @param listener the ThreeTriosEventListener registering itself to the model
   */
  void addGameEventListener(ThreeTriosEventListener listener);

  /**
   * The model should inform the controller that the turn has changed.
   */
  void notifyTurnChanged();

  /**
   * Notifying that the game is over.
   * @param winner the Player that is the winner of the game
   */
  void notifyGameOver(Player winner);


  /**
   * Model notifies its subscribers that the move was invalid.
   * @param message the invalid message
   */
  void notifyInvalidMove(String message);


}