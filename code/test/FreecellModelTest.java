import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;

/**
 * JUnit test cases for the freecell model.
 */

public class FreecellModelTest {

  private SimpleFreecellModel freecellGame;
  private List<Card> deck1;

  @Before
  public void setUp() {
    this.freecellGame = new SimpleFreecellModel();
    this.deck1 = this.freecellGame.getDeck();
  }

  @Test
  public void testConstructorSimpleFreecellModel() {
    //check if cascade piles and open piles are not null ->
    //should not be null if constructor is working
    assertEquals(-1, this.freecellGame.getNumCascadePiles());
    assertEquals(-1, this.freecellGame.getNumOpenPiles());
  }

  @Test
  public void testGetDeck() {
    assertEquals(new Card(1, "♣"), deck1.get(0));
    assertEquals(new Card(13, "♠"), deck1.get(51));
  }

  @Test
  public void testStartGame() {
    //-1 if game has not started
    assertEquals(-1, this.freecellGame.getNumCascadePiles());
    assertEquals(-1, this.freecellGame.getNumOpenPiles());

    //start game
    this.freecellGame.startGame(deck1, 4, 4, false);

    //respective sizes when game has started
    assertEquals(4, this.freecellGame.getNumCascadePiles());
    assertEquals(13, this.freecellGame.getNumCardsInCascadePile(0));
    assertEquals(4, this.freecellGame.getNumOpenPiles());
  }

  @Test
  public void testMove() {
    //start game
    this.freecellGame.startGame(deck1, 4, 4, false);

    //check move from nonempty cascade to empty open
    assertEquals(new Card(13, "♣"),
            this.freecellGame.getCascadeCardAt(0, 12));
    this.freecellGame.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);
    assertEquals(new Card(13, "♣"), this.freecellGame.getOpenCardAt(0));
    assertEquals(new Card(12, "♣"), this.freecellGame.getCascadeCardAt(0,
            this.freecellGame.getNumCardsInCascadePile(0) - 1));

    //check move from nonempty cascade to nonempty open (invalid)
    assertEquals(new Card(13, "♣"), this.freecellGame.getOpenCardAt(0));
    try {
      this.freecellGame.move(PileType.CASCADE, 1, 12,
              PileType.OPEN, 0);
      fail("Exception not found!");
    } catch (IllegalArgumentException e) {
      //test passes
    }

    //check move from nonempty cascade to nonempty foundation (invalid)
    this.freecellGame.startGame(deck1, 4, 4, false);
    try {
      this.freecellGame.move(PileType.CASCADE, 0, 12,
              PileType.FOUNDATION, 0);
      fail("Exception not found!");
    } catch (IllegalArgumentException e) {
      //test passes
    }

    //check move from nonempty cascade to nonempty cascade
    this.freecellGame.startGame(deck1, 8, 4, false);
    assertEquals(new Card(13, "♦"),
            this.freecellGame.getCascadeCardAt(1, 6));
    this.freecellGame.move(PileType.CASCADE, 4, 5,
            PileType.CASCADE, 1);
    assertEquals(new Card(12, "♣"),
            this.freecellGame.getCascadeCardAt(1, 7));
    assertEquals(new Card(10, "♣"),
            this.freecellGame.getCascadeCardAt(4, 4));

