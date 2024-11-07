package model;

import java.io.File;
import java.util.Random;

/**
 * Interface representing the core mechanics of the Three Trios game.
 * The GameModel interface defines the essential operations needed to play the game,
 * including game setup, card placement, battle execution, and determining the game state.
 */
public interface GameModel {

  /**
   * Initializes the game with a grid and a collection of cards from configuration files.
   * This method reads the grid and card files, validates their content, and sets up the game
   * by distributing the cards to players and preparing the grid.
   *
   * @param gridConfig the File object pointing to the grid configuration file.
   * @param cardConfig the File object pointing to the card database file.
   * @throws IllegalArgumentException if the configuration files contain invalid data.
   */
  void initializeGame(File gridConfig, File cardConfig, Random random)
          throws IllegalArgumentException;

  /**
   * Checks if the game is over. The game ends when all playable cells on the grid are filled.
   *
   * @return true if the game is over, false otherwise.
   */
  boolean isGameOver();

  /**
   * Determines the winner of the game based on the number of owned cards.
   * This method calculates the final scores for both players and returns a string
   * indicating the winner. If the game ends in a tie, it returns a corresponding message.
   *
   * @return a String indicating the winner or if the game is a tie.
   */
  String getWinner();

  /**
   * Places a card from the current player's hand onto the specified cell on the grid.
   * Any invalid move will be met with a try again message.
   *
   * @param player the Player making the move.
   * @param card   the Card being placed on the grid.
   * @param row    the row index where the card is to be placed.
   * @param col    the column index where the card is to be placed.
   */
  void placeCard(Player player, Card card, int row, int col);

  /**
   * Executes the battle phase for the recently placed card.
   * This involves comparing the newly placed card's attack values against adjacent opposing cards.
   * If any cards are flipped as a result of the battle, a combo step is triggered to continue
   * flipping until no more cards can be flipped.
   *
   * @param row the row index of the recently placed card.
   * @param col the column index of the recently placed card.
   */
  void executeBattlePhase(int row, int col);

  /**
   * Retrieves the current state of the game grid.
   *
   * @return the current Grid object representing the game's grid.
   */
  Grid getGrid();

  /**
   * Gets the current player whose turn it is to make a move.
   *
   * @return the Player object representing the current player.
   */
  Player getCurrentPlayer();

  /**
   * Gets the opposing player to the current player.
   *
   * @return The Player representing the opposing player from the current player
   */
  Player getOpponentPlayer();

}

