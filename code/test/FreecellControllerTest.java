import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.model.multimove.MultimoveFreecellModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * JUnit test cases for the freecell controller.
 */
public class FreecellControllerTest {

  private Appendable ap;
  private FreecellModel model;
  private List<Card> deck;
  private List<Card> winDeck;

  //for new tests for controller for multimove Freecell
  private FreecellModel multimoveModel;

  @Before
  public void setUp() {
    this.ap = new StringBuilder();
    this.model = new SimpleFreecellModel();
    this.deck = model.getDeck();
    this.winDeck = new ArrayList<>(52);

    for (int i = 13; i > 0; i--) {
      winDeck.add(new Card(i, "♣"));
      winDeck.add(new Card(i, "♦"));
      winDeck.add(new Card(i, "♥"));
      winDeck.add(new Card(i, "♠"));
    }

    //for new tests for controller for multimove Freecell
    this.multimoveModel = new MultimoveFreecellModel();
  }

  @Test
  public void testNullDeck() {
    List<Card> nullDeck = null;
    Readable rd = new StringReader("C q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    try {
      ctrl.playGame(nullDeck, 4, 1, false);
      fail("Exception not found!");
    } catch (IllegalArgumentException iae) {
      //test passes
    }
  }

  @Test
  public void testNullAppendable() {
    try {
      Readable rd = new StringReader("C");
      FreecellController nullDeckCtrl = new SimpleFreecellController(model, rd, null);
      fail("Exception not found!");
    } catch (IllegalArgumentException iae) {
      //test passes
    }
  }

  @Test
  public void testNullReadable() {
    try {
      Readable rd = null;
      FreecellController nullDeckCtrl = new SimpleFreecellController(model, rd, ap);
      fail("Exception not found!");
    } catch (IllegalArgumentException iae) {
      //test passes
    }
  }

  @Test
  public void testCouldNotStartGame() {
    List<Card> nullDeck = null;
    Readable rd = new StringReader("C q");
    FreecellModel invalidModel = new SimpleFreecellModel();
    FreecellController ctrl = new SimpleFreecellController(invalidModel, rd, ap);

    ctrl.playGame(deck, -1, 1, false);

    assertEquals("Could not start game.", ap.toString());
  }

