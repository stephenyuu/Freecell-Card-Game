package cs3500.freecell.model.multimove;

import java.util.ArrayList;
import java.util.List;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;

/**
 * The {@code MultimoveFreecellModel} class represents the implementation of Freecell game
 * that supports multi-card moves (moving builds).
 * A build is valid if it meets two conditions:
 * the cards in the build is arranged in alternating colors and consecutive, descending values
 * and the size of the build is less than or equal to (N + 1) * 2 ^ K, where
 * N is the number of empty open piles and K is the number of empty cascade piles
 * A build can be placed into destination if it forms a valid build with the last card in
 * the destination pile
 * A build can only be moved from cascade pile to cascade pile.
 * This game mode also supports single-card moves, like SimpleFreecellModel
 */
public class MultimoveFreecellModel extends SimpleFreecellModel {

  /**
   * Constructs a {@code MultimoveFreecellModel} object.
   */
  public MultimoveFreecellModel() {
    super();
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber) {

    //check if basic conditions are valid to move,
    //else throw exception
    validConditionsToMove(source, pileNumber, cardIndex);

    //get build being moved
    List<Card> build = getBuildBeingMoved(source, pileNumber, cardIndex);

    //check if conditions of a build are valid to move,
    //else throw exception
    buildConditionsAreValid(destination, build);

    //place build in destination
    placeBuildAt(build, destination, destPileNumber);

    //remove build from source
    removeBuildAt(build, source, pileNumber);
  }

  /**
   * Removes all cards in given build from the given source pile after move is completed.
   *
   * @param build      the given build that is being moved
   * @param source     the given source pile type
   * @param pileNumber the pile index of given source pile
   */
  private void removeBuildAt(List<Card> build, PileType source, int pileNumber) {
    if (source == PileType.OPEN) {
      this.openPile.get(pileNumber).removeAll(build);
    } else if (source == PileType.CASCADE) {
      this.cascadePile.get(pileNumber).removeAll(build);
    }
  }

  /**
   * Checks if the conditions of moving a build are valid.
   * The two conditions are:
   * Builds of multiple cards can only be moved from cascade piles to cascade piles
   * and the build size is compatible with the number of empty open piles and empty cascade piles
   *
   * @param destination the given destination pile type
   * @param build       the given build
   */
  private void buildConditionsAreValid(PileType destination, List<Card> build) {
    if (invalidMultimove(destination, build) || isBuildInvalidSize(build)) {
      throw new IllegalArgumentException("Invalid build!");
    }
  }

  /**
   * Checks if multi-card move is invalid.
   * Multi-card move is invalid if the build consists of multiple cards and the destination pile is
   * not a cascade pile
   *
   * @param build the given build
   * @return if build is a move of multiple cards
   */
  private boolean invalidMultimove(PileType destination, List<Card> build) {
    return destination != PileType.CASCADE && build.size() > 1;
  }

  /**
   * Checks if build is invalid size based on the number of empty open piles and empty
   * cascade piles.
   *
   * @param build the given build
   * @return if build is invalid size
   */
  private boolean isBuildInvalidSize(List<Card> build) {
    int n = getNumberOfFreeOpenPiles();
    int k = getNumberOfEmptyCascadePiles();

    return (build.size() > (n + 1) * Math.pow(2, k));
  }

  /**
   * Get the number of empty cascade piles in the game.
   *
   * @return the number of empty cascade piles
   */
  private int getNumberOfEmptyCascadePiles() {
    int count = 0;

    for (List<Card> cards : this.cascadePile) {
      if (cards.size() == 0) {
        count = count + 1;
      }
    }

    return count;
  }

  /**
   * Get the number of empty open piles.
   *
   * @return the number of empty open piles
   */
  private int getNumberOfFreeOpenPiles() {
    int count = 0;

    for (List<Card> cards : this.openPile) {
      if (cards.size() == 0) {
        count = count + 1;
      }
    }

    return count;
  }

  /**
   * Place the given build at given destination pile.
   *
   * @param build          the given build
   * @param destination    the given destination pile type
   * @param destPileNumber the index of given destination pile
   */
  private void placeBuildAt(List<Card> build, PileType destination, int destPileNumber) {
    //get single card in build
    Card cardInBuildOfSizeOne = build.get(0);

    switch (destination) {
      case FOUNDATION:
        if (this.foundationPile.get(destPileNumber).size() == 0) {
          addToEmptyFoundation(cardInBuildOfSizeOne, destPileNumber);
        } else {
          addToNonEmptyFoundation(cardInBuildOfSizeOne, destPileNumber);
        }
        break;
      case OPEN:
        if (this.openPile.get(destPileNumber).size() == 0) {
          addToEmptyOpen(cardInBuildOfSizeOne, destPileNumber);
        } else {
          throw new IllegalArgumentException("Invalid move: does not follow game rules!");
        }
        break;
      default:
        if (this.cascadePile.get(destPileNumber).size() == 0) {
          addBuildToEmptyCascade(build, destPileNumber);
        } else {
          addBuildToNonEmptyCascade(build, destPileNumber);
        }
        break;
    }
  }

