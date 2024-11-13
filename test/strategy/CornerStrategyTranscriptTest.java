package strategy;

import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.AIPlayer;
import model.Player;
import model.PlayerImpl;
import model.StandardCard;
import model.Value;
import model.Move;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class CornerStrategyTranscriptTest {

  private CornerMockGameModel mockModel;
  private AIPlayer aiPlayer;
  private CornerStrategy strategy;

  @Before
  public void setUp() {
    strategy = new CornerStrategy();
    mockModel = new CornerMockGameModel();

    aiPlayer = new AIPlayer("Red", strategy);
    aiPlayer.addCardToHand(new StandardCard("TestCard", Value.FIVE, Value.SIX, Value.FOUR, Value.FIVE));

    Player opponent = new PlayerImpl("Blue");
    opponent.addCardToHand(new StandardCard("OppCard", Value.THREE, Value.THREE, Value.THREE, Value.THREE));

    mockModel.setPlayers(aiPlayer, opponent);
  }

  @Test
  public void testCornerStrategyTranscript() throws IOException {
    mockModel.clearInspectedCoordinates();

    Move move = strategy.determineMove(mockModel, aiPlayer);

    assertNotNull(move);

    // Should be upper leftmost so 0,0
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());

    List<String> inspectedCoordinates =  mockModel.getInspectedCoordinates();

    // Write transcript to file
    try (FileWriter writer = new FileWriter("strategy-transcript.txt")) {
      writer.write("CornerStrategy Transcript:\n");
      for (String coordinate : inspectedCoordinates) {
        writer.write("Inspected: " + coordinate + "\n");
      }
    }

    List<String> expectedCorners = Arrays.asList("0,0", "0,2", "2,0", "2,2");
    for (String corner : expectedCorners) {
      assert(inspectedCoordinates.contains(corner));
    }
  }
}

