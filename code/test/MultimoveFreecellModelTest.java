import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.multimove.MultimoveFreecellModel;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test cases for the freecell model that supports multi-card moves.
 */
public class MultimoveFreecellModelTest {

  private FreecellModel freecellMultimoveGame;
  private List<Card> deck1;
  private FreecellModel freecellMultimoveGame2;

  @Before
  public void setUp() {
    this.freecellMultimoveGame = new MultimoveFreecellModel();
    this.deck1 = new ArrayList<>(52);

    for (int i = 13; i > 0; i = i - 1) {
      deck1.add(new Card(i, "♣"));
      deck1.add(new Card(i, "♦"));
      deck1.add(new Card(i, "♥"));
      deck1.add(new Card(i, "♠"));
    }

    //start game
    this.freecellMultimoveGame.startGame(deck1, 4, 1, false);

    this.freecellMultimoveGame2 = new MultimoveFreecellModel();
  }

  @Test
  public void testConstructorMultimoveFreecell() {
    //check if cascade piles and open piles are not null ->
    //should not be null if constructor is working
    assertEquals(4, this.freecellMultimoveGame.getNumCascadePiles());
    assertEquals(1, this.freecellMultimoveGame.getNumOpenPiles());
  }

  @Test
  public void testMoveSingleCardFromCascadeToEmptyOpenValid() {
    //move card from cascade to empty open
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);

    //check card in open after move from cascade to open
    assertEquals(new Card(1, "♣"), this.freecellMultimoveGame.getOpenCardAt(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveSingleCardFromCascadeToNonemptyOpenInvalid() {
    //move card from cascade to empty open
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);

    //move card from cascade to nonempty open
    //invalid because open already has card
    this.freecellMultimoveGame.move(PileType.CASCADE, 1, 12,
            PileType.OPEN, 0);
  }

  @Test
  public void testMoveSingleCardFromOpenToNonemptyCascadeValid() {
    //move card from cascade to empty open
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);

    //move card from cascade to nonempty cascade
    this.freecellMultimoveGame.move(PileType.CASCADE, 2, 12,
            PileType.CASCADE, 0);

    //move card from open to nonempty cascade
    this.freecellMultimoveGame.move(PileType.OPEN, 0, 0,
            PileType.CASCADE, 2);

    //check card in cascade after move from open back to cascade
    assertEquals(new Card(1, "♣"),
            this.freecellMultimoveGame.getCascadeCardAt(2, 12));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveSingleCardFromOpenToNonemptyCascadeInvalid() {
    //move card from cascade to empty open
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);

    //move card from open to nonempty cascade
    //invalid because doesn't follow game rules
    this.freecellMultimoveGame.move(PileType.OPEN, 0, 0,
            PileType.CASCADE, 2);
  }

  @Test
  public void testMoveSingleCardFromOpenToEmptyCascadeValid() {
    //move card from cascade to empty open
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);

