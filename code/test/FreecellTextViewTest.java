import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.view.FreecellTextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * JUnit test cases for freecell text view.
 */
public class FreecellTextViewTest {

  private SimpleFreecellModel freecellModel;
  private List<Card> deck1;
  private FreecellTextView freecellView;

  private Appendable appendable;
  private FreecellTextView freecellView2;

  private FailingAppendable failingAppendable;
  private FreecellTextView freecellViewFailing;

  @Before
  public void setUp() {
    this.freecellModel = new SimpleFreecellModel();
    this.deck1 = this.freecellModel.getDeck();
    this.freecellView = new FreecellTextView(freecellModel);

    this.appendable = new StringBuilder();
    this.freecellView2 = new FreecellTextView(freecellModel, appendable);

    this.failingAppendable = new FailingAppendable();
    this.freecellViewFailing = new FreecellTextView(freecellModel, failingAppendable);
  }

  @Test
  public void testFreecellTextViewConstructor() {
    try {
      FreecellTextView nullModel = new FreecellTextView(null);
      fail("Exception not found!");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testToString() {
    //empty string if game has not started
    assertEquals("", this.freecellView.toString());

    //start game
    this.freecellModel.startGame(deck1, 8, 4, false);

    //string if game has started
    assertEquals("F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣, K♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠", this.freecellView.toString());
  }

  @Test
  public void testFreecellTextViewConstructor2() {
    try {
      FreecellTextView nullModel = new FreecellTextView(null, appendable);
      fail("Exception not found!");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testRenderBoardFailingAppendable() {
    //if given appendable is invalid
    try {
      this.freecellViewFailing.renderBoard();
      fail("Exception not found!");
    } catch (IOException ioe) {
      //test passes
    }
  }

  @Test
  public void testRenderBoardGameNotStarted() throws IOException {
    //empty string if game has not started
    this.freecellView2.renderBoard();
    assertEquals("", appendable.toString());
  }

  @Test
  public void testRenderBoardGameStarted() throws IOException {
    //start game
    this.freecellModel.startGame(deck1, 8, 4, false);

    //string if game has started
    this.freecellView2.renderBoard();
    assertEquals("F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣, K♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠", appendable.toString());
  }

  @Test
  public void testRenderMessageFailingAppendable() {
    try {
      this.freecellViewFailing.renderMessage("abc");
      fail("Exception not found!");
    } catch (IOException ioe) {
      //test passes
    }
  }

  @Test
  public void testRenderMessage() throws IOException {
    this.freecellView2.renderMessage("abc");
    assertEquals("abc", appendable.toString());
  }
}