  /**
   * Adds all cards from given build to the end of nonempty cascade pile.
   *
   * @param build          the given build
   * @param destPileNumber the index of given destination pile
   */
  private void addBuildToNonEmptyCascade(List<Card> build, int destPileNumber) {
    //get last index of given cascade pile
    int lastIndexOfPile = this.cascadePile.get(destPileNumber).size() - 1;

    //get card at last index in given cascade pile
    //card that build is being placed onto
    Card cardTarget = this.cascadePile.get(destPileNumber).get(lastIndexOfPile);

    //get first card in build
    //card that is directly placed onto previous last card in given cascade pile
    Card firstCardInBuild = build.get(0);

    if (firstCardInBuild.isValidMoveToNonEmptyCascade(cardTarget)) {
      this.cascadePile.get(destPileNumber).addAll(build);
    } else {
      throw new IllegalArgumentException("Invalid move: does not follow game rules!");
    }
  }

  /**
   * Adds all cards from given build to empty cascade pile.
   *
   * @param build          the given build
   * @param destPileNumber the index of given destination pile
   */
  private void addBuildToEmptyCascade(List<Card> build, int destPileNumber) {
    //add build to cascade pile
    this.cascadePile.get(destPileNumber).addAll(build);
  }

  /**
   * Get the build being moved in a list of Card.
   *
   * @param source     the given source pile type
   * @param pileNumber the index of given source pile
   * @param cardIndex  the given card index
   * @return a list of Card that represents a build
   */
  private List<Card> getBuildBeingMoved(PileType source, int pileNumber, int cardIndex) {
    //represents entire build being moved
    List<Card> entireBuild = new ArrayList<>();

    //if source pile type is open
    //get single card in open pile (a build of size 1)
    if (source == PileType.OPEN && cardIndex == 0) {
      Card cardInOpenPile = this.openPile.get(pileNumber).get(cardIndex);
      entireBuild.add(cardInOpenPile);
    } else {
      //if source pile type is cascade and  rest of cascade pile forms valid build
      //get entire build
      if (source == PileType.CASCADE && isRestOfCascadePileValidBuild(pileNumber, cardIndex)) {
        getBuildInCascadePile(pileNumber, cardIndex, entireBuild);
      }
    }
    return entireBuild;
  }

  /**
   * Checks if cards starting from the card specified by player input to
   * the end of its respective pile forms a valid build.
   * Valid build if the cards are arranged in alternating colors and consecutive, descending values
   *
   * @param pileNumber the index of given source pile
   * @param cardIndex  the given card index
   * @return if the rest of the pile forms a valid build
   */
  private boolean isRestOfCascadePileValidBuild(int pileNumber, int cardIndex) {
    //get cascade pile at given index
    List<Card> oneCascadePile = this.cascadePile.get(pileNumber);

    //loop through cards in cascade pile and check if consecutive cards form valid build
    //stops when currCard is last card in cascade pile
    for (int i = cardIndex; i < oneCascadePile.size() - 1; i = i + 1) {
      //get first card in build
      //specified by given pileNumber, cardIndex
      Card currCard = oneCascadePile.get(cardIndex);

      //get next card in cascade pile
      int idxOfNextCard = cardIndex + 1;
      Card nextCard = oneCascadePile.get(idxOfNextCard);

      //check if next card does not satisfy condition
      //throw exception (breaks code)
      if (!nextCard.isValidMoveToNonEmptyCascade(currCard)) {
        throw new IllegalArgumentException("Invalid build!");
      }
    }

    //return true if loop doesn't throw exception
    return true;
  }

  /**
   * Add the cards in rest of respective pile to form a build that can be moved around.
   *
   * @param pileNumber  the index of the source pile
   * @param cardIndex   the given card index
   * @param entireBuild the list of Card representing the build
   */
  private void getBuildInCascadePile(int pileNumber, int cardIndex, List<Card> entireBuild) {
    //get cascade pile at index pileNumber
    List<Card> oneCascadePile = this.cascadePile.get(pileNumber);

    //loop through cards in cascade pile and add to entireBuild
    for (int i = cardIndex; i < oneCascadePile.size(); i = i + 1) {
      //get card at index index i
      Card card = oneCascadePile.get(i);

      //add to entireBuild
      entireBuild.add(card);
    }
  }
}