    //move all cards from one cascade pile to one foundation pile
    //leaves us with one empty cascade pile
    for (int i = 12; i >= 0; i = i - 1) {
      this.freecellMultimoveGame.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 3);
    }

    //move card from open to empty cascade
    this.freecellMultimoveGame.move(PileType.OPEN, 0, 0,
            PileType.CASCADE, 3);

    //check card in cascade after move from open back to cascade
    assertEquals(new Card(1, "♣"),
            this.freecellMultimoveGame.getCascadeCardAt(3, 0));
  }

  @Test
  public void testMoveSingleCardFromCascadeToEmptyFoundationValid() {
    //move card from cascade to empty foundation
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.FOUNDATION, 0);

    //check card in foundation after move from cascade to foundation
    assertEquals(new Card(1, "♣"),
            this.freecellMultimoveGame.getFoundationCardAt(0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveSingleCardFromCascadeToEmptyFoundationInvalid() {
    //move card from cascade to empty foundation
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.FOUNDATION, 0);

    //move card from cascade to empty foundation
    //invalid because doesn't follow game rules
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 11,
            PileType.FOUNDATION, 1);
  }

  @Test
  public void testMoveSingleCardFromCascadeToNonemptyFoundationValid() {
    //move card from cascade to empty foundation
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.FOUNDATION, 0);

    //move card from cascade to nonempty foundation
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 11,
            PileType.FOUNDATION, 0);

    //check second card in foundation after move from cascade to foundation
    assertEquals(new Card(2, "♣"),
            this.freecellMultimoveGame.getFoundationCardAt(0, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveSingleCardFromCascadeToNonemptyFoundationInvalid() {
    //move card from cascade to empty foundation
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.FOUNDATION, 0);

    //move card from cascade to nonempty foundation
    //invalid because doesn't follow game rules
    this.freecellMultimoveGame.move(PileType.CASCADE, 1, 12,
            PileType.FOUNDATION, 0);
  }

  @Test
  public void testMoveSingleCardFromCascadeToNonemptyCascadeValid() {
    //move card from cascade to empty open
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);

    //move card from cascade to nonempty cascade
    this.freecellMultimoveGame.move(PileType.CASCADE, 2, 12,
            PileType.CASCADE, 0);

    //check card in cascade after move from cascade to cascade
    assertEquals(new Card(1, "♥"),
            this.freecellMultimoveGame.getCascadeCardAt(0, 12));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveSingleCardFromCascadeToNonemptyCascadeInvalid() {
    //move card from cascade to empty open
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);

    //move card from cascade to nonempty cascade
    //invalid because doesn't follow game rules
    this.freecellMultimoveGame.move(PileType.CASCADE, 3, 12,
            PileType.CASCADE, 0);
  }

  @Test
  public void testMoveSingleCardFromCascadeToEmptyCascadeValid() {
    //move all cards from one cascade pile to one foundation pile
    //leaves us with one empty cascade pile
    for (int i = 12; i >= 0; i = i - 1) {
      this.freecellMultimoveGame.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 3);
    }

    //move card from cascade to empty cascade
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.CASCADE, 3);

    //check card in cascade after move from cascade to cascade
    assertEquals(new Card(1, "♣"),
            this.freecellMultimoveGame.getCascadeCardAt(3, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveBuildFromCascadeToEmptyOpenInvalid() {
    //start game
    this.freecellMultimoveGame2.startGame(deck1, 8, 4, false);

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 6,
            PileType.FOUNDATION, 0);

    //form valid build of two cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 5,
            PileType.CASCADE, 0);

    //form valid build of three cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 5,
            PileType.CASCADE, 5);

    //move build from cascade to open
    //invalid because multimove only for from cascade to cascade
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 4,
            PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveBuildFromCascadeToNonemptyOpenInvalid() {
    //start game
    this.freecellMultimoveGame2.startGame(deck1, 8, 4, false);

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 6,
            PileType.FOUNDATION, 0);

    //form valid build of two cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 5,
            PileType.CASCADE, 0);

    //form valid build of three cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 5,
            PileType.CASCADE, 5);

    //move card from cascade to open
    this.freecellMultimoveGame2.move(PileType.CASCADE, 7, 5,
            PileType.OPEN, 0);

    //move build from cascade to open
    //invalid because multimove only for from cascade to cascade
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 4,
            PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveBuildFromCascadeToEmptyFoundationInvalid() {
    //start game
    this.freecellMultimoveGame2.startGame(deck1, 8, 4, false);

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 6,
            PileType.FOUNDATION, 0);

    //form valid build of two cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 5,
            PileType.CASCADE, 0);

    //form valid build of three cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 5,
            PileType.CASCADE, 5);

    //move build from cascade to foundation
    //invalid because multimove only for from cascade to cascade
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 4,
            PileType.FOUNDATION, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveBuildFromCascadeToNonemptyFoundationInvalid() {
    //start game
    this.freecellMultimoveGame2.startGame(deck1, 8, 4, false);

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 6,
            PileType.FOUNDATION, 0);

    //form valid build of two cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 5,
            PileType.CASCADE, 0);

    //form valid build of three cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 5,
            PileType.CASCADE, 5);

    //move build from cascade to foundation
    //invalid because multimove only for from cascade to cascade
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 4,
            PileType.FOUNDATION, 0);
  }

  @Test
  public void testMoveBuildSizeTwoFromCascadeToCascadeOneOpenPileValid() {
    //start game
    this.freecellMultimoveGame2.startGame(deck1, 4, 1, false);

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 3, 12,
            PileType.FOUNDATION, 3);

    //form valid build of two cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 2, 12,
            PileType.CASCADE, 3);

    //move necessary cards from cascade to foundation to allow move build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 1, 12,
            PileType.FOUNDATION, 1);
    this.freecellMultimoveGame2.move(PileType.CASCADE, 1, 11,
            PileType.FOUNDATION, 1);

    //move build from cascade to cascade
    this.freecellMultimoveGame2.move(PileType.CASCADE, 3, 11,
            PileType.CASCADE, 1);

    //check cards in cascade after moving build from cascade to cascade
    assertEquals(new Card(2, "♠"),
            this.freecellMultimoveGame2.getCascadeCardAt(1, 11));
    assertEquals(new Card(1, "♥"),
            this.freecellMultimoveGame2.getCascadeCardAt(1, 12));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveBuildSizeTwoFromCascadeToCascadeZeroOpenPileInvalid() {
    //start game
    this.freecellMultimoveGame2.startGame(deck1, 4, 1, false);

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 3, 12,
            PileType.FOUNDATION, 3);

    //form valid build of two cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 2, 12,
            PileType.CASCADE, 3);

    //move necessary cards from cascade to foundation to allow move build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 1, 12,
            PileType.FOUNDATION, 1);
    this.freecellMultimoveGame2.move(PileType.CASCADE, 1, 11,
            PileType.FOUNDATION, 1);

    //move card from cascade to open to make move build invalid
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);

    //move build to cascade
    //invalid because build size of 2 is greater than ((0+1) * 2 ^ 0)
    this.freecellMultimoveGame2.move(PileType.CASCADE, 3, 11,
            PileType.CASCADE, 1);
  }

  @Test
  public void testMoveBuildSizeTwoFromCascadeToCascadeZeroOpenPileOneEmptyCascadePileValid() {
    //move all cards from one cascade pile to one foundation pile
    //leaves us with one empty cascade pile
    for (int i = 12; i >= 0; i = i - 1) {
      this.freecellMultimoveGame.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 3);
    }

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame.move(PileType.CASCADE, 2, 12,
            PileType.FOUNDATION, 2);

    //form valid build of two cards
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.CASCADE, 2);

    //move cards from cascade to open to fill open
    this.freecellMultimoveGame.move(PileType.CASCADE, 1, 12,
            PileType.OPEN, 0);

    //move build from cascade to cascade
    this.freecellMultimoveGame.move(PileType.CASCADE, 2, 11,
            PileType.CASCADE, 3);

    //check cards in cascade after moving build from cascade to cascade
    assertEquals(new Card(2, "♥"),
            this.freecellMultimoveGame.getCascadeCardAt(3, 0));
    assertEquals(new Card(1, "♣"),
            this.freecellMultimoveGame.getCascadeCardAt(3, 1));
  }

  @Test
  public void testMoveBuildSizeThreeFromCascadeToCascadeTwoOpenPileValid() {
    //start game
    this.freecellMultimoveGame2.startGame(deck1, 8, 2, false);

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 6,
            PileType.FOUNDATION, 0);

    //form valid build of two cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 5,
            PileType.CASCADE, 0);

    //form valid build of three cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 5,
            PileType.CASCADE, 5);

    //move build from cascade to cascade
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 4,
            PileType.CASCADE, 0);

    //check cards in cascade after moving build from cascade to cascade
    assertEquals(new Card(4, "♦"),
            this.freecellMultimoveGame2.getCascadeCardAt(0, 5));
    assertEquals(new Card(3, "♣"),
            this.freecellMultimoveGame2.getCascadeCardAt(0, 6));
    assertEquals(new Card(2, "♦"),
            this.freecellMultimoveGame2.getCascadeCardAt(0, 7));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveBuildSizeThreeFromCascadeToCascadeOneOpenPileInvalid() {
    //start game
    this.freecellMultimoveGame2.startGame(deck1, 8, 1, false);

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 6,
            PileType.FOUNDATION, 0);

    //form valid build of two cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 5,
            PileType.CASCADE, 0);

    //form valid build of three cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 5,
            PileType.CASCADE, 5);

    //move card from cascade to open to make move build invalid
    this.freecellMultimoveGame2.move(PileType.CASCADE, 7, 5,
            PileType.OPEN, 0);

    //move build from cascade to cascade
    //invalid because build of size 3 is greater than ((1 + 1) * 2 ^ 0)
    this.freecellMultimoveGame2.move(PileType.CASCADE, 5, 4,
            PileType.CASCADE, 0);
  }

  @Test
  public void testMoveBuildSizeThreeFromCascadeToCascadeOneOpenPileOneEmptyCascadePileValid() {
    //start game
    this.freecellMultimoveGame2.startGame(deck1, 4, 2, false);

    //move all cards from one cascade pile to one foundation pile
    //leaves us with one empty cascade pile
    for (int i = 12; i >= 0; i = i - 1) {
      this.freecellMultimoveGame2.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 3);
    }

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 2, 12,
            PileType.FOUNDATION, 2);

    //form valid build of two cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 12,
            PileType.CASCADE, 2);

    //move card from cascade to open to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 11,
            PileType.OPEN, 0);

    //form valid build of three cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 2, 11,
            PileType.CASCADE, 0);

    //move build from cascade to cascade
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 10,
            PileType.CASCADE, 3);

    //check cards in cascade after moving build from cascade to cascade
    assertEquals(new Card(3, "♣"),
            this.freecellMultimoveGame2.getCascadeCardAt(3, 0));
    assertEquals(new Card(2, "♥"),
            this.freecellMultimoveGame2.getCascadeCardAt(3, 1));
    assertEquals(new Card(1, "♣"),
            this.freecellMultimoveGame2.getCascadeCardAt(3, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveBuildSizeThreeFromCascadeToCascadeZeroOpenPileOneEmptyCascadePileInvalid() {
    //move all cards from one cascade pile to one foundation pile
    //leaves us with one empty cascade pile
    for (int i = 12; i >= 0; i = i - 1) {
      this.freecellMultimoveGame.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 3);
    }

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame.move(PileType.CASCADE, 2, 12,
            PileType.FOUNDATION, 2);

    //form valid build of two cards
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 12,
            PileType.CASCADE, 2);

    //move card from cascade to open
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 11,
            PileType.OPEN, 0);

    //form valid build of three cards
    this.freecellMultimoveGame.move(PileType.CASCADE, 2, 11,
            PileType.CASCADE, 0);

    //move build from cascade to cascade
    //invalid because build size of 3 is greater than ((0 + 1) * 2 ^ 1)
    this.freecellMultimoveGame.move(PileType.CASCADE, 0, 10,
            PileType.CASCADE, 3);
  }

  @Test
  public void testMoveBuildSizeFourFromCascadeToCascadeOneOpenPileOneEmptyCascadePileValid() {
    //start game
    this.freecellMultimoveGame2.startGame(deck1, 4, 2, false);

    //move all cards from one cascade pile to one foundation pile
    //leaves us with one empty cascade pile
    for (int i = 12; i >= 0; i = i - 1) {
      this.freecellMultimoveGame2.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 3);
    }

    //move some cards from one cascade pile to one foundation pile
    //allow valid build
    for (int i = 12; i >= 10; i = i - 1) {
      this.freecellMultimoveGame2.move(PileType.CASCADE, 1, i,
              PileType.FOUNDATION, 1);
    }

    //move necessary cards from cascade to foundation to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 2, 12,
            PileType.FOUNDATION, 2);

    //form valid build of two cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 12,
            PileType.CASCADE, 2);

    //move card from cascade to open to allow valid build
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 11,
            PileType.OPEN, 0);

    //form valid build of three cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 2, 11,
            PileType.CASCADE, 0);

    //form valid build of four cards
    this.freecellMultimoveGame2.move(PileType.CASCADE, 0, 10,
            PileType.CASCADE, 1);

    //move build from cascade to cascade
    this.freecellMultimoveGame2.move(PileType.CASCADE, 1, 9,
            PileType.CASCADE, 3);

    //check cards in cascade after moving build from cascade to cascade
    assertEquals(new Card(4, "♦"),
            this.freecellMultimoveGame2.getCascadeCardAt(3, 0));
    assertEquals(new Card(3, "♣"),
            this.freecellMultimoveGame2.getCascadeCardAt(3, 1));
    assertEquals(new Card(2, "♥"),
            this.freecellMultimoveGame2.getCascadeCardAt(3, 2));
    assertEquals(new Card(1, "♣"),
            this.freecellMultimoveGame2.getCascadeCardAt(3, 3));
  }
}