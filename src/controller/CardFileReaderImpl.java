package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Card;
import model.CardFactory;
import model.Value;

/**
 * Reads and parses card data from a configuration file.
 */
public class CardFileReaderImpl implements CardFileReader {
  @Override
  public List<Card> readCards(File file) throws IllegalArgumentException {
    List<Card> cards = new ArrayList<>();
    try (Scanner scanner = new Scanner(file)) {
      int lineNumber = 0; // for debugging
      while (scanner.hasNextLine()) {
        lineNumber++; // for debugging
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
          continue;
        }
        String[] parts = line.split("\\s+");
        if (parts.length != 5) {
          throw new IllegalArgumentException(
                  "Invalid card format at line " + lineNumber + ": " + line);
        }

        String name = parts[0];
        Value north = parseValue(parts[1]);
        Value south = parseValue(parts[2]);
        Value east = parseValue(parts[3]);
        Value west = parseValue(parts[4]);

        Card card = CardFactory.createCard(name, north, south, east, west);
        cards.add(card);
      }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Card file not found: " + file.getName(), e);
    }
    return cards;
  }

  private Value parseValue(String valueStr) {
    try {
      switch (valueStr) {
        case "1":
          return Value.ONE;
        case "2":
          return Value.TWO;
        case "3":
          return Value.THREE;
        case "4":
          return Value.FOUR;
        case "5":
          return Value.FIVE;
        case "6":
          return Value.SIX;
        case "7":
          return Value.SEVEN;
        case "8":
          return Value.EIGHT;
        case "9":
          return Value.NINE;
        case "A":
          return Value.A;
        default:
          throw new IllegalArgumentException("Invalid card value: " + valueStr);
      }
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid card value: " + valueStr, e);
    }
  }
}
