package model;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Interface representing the core mechanics of the Three Trios game.
 * The GameModel interface defines the essential operations needed to play the game,
 * including game setup, card placement, battle execution, and determining the game state.
 */
public interface GameModel extends ReadOnlyGameModel{

  /**
   * Initializes the game with a grid and a collection of cards from configuration files.
   * This method reads the grid and card files, validates their content, and sets up the game
   * by distributing the cards to players and preparing the grid.
   *
   * @param grid takes in the created grid
   * @param cards takes in a list of Card types
   * @param random optional Random seed for testing
   * @throws IllegalArgumentException thrown for any illegal deck or grid configs
   */
  void initializeGame(Grid grid, List<Card> cards, Random random)
          throws IllegalArgumentException;


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
   * Method used to override state for testing certain scenarios
   * @param player Player type who's turn it needs to be for testing
   */
  void setCurrentPlayerForTest(Player player);



}

