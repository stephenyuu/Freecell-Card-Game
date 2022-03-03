import org.junit.Before;
import org.junit.Test;

import cs3500.freecell.model.Card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * JUnit test cases for card.
 */
public class CardTest {

  private Card card1;
  private Card card2;
  private Card card3;
  private Card card4;
  private Card card5;
  private Card card6;
  private Card card7;


  @Before
  public void setUp() {
    card1 = new Card(1, "♣");
    card2 = new Card(2, "♣");
    card3 = new Card(2, "♦");
    card4 = new Card(8, "♥");
    card5 = new Card(11, "♦");
    card6 = new Card(12, "♥");
    card7 = new Card(13, "♠");

  }

  @Test
  public void testCardConstructor() {
    try {
      Card invalidCard1 = new Card(15, "♣");
      fail("Exception not found!");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      Card invalidCard2 = new Card(1, "abc");
      fail("Exception not found!");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testToString() {
    assertEquals("A♣", card1.toString());
    assertEquals("J♦", card5.toString());
    assertEquals("Q♥", card6.toString());
    assertEquals("K♠", card7.toString());
    assertEquals("8♥", card4.toString());
  }

  @Test
  public void testIsValidCard() {
    assertEquals(true, card1.isValidCard());
    assertEquals(true, card5.isValidCard());
  }

  @Test
  public void testDoesValueEqualA() {
    assertEquals(true, card1.doesValueEqualA());
    assertEquals(false, card2.doesValueEqualA());
  }

  @Test
  public void testIsValidMoveToNonEmptyFoundation() {
    assertEquals(true, card2.isValidMoveToNonEmptyFoundation(card1));
    assertEquals(false, card3.isValidMoveToNonEmptyFoundation(card1));
    assertEquals(true, card6.isValidMoveToNonEmptyCascade(card7));
  }

  @Test
  public void testIsValidMoveToNonEmptyCascade() {
    assertEquals(true, card1.isValidMoveToNonEmptyCascade(card3));
    assertEquals(false, card1.isValidMoveToNonEmptyCascade(card2));
  }

  @Test
  public void testEquals() {
    assertEquals(true, card1.equals(card1));
    assertEquals(false, card2.equals(card3));
  }
}