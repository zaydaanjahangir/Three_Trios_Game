# Three Trios Game - README

## Overview

This codebase implements *Three Trios* A card game played on a grid where two players compete to
own the grid via placing cards with attack values on each side, which have the ability to flip
their opponents cards. This design allows custom card and grid configurations which are flexible
so long as they fit the requirements, making it easy to customize the game setup.

The code creates a game model that can handle playing human or AI players, so as long as you know
the rules of this simple card game you should be ready to dive into the code.

## Quick Start

Here’s a quick example to get up and running with *Three Trios*. In the Main method,
initialize the game model,
set up the grid and card files, and call `render()` in `TextView` to display the initial game state:

```java
import model.*;
import view.TextView;
import java.io.File;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        File gridFile = new File("resources/grid_configs/grid1.txt");
        File cardFile = new File("resources/card_configs/cards1.txt");

        GameModel model = GameModelFactory.createGameModel(GameMode.PLAYER_VS_PLAYER);
        model.initializeGame(gridFile, cardFile, random);

        TextView view = new TextView(model);
        System.out.println(view.render()); 
    }
}
```

## Key Components

### GameModel
This is the heart of the game logic. `GameModel` handles everything from setting up the game to
tracking players’ turns, checking for game-over conditions, and handling all the battles between
cards. The class `ThreeTriosGameModel` is where all of this happens. It’s responsible for starting
the game, letting players place cards, triggering battles, and deciding who wins.

Some key methods include: `initializeGame`, `placeCard`, `executeBattlePhase`, `isGameOver`,
`getWinner`.

### Grid
The `Grid` class is where the game board lives. It’s full of cells, which might be playable spaces
or "holes" that can’t be used. `StandardGrid` manages the setup, making sure cells are available
and keeping track of cards as they’re placed. It doesn’t do much on its own but gets updated
constantly as the model continues to run.

Some key methods include: `isPlayable`, `getCell`, `placeCard`, `isFull`.

### Card and CardFactory
Cards in *Three Trios* are like mini compasses with attack values on the North, South, East, and
West side. When they’re placed on the board, their values are compared to neighboring cards to see
if they flip control. The `Card` interface and `StandardCard` class define each card's attributes
and logic, while `CardFactory` helps create the cards whenever needed.

Some key methods include: `compareAgainst`, `getName`, `getValue` methods (for each direction).

### Player
The `Player` component manages each player’s hand of cards and tracks their
color (Red or Blue). It helps manage turn-taking and lets players interact with the grid via
`GameModel`.

Some key methods include: `addCardToHand`, `removeCardFromHand`, `getHand`.

### File Readers
To load configurations, we use `CardFileReader` and `GridFileReader`, which parse and validate
configuration files for the game. These components keep the game setup flexible, letting you load
different cards or grid structures whenever you want.

Some key methods include: `readCards`, `readGrid`.

### TextView
The `TextView` class is the text-based "view" that displays the current game state on the console.
It relies on data from the model to show the board and players’ hands, giving a visualization for
testing and playing the game.

Some key methods include: `render`, which calls helper methods to format the grid and hands.

## Source Organization

- **`model/`**: Contains the model-related classes and interfaces like `GameModel`, `Grid`, `Player`
  , and configuration file readers (`CardFileReader`, `GridFileReader`). Everything here handles the
  game’s core data and rules.

- **`view/`**: Holds the `TextView` class, which is used to show the game state.

- **`resources/`**: Contains configuration files for grids (`grid_configs/`) and cards
  (`card_configs/`). These are easy to modify if you want to change how the game is set up.

- **`tests/`**: Includes test files like `FileReaderTest`, `ThreeTriosGameModelTest`, and
  `TextViewTest` to make sure everything’s working as expected. The tests cover the main components
  and validate key logic.


## Changes for Part 2

- **Moved File Readers to Controller:**
  - Migrated `CardFileReader`, `CardFileReaderImpl`, `GridFileReader`, and `GridFileReaderImpl` to 
    controller to better fit with the MVC definition
  - This ensures the jobs of the model and controller are separate and modular.

- **Added Missing Functionality:**
  - Implemented methods to get grid size, cell contents, player's hand, card owner at a cell, check 
  move legality, calculate potential flips, and get player's score.
  - These methods are necessary for the view to display game information and for strategies to make
  decisions.

- **Implemented `compareAgainst` Method in `StandardCard`:**
  - Completed the comparison logic to enable the battle phase and strategy calculations.

## Changes for Part 3

- **Controller Redesign:**
  - Introduced the `Features` and `PlayerAction` interfaces to modularize player interactions.
  - Completely redesigned the `ThreeTriosController` to mediate between the model, view, and players
  , allowing for human and AI interactions.
  - The controller now listens to changes in the model and view and coordinates turn-based play.

- **Model Enhancements:**
  - Updated the model to include listeners (`ModelStatusListener`) that notify the controller of 
  state changes (e.g., turn switching or game over).
  - Added the ability to manage turns dynamically and notify players when it's their turn.

- **View Updates:**
  - Adapted the `ThreeTriosView` to handle two distinct views (one for each player) that reflect the
  same game state.
  - Ensured only the current player's view is interactive while the other view waits for its turn.
  - Integrated visual feedback (e.g., highlighting selected cards) for better player experience.

- **Command-Line Configuration:**
  - Updated the `Main` method to support command-line arguments for specifying player types:
    - `human`, `flipmaxStrategy`, or `cornerStrategy`.
  - The game can now be configured with any combination of human and AI players, supporting diverse 
  gameplay scenarios.

## Running the Game with Command-Line Arguments

To run the game, pass two arguments specifying the player types.

### Usage:

```bash
java -jar ThreeTriosGame.jar <player1Type> <player2Type>
```

### Player Types:

- `human`: A human-controlled player.
- `flipmaxStrategy`: An AI player using the `FlipMaxStrategy`.
- `cornerStrategy`: An AI player using the `CornerStrategy`.

### Examples:

- **Human vs. Human:**

  ```bash
  java -jar ThreeTriosGame.jar human human
  ```
- **Human vs. AI (FlipMaxStrategy):**

  ```bash
  java -jar ThreeTriosGame.jar human flipmaxstrategy
  ```

- **AI (CornerStrategy) vs. AI (FlipMaxStrategy):**

  ```bash
  java -jar ThreeTriosGame.jar cornerstrategy flipmaxstrategy
  ```