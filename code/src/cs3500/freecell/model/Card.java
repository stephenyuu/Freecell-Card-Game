package cs3500.freecell.model;

import java.util.Objects;

/**
 * The {@code Card} class represents a card.
 */
public class Card {
  private final int value;
  private final String suit;

  /**
   * Constructs a {@code Card} object.
   *
   * @param value the value of the card
   * @param suit  the suit of the card
   * @throws IllegalArgumentException if the suit of card is not "♣", "♦", "♥", or "♠"
   * @throws IllegalArgumentException if the value of card not between 1 and 13
   */
  public Card(int value, String suit) {
    if (1 <= value && value <= 13) {
      this.value = value;
    } else {
      throw new IllegalArgumentException("Invalid value!");
    }
    if (suit.equals("♣") || suit.equals("♦") ||
            suit.equals("♥") || suit.equals("♠")) {
      this.suit = suit;
    } else {
      throw new IllegalArgumentException("Invalid suit!");
    }
  }

  /**
   * Prints this card as value followed by suit.
   *
   * @return print version of this card
   */
  public String toString() {
    switch (value) {
      case 1:
        return "A" + suit;
      case 11:
        return "J" + suit;
      case 12:
        return "Q" + suit;
      case 13:
        return "K" + suit;
      default:
        return String.valueOf(value) + suit;
    }
  }

  /**
   * Checks if this card is valid.
   *
   * @return if this card is a valid card
   */
  public boolean isValidCard() {
    return (1 <= value && value <= 13) &&
            (suit.equals("♣") || suit.equals("♦") || suit.equals("♥") || suit.equals("♠"));
  }

  /**
   * Checks if the suit value is equal to 1.
   *
   * @return if suit value is equal to 1
   */
  public boolean doesValueEqualA() {
    return this.value == 1;
  }

  /**
   * Checks if move to non empty foundation pile is valid
   * Move is valid if suits are equal and the value of this card is exactly 1 more than given card.
   *
   * @param cardTarget the given card on top of foundation pile
   * @return if move is valid
   */
  public boolean isValidMoveToNonEmptyFoundation(Card cardTarget) {
    return this.suit.equals(cardTarget.suit)
            && this.value == cardTarget.value + 1;
  }

  /**
   * Checks if move to nonempty cascade pile is valid.
   * Move is valid if suits are opposite colors and
   * the value of this card is equal to exactly value of given card minus 1
   *
   * @param cardTarget the given card at end of cascade pile
   * @return if move is valid
   */
  public boolean isValidMoveToNonEmptyCascade(Card cardTarget) {
    return this.isOppositeColor(cardTarget) && this.doesValueEqualOneLess(cardTarget);
  }

  /**
   * Checks if value of this card is equal to exactly value of given card minus 1.
   *
   * @param cardTarget the given card at end of cascade pile
   * @return if value of this card is equal to exactly value of given card minus 1
   */
  private boolean doesValueEqualOneLess(Card cardTarget) {
    return this.value == cardTarget.value - 1;
  }

  /**
   * Checks if suit of this card is opposite of suit of given card.
   *
   * @param cardTarget the given card at the end of cascade pile
   * @return if suit of this card is opposite of suit of given card
   */
  private boolean isOppositeColor(Card cardTarget) {
    if (this.suit.equals("♣") || this.suit.equals("♠")) {
      return cardTarget.suit.equals("♦") || cardTarget.suit.equals("♥");
    } else {
      return cardTarget.suit.equals("♣") || cardTarget.suit.equals("♠");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Card)) {
      return false;
    }

    Card other = (Card) o;

    return this.value == other.value
            && this.suit.equals(other.suit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, suit);
  }
}