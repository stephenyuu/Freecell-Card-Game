package cs3500.freecell.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@code SimpleFreecellModel} class represents implementation of Freecell game.
 */
public class SimpleFreecellModel implements FreecellModel<Card> {
  protected List<List<Card>> cascadePile;
  protected List<List<Card>> openPile;
  protected List<List<Card>> foundationPile;
  protected boolean hasGameStarted;

  //changed access modifier for fields from private to protected to allow access in child classes

  /**
   * Constructs a {@code SimpleFreecellModel} object.
   */
  public SimpleFreecellModel() {
    this.cascadePile = null;
    this.openPile = null;
    this.foundationPile = null;
    this.hasGameStarted = false;
  }

  @Override
  public List<Card> getDeck() {
    List<Card> validDeck = new ArrayList<Card>(52);
    for (int i = 1; i < 14; i++) {
      validDeck.add(new Card(i, "♣"));
      validDeck.add(new Card(i, "♦"));
      validDeck.add(new Card(i, "♥"));
      validDeck.add(new Card(i, "♠"));
    }
    return validDeck;
  }

  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle) {

    isValidParametersToStartGame(deck, numCascadePiles, numOpenPiles);

    initializePiles(numCascadePiles, numOpenPiles);

    //if shuffle is true, shuffle deck before dealing deck
    //if shuffle is false, deal deck as is
    if (shuffle) {
      Collections.shuffle(deck);
    }

    //create a copy of the deck
    List<Card> copyDeck = new ArrayList<>();
    copyDeck.addAll(deck);

    //deals the deck in round-robin fashion
    for (int rowNum = 0; rowNum < Math.ceil(deck.size() / (double) numCascadePiles); rowNum++) {
      for (int colNum = 0; colNum < numCascadePiles; colNum++) {
        if (copyDeck.size() == 0) {
          break;
        } else {
          this.cascadePile.get(colNum).add(copyDeck.get(0));
          copyDeck.remove(0);
        }
      }
    }

    //mark hasGameStarted as true
    this.hasGameStarted = true;
  }

  /**
   * Initializes the cascade, open, and foundation piles.
   *
   * @param numCascadePiles the given number of cascade piles
   * @param numOpenPiles    the given number of open piles
   */
  private void initializePiles(int numCascadePiles, int numOpenPiles) {

    //initializes cascade pile
    this.cascadePile = new ArrayList<>(numCascadePiles);
    for (int i = 0; i < numCascadePiles; i++) {
      this.cascadePile.add(new ArrayList<>());
    }

    //initializes open pile
    this.openPile = new ArrayList<>(numOpenPiles);
    for (int i = 0; i < numOpenPiles; i++) {
      this.openPile.add(new ArrayList<>());
    }

    //initializes foundation piles
    this.foundationPile = new ArrayList<>(4);
    for (int i = 0; i < 4; i++) {
      this.foundationPile.add(new ArrayList<>());
    }
  }

  /**
   * Checks if the given parameters are valid to start the game.
   * The deck has to satisfy several conditions (checked in helper method)
   * The number of cascade piles must be at least 4
   * The number of open piles must be at least 1
   *
   * @param deck            the given deck
   * @param numCascadePiles the given number of cascade piles
   * @param numOpenPiles    the given number of open piles
   */
  private void isValidParametersToStartGame(List<Card> deck, int numCascadePiles,
                                            int numOpenPiles) {

    //checks if deck is valid
    isValidDeck(deck);

    //checks if number of cascade piles is valid
    if (numCascadePiles < 4) {
      throw new IllegalArgumentException("Invalid number of cascade piles!");
    }

    //checks if number of open piles is valid
    if (numOpenPiles < 1) {
      throw new IllegalArgumentException("Invalid number of open piles!");
    }
  }

  /**
   * Checks if given deck is valid.
   * The deck is valid if it has exactly 52 cards and contains no duplicates and
   * contains no invalid cards
   *
   * @param deck the given deck
   */
  private void isValidDeck(List<Card> deck) {

    //throws exception if deck does not contain 52 cards
    if (deck.size() != 52) {
      throw new IllegalArgumentException("Invalid deck: size is incorrect!");
    }

    //throws exception if deck contains duplicate cards
    for (int i = 0; i < deck.size(); i++) {
      for (int j = i + 1; j < deck.size(); j++) {
        if (deck.get(i).equals(deck.get(j))) {
          throw new IllegalArgumentException("Invalid deck: contains duplicate cards!");
        }
      }
    }

    //throws exception if deck contains invalid cards
    for (Card c : deck) {
      if (!c.isValidCard()) {
        throw new IllegalArgumentException("Invalid deck: contains invalid card!");
      }
    }
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber) {

    validConditionsToMove(source, pileNumber, cardIndex);

    Card card = getCardBeingMoved(source, pileNumber, cardIndex);

    //place the card in destination
    placeCardAt(card, destination, destPileNumber);

    //remove the card from source
    removeCardAt(card, source, pileNumber);
  }

  /**
   * Removes the given card at given source pile.
   *
   * @param card       the card that is being moved
   * @param source     the given source pile
   * @param pileNumber the given index number of source pile
   */
  private void removeCardAt(Card card, PileType source, int pileNumber) {
    if (source == PileType.OPEN) {
      this.openPile.get(pileNumber).remove(card);
    } else if (source == PileType.CASCADE) {
      this.cascadePile.get(pileNumber).remove(card);
    }
  }

  /**
   * Places the given card at given destination pile.
   *
   * @param card           the card that is being moved
   * @param destination    the given destination pile
   * @param destPileNumber the given index number of destination pile
   */
  private void placeCardAt(Card card, PileType destination, int destPileNumber) {
    switch (destination) {
      case FOUNDATION:
        if (this.foundationPile.get(destPileNumber).size() == 0) {
          addToEmptyFoundation(card, destPileNumber);
        } else {
          addToNonEmptyFoundation(card, destPileNumber);
        }
        break;
      case OPEN:
        if (this.openPile.get(destPileNumber).size() == 0) {
          addToEmptyOpen(card, destPileNumber);
        } else {
          throw new IllegalArgumentException("Invalid move: does not follow game rules!");
        }
        break;
      default:
        if (this.cascadePile.get(destPileNumber).size() == 0) {
          addToEmptyCascade(card, destPileNumber);
        } else {
          addToNonEmptyCascade(card, destPileNumber);
        }
        break;
    }
  }

  /**
   * Adds the given card to end of given nonempty cascade pile based on game rules.
   *
   * @param card           the card that is being moved
   * @param destPileNumber the index number of given destination cascade pile
   */
  private void addToNonEmptyCascade(Card card, int destPileNumber) {

    int lastIndexOfPile = this.cascadePile.get(destPileNumber).size() - 1;
    Card cardTarget = this.cascadePile.get(destPileNumber).get(lastIndexOfPile);

    if (card.isValidMoveToNonEmptyCascade(cardTarget)) {
      this.cascadePile.get(destPileNumber).add(card);
    } else {
      throw new IllegalArgumentException("Invalid move: does not follow game rules!");
    }
  }

  /**
   * Adds the given card to the end of given empty cascade pile based on game rules.
   *
   * @param card           the card that is being moved
   * @param destPileNumber the index number of given destination cascade pile
   */
  private void addToEmptyCascade(Card card, int destPileNumber) {
    this.cascadePile.get(destPileNumber).add(card);
  }

  /**
   * Adds the given card to the given open cascade pile based on game rules.
   *
   * @param card           the card that is being moved
   * @param destPileNumber the index number of given destination open pile
   */
  protected void addToEmptyOpen(Card card, int destPileNumber) {
    //changed access modifier for validConditionsToMove from private to protected
    // to allow access in child classes

    this.openPile.get(destPileNumber).add(card);
  }

  /**
   * Adds the given card to the given nonempty foundation pile based on game rules.
   *
   * @param card           the card that is being moved
   * @param destPileNumber the index number of given destination foundation pile
   */
  protected void addToNonEmptyFoundation(Card card, int destPileNumber) {
    //changed access modifier for validConditionsToMove from private to protected
    // to allow access in child classes

    int lastIndexOfPile = this.foundationPile.get(destPileNumber).size() - 1;
    Card cardTarget = this.foundationPile.get(destPileNumber).get(lastIndexOfPile);

    if (card.isValidMoveToNonEmptyFoundation(cardTarget)) {
      this.foundationPile.get(destPileNumber).add(card);
    } else {
      throw new IllegalArgumentException("Invalid move: does not follow game rules!");
    }
  }

  /**
   * Adds the given card to the given empty foundation pile based on game rules.
   *
   * @param card           the card that is being moved
   * @param destPileNumber the index number of given destination foundation pile
   */
  protected void addToEmptyFoundation(Card card, int destPileNumber) {
    //changed access modifier for validConditionsToMove from private to protected
    // to allow access in child classes

    if (card.doesValueEqualA()) {
      this.foundationPile.get(destPileNumber).add(card);
    } else {
      throw new IllegalArgumentException("Invalid move: does not follow game rules!");
    }
  }


  /**
   * Gets the card that is being moved.
   *
   * @param source     the given source pile
   * @param pileNumber the given index number of the source pile
   * @param cardIndex  the given index number of the card in the source pile
   * @return the card that is being moved
   */
  private Card getCardBeingMoved(PileType source, int pileNumber, int cardIndex) {
    if (source == PileType.CASCADE && isCardInRange(pileNumber, cardIndex)
            && isLastCardInPile(pileNumber, cardIndex)) {
      return this.cascadePile.get(pileNumber).get(cardIndex);
    } else {
      if (source == PileType.OPEN && cardIndex == 0) {
        return this.openPile.get(pileNumber).get(cardIndex);
      } else {
        throw new IllegalArgumentException("Invalid pile/card index!");
      }
    }
  }

  /**
   * Checks if card index is the last in the cascade pile it belongs to.
   *
   * @param pileNumber the given index number of the source cascade pile
   * @param cardIndex  the given index number of the card in the source pile
   * @return
   */
  private boolean isLastCardInPile(int pileNumber, int cardIndex) {
    return cardIndex == this.cascadePile.get(pileNumber).size() - 1;
  }

  /**
   * Checks if card index is within the range of the cascade pile it belongs to.
   *
   * @param pileNumber the given index number of the source cascade pile
   * @param cardIndex  the given index number of the card in the source pile
   * @return if the card index is within the range of the pile it belongs to
   */
  private boolean isCardInRange(int pileNumber, int cardIndex) {
    return cardIndex >= 0 && cardIndex < this.cascadePile.get(pileNumber).size();
  }

  /**
   * Checks if the given conditions are valid to move a card.
   * The game must be started and the source pile cannot be of type foundation
   *
   * @param source     the given source pile
   * @param pileNumber the given index number of the source pile
   * @param cardIndex  the given index number of the card in the source pile
   */
  protected void validConditionsToMove(PileType source, int pileNumber, int cardIndex) {
    //changed access modifier for validConditionsToMove from private to protected
    // to allow access in child classes

    //throws exception if game has not started
    if (!hasGameStarted) {
      throw new IllegalStateException("Invalid move: game has not started!");
    }

    //throws exception if source is foundation pile
    if (source == PileType.FOUNDATION) {
      throw new IllegalArgumentException("Invalid move: source cannot be foundation pile!");
    }
  }

  @Override
  public boolean isGameOver() {
    //checks if each foundation pile has 13 cards
    //if true, means all cards have been moved to foundation piles and game is over
    return this.foundationPile.get(0).size() == 13
            && this.foundationPile.get(1).size() == 13
            && this.foundationPile.get(2).size() == 13
            && this.foundationPile.get(3).size() == 13;
  }

  @Override
  public int getNumCardsInFoundationPile(int index) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started!");
    } else if (isInvalidPileIndex(PileType.FOUNDATION, index)) {
      throw new IllegalArgumentException("Index is invalid!");
    } else {
      return this.foundationPile.get(index).size();
    }
  }

  @Override
  public int getNumCascadePiles() {
    if (hasGameStarted) {
      return this.cascadePile.size();
    } else {
      return -1;
    }
  }

  @Override
  public int getNumCardsInCascadePile(int index) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started!");
    } else if (isInvalidPileIndex(PileType.CASCADE, index)) {
      throw new IllegalArgumentException("Index is invalid!");
    } else {
      return this.cascadePile.get(index).size();
    }
  }

  @Override
  public int getNumCardsInOpenPile(int index) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started!");
    } else if (isInvalidPileIndex(PileType.OPEN, index)) {
      throw new IllegalArgumentException("Index is invalid!");
    } else {
      return this.openPile.get(index).size();
    }
  }

  @Override
  public int getNumOpenPiles() {
    if (hasGameStarted) {
      return this.openPile.size();
    } else {
      return -1;
    }
  }

  @Override
  public Card getFoundationCardAt(int pileIndex, int cardIndex) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started!");
    } else if (isInvalidPileIndex(PileType.FOUNDATION, pileIndex)) {
      throw new IllegalArgumentException("Index is invalid!");
    } else if (isInvalidCardIndex(PileType.FOUNDATION, pileIndex, cardIndex)) {
      throw new IllegalArgumentException("Index is invalid!");
    } else {
      return this.foundationPile.get(pileIndex).get(cardIndex);
    }
  }

  @Override
  public Card getCascadeCardAt(int pileIndex, int cardIndex) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started!");
    } else if (isInvalidPileIndex(PileType.CASCADE, pileIndex)) {
      throw new IllegalArgumentException("Index is invalid!");
    } else if (isInvalidCardIndex(PileType.CASCADE, pileIndex, cardIndex)) {
      throw new IllegalArgumentException("Index is invalid!");
    } else {
      return this.cascadePile.get(pileIndex).get(cardIndex);
    }
  }

  @Override
  public Card getOpenCardAt(int pileIndex) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started!");
    } else if (isInvalidPileIndex(PileType.OPEN, pileIndex)) {
      throw new IllegalArgumentException("Index is invalid!");
    } else if (isInvalidCardIndex(PileType.OPEN, pileIndex, 0)) {
      return null;
    } else {
      return this.openPile.get(pileIndex).get(0);
    }
  }

  /**
   * Checks if given card index is not within the range of the pile it belongs to.
   *
   * @param pileType  the given pile type
   * @param cardIndex the given card index
   * @return if given card index is not within the range of the pile it belongs to
   */
  private boolean isInvalidCardIndex(PileType pileType, int pileIndex, int cardIndex) {
    if (pileType == PileType.FOUNDATION) {
      return cardIndex < 0 || cardIndex > this.foundationPile.get(pileIndex).size() - 1;
    } else if (pileType == PileType.CASCADE) {
      return cardIndex < 0 || cardIndex > this.cascadePile.get(pileIndex).size() - 1;
    } else {
      return this.openPile.get(pileIndex).size() == 0;
    }
  }

  /**
   * Checks if given pile index number is not within the range of the piles of its type.
   *
   * @param pileType  the given pile type
   * @param pileIndex the given index number of the pile
   * @return if given pile index is not within the range of the piles of its type
   */
  private boolean isInvalidPileIndex(PileType pileType, int pileIndex) {
    if (pileType == PileType.FOUNDATION) {
      return pileIndex < 0 || pileIndex > 3;
    } else if (pileType == PileType.CASCADE) {
      return pileIndex < 0 || pileIndex > this.cascadePile.size() - 1;
    } else {
      return pileIndex < 0 || pileIndex > this.openPile.size() - 1;
    }
  }
}