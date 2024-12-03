package provider.model;

/**
 * Represents the exception for when there is a tie in the determining the winner.
 */
public class TieBreakerException extends Exception {
  public TieBreakerException(String errorMessage) {
    super(errorMessage);
  }
}