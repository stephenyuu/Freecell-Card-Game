package cs3500.freecell.model;

import cs3500.freecell.model.multimove.MultimoveFreecellModel;

/**
 * The {@code FreecellModelCreator} class is a factory class that holds a
 * public static factory method that returns a specific FreecellModel object.
 */
public class FreecellModelCreator {
  /**
   * The {@code GameType} is an enumeration that represents the two different
   * types of game modes.
   * SINGLEMOVE represents the game mode that supports single-card moves
   * MULTIMOVE represents the game mode that supports multi-card moves (moving builds)
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE
  }

  /**
   * Returns an object of SimpleFreecellModel or MultimoveFreecellModel based on the given GameType.
   *
   * @param type the given game type
   * @return an object of SimpleFreecellModel or MultimoveFreecellModel
   */
  public static FreecellModel create(GameType type) {
    if (type != null && type == GameType.SINGLEMOVE) {
      return new SimpleFreecellModel();
    } else if (type == GameType.MULTIMOVE) {
      return new MultimoveFreecellModel();
    } else {
      throw new IllegalArgumentException("Invalid game type!");
    }
  }
}