  @Test
  public void testInputPileTypeC() {
    Readable rd = new StringReader("C q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //no pile index is provided -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeCWithIntIdx() {
    Readable rd = new StringReader("C1 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //accept input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testValidPileTypeCWithVeryLargeIntIdx() {
    Readable rd = new StringReader("C999999 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //accept input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeCWithNegativeIntIdx() {
    Readable rd = new StringReader("C-11 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //accept input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeCWithStringIdx() {
    Readable rd = new StringReader("CJKJaj q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile index is a string -> invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeCWithStringIdxFollowedByIntIdx() {
    Readable rd = new StringReader("CJKJaj 1 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile index is a string -> invalid -> prompt to try again ->
    //accept next valid input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeMinusC() {
    Readable rd = new StringReader("-C q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile type is invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile type. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeF() {
    Readable rd = new StringReader("F q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //no pile index is provided -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeFWithIntIdx() {
    Readable rd = new StringReader("F1 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //accept input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testValidPileTypeFWithVeryLargeIntIdx() {
    Readable rd = new StringReader("F999999 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //accept input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeFWithNegativeIntIdx() {
    Readable rd = new StringReader("F-11 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //accept input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeFWithStringIdx() {
    Readable rd = new StringReader("FJKJaj q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile index is a string -> invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeFWithStringIdxFollowedByIntIdx() {
    Readable rd = new StringReader("FJKJaj sdada3 ddd 3 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile index is a string -> invalid -> prompt to try again ->
    //accept next valid input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile index. Try again.\n" +
            "Invalid pile index. Try again.\n" +
            "Invalid pile index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeMinusF() {
    Readable rd = new StringReader("-F q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile type is invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile type. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeO() {
    Readable rd = new StringReader("O q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //no pile index is provided -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeOWithIntIdx() {
    Readable rd = new StringReader("O1 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //accept input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testValidPileTypeOWithVeryLargeIntIdx() {
    Readable rd = new StringReader("O999999 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //accept input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeOWithNegativeIntIdx() {
    Readable rd = new StringReader("O-11 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //accept input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeOWithStringIdx() {
    Readable rd = new StringReader("OJKJaj q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile index is a string -> invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeOWithStringIdxFollowedByIntIdx() {
    Readable rd = new StringReader("OJKJaj ssdnsd 3 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile index is a string -> invalid -> prompt to try again ->
    //accept next valid input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile index. Try again.\n" +
            "Invalid pile index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeMinusO() {
    Readable rd = new StringReader("-O q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile type is invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile type. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeOtherUppercaseLetterBesidesCFO() {
    Readable rd = new StringReader("D q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile type is invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile type. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeLowercaseC() {
    Readable rd = new StringReader("c q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile type is invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile type. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeLowercaseF() {
    Readable rd = new StringReader("f q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile type is invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile type. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputPileTypeLowercaseO() {
    Readable rd = new StringReader("o q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //pile type is invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid pile type. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputCardIntIdx() {
    Readable rd = new StringReader("C1 13 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //accept input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputCardStringIdx() {
    Readable rd = new StringReader("C1 13jdfafj q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //card index is a string -> invalid -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid card index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputCardStringIdxFollowedByIntIdx() {
    Readable rd = new StringReader("C1 13jdfafj 13 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //card index is a string -> invalid -> prompt to try again ->
    //accept next valid input (accepted if not prompted to try again)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid card index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInputDestPileString() {
    Readable rd = new StringReader("C1 13jdfafj 13 O q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //no pile index is provided -> prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid card index. Try again.\n" +
            "Invalid pile index. Try again.\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testValidMoveFromC1CIdx13ToO1() {
    Readable rd = new StringReader("C1 13 O1 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //if move is valid, display view after move
    //if move is invalid, prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: A♣\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testValidMoveFromC1CIdx13ToF1() {
    Readable rd = new StringReader("C1 13 F1 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //if move is valid, display view after move
    //if move is invalid, prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testValidMoveFromC1CIdx13ToF1WithMultipleSpacesAsWhitespace() {
    Readable rd = new StringReader("C1      13 F1 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //if move is valid, display view after move
    //if move is invalid, prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testValidMoveFromC1CIdx13ToF1WithInvalidInputsInBetweenValidInputs() {
    Readable rd = new StringReader("C1 jsdfajfa q232 13 jsjsjsF1 Fkjjs 1 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //if move is valid, display view after move
    //if move is invalid, prompt to try again
    //if input is invalid, prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid card index. Try again.\n" +
            "Invalid card index. Try again.\n" +
            "Invalid pile type. Try again.\n" +
            "Invalid pile index. Try again.\n" +
            "F1: A♣\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInvalidMoveFromC1CIdx12ToF1() {
    Readable rd = new StringReader("C1 12 F1 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //if move is valid, display view after move
    //if move is invalid, prompt to try again
    //if input is invalid, prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid move. Try again\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInvalidMoveFromC1CIdx12ToF1WithMultipleSpacesAsWhitespace() {
    Readable rd = new StringReader("C1   12  F1     q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //if move is valid, display view after move
    //if move is invalid, prompt to try again
    //if input is invalid, prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid move. Try again\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInvalidMoveFromC1CIdx12ToF1WithInvalidInputsInBetweenValidInputs() {
    Readable rd = new StringReader("C1 lksfdklf jksfkj 89jjjd 12 sdjfds JKSJKS Fdljkak 1 q");
    FreecellController ctrl = new SimpleFreecellController(model, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //if move is valid, display view after move
    //if move is invalid, prompt to try again
    //if input is invalid, prompt to try again
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid card index. Try again.\n" +
            "Invalid card index. Try again.\n" +
            "Invalid card index. Try again.\n" +
            "Invalid pile type. Try again.\n" +
            "Invalid pile type. Try again.\n" +
            "Invalid pile index. Try again.\n" +
            "Invalid move. Try again\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  /*
  @Test
  public void testWonGame() {
    //Loop that puts all cards into foundation piles
    //Print out the appendable
    //Should display all cards in foundation piles and game over message
  }
  */

  //Added new tests for controller for multimove Freecell

  @Test
  public void testValidBuildMoveFromCascadeToCascade() {
    Readable rd = new StringReader("C1 13 F1 C3 13 C1 C2 13 F2 C2 12 F2 C1 12 C2 q");
    FreecellController ctrl = new SimpleFreecellController(multimoveModel, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //valid move of build size 2 from cascade to cascade
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2: A♦\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2: A♦, 2♦\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2: A♦, 2♦\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♣, A♥\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }

  @Test
  public void testInvalidBuildMoveFromCascadeToCascadeInvalidSizeOfBuild() {
    Readable rd = new StringReader("C1 13 F1 C3 13 C1 C2 13 F2 C2 12 F2 C2 11 O1 C1 12 C2 q");
    FreecellController ctrl = new SimpleFreecellController(multimoveModel, rd, ap);

    ctrl.playGame(winDeck, 4, 1, false);

    //invalid move of build size 2 from cascade to cascade
    //invalid because build of size 2 is greater than ((0 + 1) * 2 ^ 0)
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2: A♦\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2: A♦, 2♦\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "F1: A♣\n" + "F2: A♦, 2♦\n" + "F3:\n" + "F4:\n" + "O1: 3♦\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Invalid move. Try again\n" + "F1: A♣\n" + "F2: A♦, 2♦\n" + "F3:\n" + "F4:\n" +
            "O1: 3♦\n" + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♥\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "Game quit prematurely.\n", ap.toString());
  }
}