    //check move from nonempty cascade to nonempty cascade (invalid)
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.move(PileType.CASCADE, 0, 12,
              PileType.CASCADE, 1);
      fail("Exception not found!");
    } catch (IllegalArgumentException e) {
      //test passes
    }

    //creating a deck that allows for the cards at front of cascade piles to be put into
    // foundation pile
    List<Card> testDeck = new ArrayList<>(52);
    for (int i = 13; i > 0; i--) {
      testDeck.add(new Card(i, "♣"));
      testDeck.add(new Card(i, "♦"));
      testDeck.add(new Card(i, "♥"));
      testDeck.add(new Card(i, "♠"));
    }

    //move from nonempty cascade pile into foundation pile
    this.freecellGame.startGame(testDeck, 4, 4, false);
    assertEquals(new Card(1, "♣"),
            this.freecellGame.getCascadeCardAt(0, 12));
    this.freecellGame.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);
    assertEquals(new Card(1, "♣"), this.freecellGame.getOpenCardAt(0));
    assertEquals(new Card(2, "♣"),
            this.freecellGame.getCascadeCardAt(0, 11));

    //move from nonempty cascade pile into nonempty foundation (invalid)
    try {
      this.freecellGame.startGame(testDeck, 4, 4, false);
      this.freecellGame.move(PileType.CASCADE, 0, 12,
              PileType.FOUNDATION, 0);
      this.freecellGame.move(PileType.CASCADE, 1, 12,
              PileType.FOUNDATION, 0);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testIsGameOver() {
    //creating a deck that allows for the cards at front of cascade piles to be put into
    // foundation pile
    List<Card> testDeck = new ArrayList<>(52);
    for (int i = 13; i > 0; i--) {
      testDeck.add(new Card(i, "♣"));
      testDeck.add(new Card(i, "♦"));
      testDeck.add(new Card(i, "♥"));
      testDeck.add(new Card(i, "♠"));
    }

    //start game
    this.freecellGame.startGame(testDeck, 4, 4, false);

    assertEquals(false, this.freecellGame.isGameOver());

    //move all cards into foundation piles
    for (int i = 12; i < 0; i--) {
      this.freecellGame.move(PileType.CASCADE, 0, i,
              PileType.FOUNDATION, 0);
      this.freecellGame.move(PileType.CASCADE, 1, i,
              PileType.FOUNDATION, 1);
      this.freecellGame.move(PileType.CASCADE, 2, i,
              PileType.FOUNDATION, 2);
      this.freecellGame.move(PileType.CASCADE, 3, i,
              PileType.FOUNDATION, 3);

      //why does this test pass?
      assertEquals(true, this.freecellGame.isGameOver());
    }
  }

  @Test
  public void testGetNumCardsInFoundationPile() {
    //throw error if game has not started
    try {
      this.freecellGame.getNumCardsInFoundationPile(1);
      fail("Exception not found");
    } catch (IllegalStateException e) {
      //test passes
    }

    //throw error if pile index is invalid (not one of the four foundation piles)
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getNumCardsInFoundationPile(-1);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getNumCardsInFoundationPile(4);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }

    //creating a deck that allows for the cards at front of cascade piles to be put into
    // foundation pile
    List<Card> testDeck = new ArrayList<>(52);
    for (int i = 13; i > 0; i--) {
      testDeck.add(new Card(i, "♣"));
      testDeck.add(new Card(i, "♦"));
      testDeck.add(new Card(i, "♥"));
      testDeck.add(new Card(i, "♠"));
    }

    //start game
    this.freecellGame.startGame(testDeck, 4, 4, false);

    //game started and pile index is valid
    assertEquals(0, this.freecellGame.getNumCardsInFoundationPile(0));

    //move a card into foundation pile index 0
    this.freecellGame.move(PileType.CASCADE, 0, 12,
            PileType.FOUNDATION, 0);

    //after move
    assertEquals(1,
            this.freecellGame.getNumCardsInFoundationPile(0));
  }

  @Test
  public void testGetNumCascadePiles() {
    //-1 if game has not started
    assertEquals(-1, this.freecellGame.getNumCascadePiles());

    //start game
    this.freecellGame.startGame(deck1, 8, 4, false);

    //specified size after game has started
    assertEquals(8, this.freecellGame.getNumCascadePiles());
  }

  @Test
  public void testGetNumCardsInCascadePile() {
    //throw error if game has not started
    try {
      this.freecellGame.getNumCardsInCascadePile(1);
      fail("Exception not found");
    } catch (IllegalStateException e) {
      //test passes
    }

    //throw error if pile index is invalid (not within indices of cascade piles)
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getNumCardsInCascadePile(-1);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getNumCardsInCascadePile(5);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }

    //start game
    this.freecellGame.startGame(deck1, 4, 4, false);

    //game started and pile index is valid
    assertEquals(13, this.freecellGame.getNumCardsInCascadePile(0));

    //Move one card from cascade pile index 0
    this.freecellGame.move(PileType.CASCADE, 0, 12,
            PileType.OPEN, 0);

    //after move
    assertEquals(12, this.freecellGame.getNumCardsInCascadePile(0));
  }

  @Test
  public void testGetNumCardsInOpenPile() {
    //throw error if game has not started
    try {
      this.freecellGame.getNumCardsInOpenPile(1);
      fail("Exception not found");
    } catch (IllegalStateException e) {
      //test passes
    }

    //throw error if pile index is invalid (not within indices of open piles)
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getNumCardsInOpenPile(-1);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getNumCardsInOpenPile(5);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }

    //start game
    this.freecellGame.startGame(deck1, 4, 4, false);

    //game started and pile is valid
    assertEquals(0, this.freecellGame.getNumCardsInOpenPile(0));

    //move a card into open pile index 0
    this.freecellGame.move(PileType.CASCADE, 1, 12,
            PileType.OPEN, 0);

    //after move
    assertEquals(1, this.freecellGame.getNumCardsInOpenPile(0));
  }

  @Test
  public void testGetNumOpenPiles() {
    //-1 if game has not started
    assertEquals(-1, this.freecellGame.getNumOpenPiles());

    //start game
    this.freecellGame.startGame(deck1, 8, 4, false);

    //specified size after game has started
    assertEquals(4, this.freecellGame.getNumOpenPiles());
  }

  @Test
  public void testGetFoundationCardAt() {
    //throw error if game has not started
    try {
      this.freecellGame.getFoundationCardAt(0, 12);
      fail("Exception not found");
    } catch (IllegalStateException e) {
      //test passes
    }

    //throw error if pile index is invalid (not one of the four foundation piles)
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getFoundationCardAt(-1, 0);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }

    //throw error if card index is invalid (card not in the pile)
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getFoundationCardAt(0, 50);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }


    //creating a deck that allows for the cards at front of cascade piles to be put into
    // foundation pile
    List<Card> testDeck = new ArrayList<>(52);
    for (int i = 13; i > 0; i--) {
      testDeck.add(new Card(i, "♣"));
      testDeck.add(new Card(i, "♦"));
      testDeck.add(new Card(i, "♥"));
      testDeck.add(new Card(i, "♠"));
    }

    //start game
    this.freecellGame.startGame(testDeck, 4, 4, false);

    //game started and pile index is valid
    assertEquals(0, this.freecellGame.getNumCardsInFoundationPile(0));

    //move a card into foundation pile index 0
    this.freecellGame.move(PileType.CASCADE, 0, 12,
            PileType.FOUNDATION, 0);

    //after move
    assertEquals(new Card(1, "♣"),
            this.freecellGame.getFoundationCardAt(0, 0));
  }

  @Test
  public void testGetCascadeCardAt() {
    //throw error if game has not started
    try {
      this.freecellGame.getCascadeCardAt(0, 12);
      fail("Exception not found");
    } catch (IllegalStateException e) {
      //test passes
    }

    //throw error if pile index is invalid (not within indices of cascade piles)
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getCascadeCardAt(-1, 0);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }

    //throw error if card index is invalid (card not in the pile)
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getCascadeCardAt(0, 50);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }

    //start game
    this.freecellGame.startGame(deck1, 4, 4, false);

    assertEquals(new Card(1, "♣"),
            this.freecellGame.getCascadeCardAt(0, 0));
    assertEquals(new Card(6, "♦"),
            this.freecellGame.getCascadeCardAt(1, 5));
  }

  @Test
  public void testGetOpenCardAt() {
    //throw error if game has not started
    try {
      this.freecellGame.getOpenCardAt(0);
      fail("Exception not found");
    } catch (IllegalStateException e) {
      //test passes
    }

    //throw error if pile index is invalid (not within indices of open piles)
    try {
      this.freecellGame.startGame(deck1, 4, 4, false);
      this.freecellGame.getOpenCardAt(-1);
      fail("Exception not found");
    } catch (IllegalArgumentException e) {
      //test passes
    }

    //start game
    this.freecellGame.startGame(deck1, 4, 4, false);

    //move card into open pile index 1
    this.freecellGame.move(PileType.CASCADE, 3, 12,
            PileType.OPEN, 1);

    //get card in open pile index 1
    assertEquals(new Card(13, "♠"), this.freecellGame.getOpenCardAt(1));

    //move card into open pile index 2
    this.freecellGame.move(PileType.CASCADE, 2, 12,
            PileType.OPEN, 2);

    //get card in open pile index 2
    assertEquals(new Card(13, "♥"), this.freecellGame.getOpenCardAt(2));
  }
}