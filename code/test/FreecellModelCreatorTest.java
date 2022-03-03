import org.junit.Test;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.model.multimove.MultimoveFreecellModel;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test cases for the freecell model creator factory class.
 */
public class FreecellModelCreatorTest {

  @Test
  public void testCreateSingleMove() {
    FreecellModel game = FreecellModelCreator.create(FreecellModelCreator.GameType.SINGLEMOVE);

    assertEquals(true, game instanceof SimpleFreecellModel);
    assertEquals(false, game instanceof MultimoveFreecellModel);
  }

  @Test
  public void testCreateMultimove() {
    FreecellModel game = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);

    assertEquals(true, game instanceof MultimoveFreecellModel);
    assertEquals(true, game instanceof SimpleFreecellModel);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullGameType() {
    FreecellModel game = FreecellModelCreator.create(null);
  }
}