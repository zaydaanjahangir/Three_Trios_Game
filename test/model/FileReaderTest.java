package model;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests the reading and validation of the config files.
 */
public class FileReaderTest {

  private GridFileReader gridReader;
  private CardFileReader cardReader;


  @Before
  public void setUp() {
    gridReader = new GridFileReaderImpl();
    cardReader = new CardFileReaderImpl();
  }

  // private helper to get files
  private File getResourceFile(String path) {
    ClassLoader classLoader = getClass().getClassLoader();
    URL resource = classLoader.getResource(path);
    assertNotNull("Resource file not found: " + path, resource);
    try {
      return new File(resource.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException("Failed to load resource file: " + path, e);
    }
  }

  @Test
  public void testValidGridConfigGrid1() {
    ClassLoader classLoader = getClass().getClassLoader();
    File gridFile = getResourceFile("grid_configs/grid1.txt");
    Grid grid = gridReader.readGrid(gridFile);
    assertNotNull(grid);
    assertEquals(3, grid.getRows());
    assertEquals(3, grid.getCols());
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        assertTrue(grid.isPlayable(row, col));
      }
    }
  }

  @Test
  public void testValidCardConfigCards1() {
    ClassLoader classLoader = getClass().getClassLoader();
    File cardFile = new File(classLoader.getResource("card_configs/cards1.txt").getFile());

    List<Card> cards = cardReader.readCards(cardFile);

    assertNotNull(cards);
    assertEquals(3, cards.size());

    Card firstCard = cards.get(0);
    assertEquals("FireDragon", firstCard.getName());
    assertEquals(5, firstCard.getNorthValue().getIntValue());
    assertEquals(3, firstCard.getSouthValue().getIntValue());
    assertEquals(7, firstCard.getEastValue().getIntValue());
    assertEquals(6, firstCard.getWestValue().getIntValue());

    Card secondCard = cards.get(1);
    assertEquals("WaterSerpent", secondCard.getName());
    assertEquals(4, secondCard.getNorthValue().getIntValue());
    assertEquals(6, secondCard.getSouthValue().getIntValue());
    assertEquals(2, secondCard.getEastValue().getIntValue());
    assertEquals(8, secondCard.getWestValue().getIntValue());

    Card thirdCard = cards.get(2);
    assertEquals("EarthGolem", thirdCard.getName());
    assertEquals(7, thirdCard.getNorthValue().getIntValue());
    assertEquals(5, thirdCard.getSouthValue().getIntValue());
    assertEquals(3, thirdCard.getEastValue().getIntValue());
    assertEquals(4, thirdCard.getWestValue().getIntValue());
  }

  // Validation tests

  // Test that grid contains only 'X' and 'C' characters
  @Test
  public void testGridInvalidCharacters() {
    File gridFile = getResourceFile("grid_configs/grid_invalid_chars.txt");
    assertThrows(IllegalArgumentException.class, () -> {
      gridReader.readGrid(gridFile);
    });
  }

  // Test that grid dimensions are positive integers
  @Test
  public void testGridNegativeDimensions() {
    File gridFile = getResourceFile("grid_configs/grid_negative_dimensions.txt");
    assertThrows(IllegalArgumentException.class, () -> {
      gridReader.readGrid(gridFile);
    });
  }

  // Test that there are too few cards in the card config file
  @Test
  public void testTooFewCards() {
    File cardFile = getResourceFile("card_configs/cards_too_few.txt");
    assertThrows(IllegalArgumentException.class, () -> {
      cardReader.readCards(cardFile);
    });
  }

  // Test that card config contains only valid values (1-9, A)
  @Test
  public void testCardInvalidValues() {
    File cardFile = getResourceFile("card_configs/cards_invalid_values.txt");
    assertThrows(IllegalArgumentException.class, () -> {
      cardReader.readCards(cardFile);
    });
  }

  // Test that card config has invalid formatting
  @Test
  public void testCardInvalidFormat() {
    File cardFile = getResourceFile("card_configs/cards_invalid_format.txt");
    assertThrows(IllegalArgumentException.class, () -> {
      cardReader.readCards(cardFile);
    });
  }

  // Test that card config has too many fields in a line
  @Test
  public void testCardTooManyFields() {
    File cardFile = getResourceFile("card_configs/cards_too_many_fields.txt");
    assertThrows(IllegalArgumentException.class, () -> {
      cardReader.readCards(cardFile);
    });
  }

  // Test that card config has too few fields in a line
  @Test
  public void testCardTooFewFields() {
    File cardFile = getResourceFile("card_configs/cards_too_few_fields.txt");
    assertThrows(IllegalArgumentException.class, () -> {
      cardReader.readCards(cardFile);
    });
  }

  // Test grid row length mismatch with expected column count
  @Test
  public void testGridRowLengthMismatch() {
    File gridFile = getResourceFile("grid_configs/grid_row_length_mismatch.txt");
    assertThrows(IllegalArgumentException.class, () -> {
      gridReader.readGrid(gridFile);
    });
  }

  /**
   * Test that the grid contains an odd number of card cells.
   */
  @Test
  public void testOddNumberOfCardCells() {
    File gridFile = getResourceFile("grid_configs/grid3.txt");
    Grid grid = gridReader.readGrid(gridFile);

    int cardCellCount = 0;
    for (int row = 0; row < grid.getRows(); row++) {
      for (int col = 0; col < grid.getCols(); col++) {
        if (grid.isPlayable(row, col)) {
          cardCellCount++;
        }
      }
    }

    assertTrue("The number of card cells was " + cardCellCount
            , cardCellCount % 2 != 0);
  }

}
