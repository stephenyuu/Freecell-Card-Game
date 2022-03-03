package cs3500.freecell.view;

import java.io.IOException;

import cs3500.freecell.model.FreecellModelState;

/**
 * The {@code FreecellTextView} class represents the view of Freecell game.
 */
public class FreecellTextView implements FreecellView {
  private final FreecellModelState<?> model;
  private Appendable dest;

  /**
   * Constructs a {@code FreecellTextView} object.
   *
   * @param model the given freecell model
   */
  public FreecellTextView(FreecellModelState<?> model) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null");
    }
    this.model = model;
  }

  /**
   * Constructs a {@code FreecellTextView} object transmitted to a given destination.
   *
   * @param model the given Freecell model
   * @param dest  the given data destination
   */
  public FreecellTextView(FreecellModelState<?> model, Appendable dest) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null");
    }
    this.model = model;

    this.dest = dest;
  }

  @Override
  public String toString() {
    //checks if game has started
    //if game has not started, return empty string
    if (this.model.getNumCascadePiles() == -1) {
      return "";
    } else {

      //initial string is empty string
      String appendedString = "";

      //foundation piles
      for (int pIdx = 0; pIdx < 4; pIdx++) {
        //checks if pile is empty
        if (this.model.getNumCardsInFoundationPile(pIdx) == 0) {
          appendedString = appendedString + "F" + (pIdx + 1) + ":\n";
        } else {
          appendedString = appendedString + "F" + (pIdx + 1) + ": ";
        }
        for (int cIdx = 0; cIdx < this.model.getNumCardsInFoundationPile(pIdx); cIdx++) {
          //checks if card is the last card in pile
          if (cIdx == this.model.getNumCardsInFoundationPile(pIdx) - 1) {
            appendedString = appendedString + this.model.getFoundationCardAt(pIdx, cIdx).toString()
                    + "\n";
          } else {
            appendedString = appendedString + this.model.getFoundationCardAt(pIdx, cIdx).toString()
                    + ", ";
          }
        }
      }

      //open piles
      for (int pIdx = 0; pIdx < this.model.getNumOpenPiles(); pIdx++) {
        //checks if pile is empty
        if (this.model.getNumCardsInOpenPile(pIdx) == 0) {
          appendedString = appendedString + "O" + (pIdx + 1) + ":\n";
        } else {
          appendedString = appendedString + "O" + (pIdx + 1) + ": ";
        }
        for (int cIdx = 0; cIdx < this.model.getNumCardsInOpenPile(pIdx); cIdx++) {
          //checks if card is the last card in pile
          if (cIdx == this.model.getNumCardsInOpenPile(pIdx) - 1) {
            appendedString = appendedString + this.model.getOpenCardAt(pIdx).toString() + "\n";
          } else {
            return appendedString + this.model.getOpenCardAt(pIdx).toString() + ", ";
          }
        }
      }

      //cascade piles
      for (int pIdx = 0; pIdx < this.model.getNumCascadePiles(); pIdx++) {
        //checks if pile is empty
        if (this.model.getNumCardsInCascadePile(pIdx) == 0) {
          appendedString = appendedString + "C" + (pIdx + 1) + ":\n";
        } else {
          appendedString = appendedString + "C" + (pIdx + 1) + ": ";
        }
        for (int cIdx = 0; cIdx < this.model.getNumCardsInCascadePile(pIdx); cIdx++) {
          //checks if card is last card of last pile
          if ((pIdx == this.model.getNumCascadePiles() - 1)
                  && (cIdx == this.model.getNumCardsInCascadePile(pIdx) - 1)) {
            appendedString = appendedString + this.model.getCascadeCardAt(pIdx, cIdx).toString();
          }
          //checks if card is the last card in pile
          else if (cIdx == this.model.getNumCardsInCascadePile(pIdx) - 1) {
            appendedString = appendedString + this.model.getCascadeCardAt(pIdx, cIdx).toString()
                    + "\n";
          } else {
            appendedString = appendedString + this.model.getCascadeCardAt(pIdx, cIdx).toString()
                    + ", ";
          }
        }
      }

      return appendedString;
    }
  }

  @Override
  public void renderBoard() throws IOException {
    dest.append(this.toString());
  }

  @Override
  public void renderMessage(String message) throws IOException {
    dest.append(message);
  }
}