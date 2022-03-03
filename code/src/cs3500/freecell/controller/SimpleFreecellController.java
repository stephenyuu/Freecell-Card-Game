package cs3500.freecell.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;

/**
 * The {@code SimpleFreecellController} class represents the controller that handles user input for
 * Freecell game.
 *
 * @param <Card> the Card type
 */
public class SimpleFreecellController<Card> implements FreecellController<Card> {
  private final FreecellModel<Card> model;
  private final Readable input;
  private final Appendable output;
  private final Scanner scan;
  private final FreecellView view;
  private boolean didPlayerQuit;

  /**
   * Constructs a {@SimpleFreecellController} object.
   *
   * @param model the given Freecell model
   * @param rd    the player's stream of game moves (the input)
   * @param ap    the given destination where the game is being written onto (the output)
   */
  public SimpleFreecellController(FreecellModel<Card> model, Readable rd, Appendable ap) {
    if (model == null || rd == null || ap == null) {
      throw new IllegalArgumentException("Inputs cannot be null!");
    }
    this.model = model;
    this.input = rd;
    this.output = ap;
    scan = new Scanner(input);
    this.view = new FreecellTextView(model, ap);
    this.didPlayerQuit = false;
  }

  @Override
  public void playGame(List<Card> deck, int numCascades, int numOpens, boolean shuffle) {
    //check if deck is null
    if (deck == null) {
      throw new IllegalArgumentException("Deck cannot be null!");
    }

    //start the game
    try {
      model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException iae) {
      try {
        view.renderMessage("Could not start game.");
      } catch (IOException e) {
        throw new IllegalStateException();
      }
      //stop the method if game cannot be started
      return;
    }

    //gameplay when game is ongoing (when isGameOver is false)
    while (!model.isGameOver()) {
      //render the game board
      try {
        view.renderBoard();
        output.append("\n");
      } catch (IOException e) {
        throw new IllegalStateException();
      }

      //ask for inputs (arranged in a list of 3 elements: source, card index, destination)
      //called user input list = organized as (pile input, card index input, pile input)
      List<String> userMove = new ArrayList<>(3);

      getPileInput(userMove, scan);
      //stop the game if quit detected
      if (didPlayerQuit) {
        return;
      }

      getCardIdxInput(userMove, scan);
      //stop the game if quit detected
      if (didPlayerQuit) {
        return;
      }

      getPileInput(userMove, scan);
      //stop the game if quit detected
      if (didPlayerQuit) {
        return;
      }

      //extract the move parameters from input list
      PileType moveSourcePileType = extractPileType(userMove.get(0));
      int moveSourcePileNum = extractPileNum(userMove.get(0));
      int moveCardIdx = extractCardIdx(userMove.get(1));
      PileType moveDestPileType = extractPileType(userMove.get(2));
      int moveDestPileNum = extractPileNum(userMove.get(2));

      //execute the move with provided inputs by controller
      try {
        model.move(moveSourcePileType, moveSourcePileNum, moveCardIdx,
                moveDestPileType, moveDestPileNum);
      } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
        printMessage("Invalid move. Try again");
      }
    }

    //when game has finished (when isGameOver is true)
    //transmit final game state
    output.toString();

    //print final message
    printMessage("Game over.");
  }

  /**
   * Get the card index from the user input list to be passed into move method in model.
   *
   * @param s the card index input in user input list as a string
   * @return the card index as an integer
   */
  private int extractCardIdx(String s) {
    return Integer.parseInt(s) - 1;
  }

  /**
   * Get the pile index from the user input list to be passed into move method in model.
   *
   * @param s the pile input in user input list as a string
   * @return the pile index as an integer
   */
  private int extractPileNum(String s) {
    //get the rest of the string
    String restOfString = s.substring(1);

    return Integer.parseInt(restOfString) - 1;
  }

  /**
   * Get the pile type from the user input list to be passed into move method in model.
   *
   * @param s the pile input in user input list as a string
   * @return the pile type as a PileType
   */
  private PileType extractPileType(String s) {
    //get the first character of the string
    Character firstChar = s.charAt(0);

    switch (firstChar) {
      case 'C':
        return PileType.CASCADE;
      case 'F':
        return PileType.FOUNDATION;
      default:
        return PileType.OPEN;
    }
  }

  /**
   * Get the next valid card index input and add it to user input list.
   *
   * @param userMove the user input list (represents a move when all 3 elements are found)
   * @param scan     the given Scanner to scan through player's stream of game moves
   */
  private void getCardIdxInput(List<String> userMove, Scanner scan) {
    while (true) {
      //get the next element from input
      String cardIdxString = getNext(scan);

      //check if element is "q" or "Q"
      detectPlayerQuit(cardIdxString);
      //stop the method if quit detected
      if (didPlayerQuit) {
        return;
      }

      //check if card index is an integer
      //if parseInt throws error -> cardIdxString is not an integer -> invalid
      try {
        Integer.parseInt(cardIdxString);
        userMove.add(cardIdxString);
        //card index found -> stop execution of loop
        return;
      } catch (NumberFormatException nfe) {
        printMessage("Invalid card index. Try again.");
      }
    }
  }

  /**
   * Get the next valid pile input and add it to user input list.
   *
   * @param userMove the user input list (represents a move when all 3 elements are found)
   * @param scan     the given Scanner to scan through player's stream of game moves
   */
  private void getPileInput(List<String> userMove, Scanner scan) {
    while (true) {
      //get the next element from input
      String pileString = getNext(scan);

      //check if element is "q" or "Q"
      detectPlayerQuit(pileString);
      //stop the method if quit detected
      if (didPlayerQuit) {
        return;
      }

      //get the first character
      Character firstChar = pileString.charAt(0);

      //get the rest of the string
      String restOfString = pileString.substring(1);

      //if first character of pileString is valid...
      if (validPileType(firstChar)) {
        //if the rest of pileString is an integer...
        if (pileIdxIsInt(restOfString)) {
          //add to position in input list
          userMove.add(pileString);
          //pile found -> stop execution of loop
          return;
        } else {
          //get the next element that completes pileString (need int for pileIdx)
          String restOfPileString = getIntegerPileIdx(scan, firstChar);
          if (restOfPileString == null) {
            return;
          } else {
            userMove.add(firstChar + restOfPileString);
            //pile found -> stop execution of loop
            return;
          }
        }
      }
    }
  }

  /**
   * Get the next element that is a valid pile index that completes the pile input string
   * and return it as a string.
   * Handles the case in which a valid pile type is found, but a valid pile index does not follow
   * immediately.
   * For example, "CHSJSH 1" would become the pile input "C1".
   *
   * @param scan      the given Scanner to scan through player's stream of game moves
   * @param firstChar the first character of the pile input string,
   *                  represents the pile type that has been found already
   * @return the pile index as a string that completes the pile input string
   */
  private String getIntegerPileIdx(Scanner scan, Character firstChar) {
    while (true) {
      //get the next element from input
      String nextElementToCompletePileString = getNext(scan);

      //check if element is "q" or "Q"
      detectPlayerQuit(nextElementToCompletePileString);
      //stop the method if quit detected
      if (didPlayerQuit) {
        return null;
      }

      //check if nextElementToCompletePileString is an integer
      //if parseInt throws error -> restOfString is not an integer -> invalid
      try {
        Integer.parseInt(nextElementToCompletePileString);
        return nextElementToCompletePileString;
      } catch (NumberFormatException nfe) {
        printMessage("Invalid pile index. Try again.");
      }
    }
  }

  /**
   * Checks if the rest of pile input string is an integer.
   * Therefore, a valid pile index.
   *
   * @param restOfString substring of pile input string (from second element in string onwards)
   * @return if the rest of pile input string is a valid pile index
   */
  private boolean pileIdxIsInt(String restOfString) {
    //check if restOfString is an integer
    //if parseInt throws error -> restOfString is not an integer -> invalid -> ask for another input
    try {
      Integer.parseInt(restOfString);
      return true;
    } catch (NumberFormatException nfe) {
      printMessage("Invalid pile index. Try again.");
      return false;
    }
  }

  /**
   * Checks if the first character of pile input string is a 'C', 'F', or 'O'.
   * Therefore, a valid pile type.
   *
   * @param firstChar the first character of pile input string
   * @return if the first character of pile input string is a valid pile type
   */
  private boolean validPileType(Character firstChar) {
    //check if first character is 'C', 'F', or 'O'
    //if not -> invalid -> ask for another input
    if (firstChar.equals('C') || firstChar.equals('F') || firstChar.equals('O')) {
      return true;
    } else {
      printMessage("Invalid pile type. Try again.");
      return false;
    }
  }

  /**
   * Get the next element from the player's stream of game moves.
   *
   * @param scan the given Scanner to scan through player's stream of game moves
   * @return the next element
   */
  private String getNext(Scanner scan) {
    try {
      String nextElement = scan.next();
      return nextElement;
    } catch (NoSuchElementException nsee) {
      throw new IllegalStateException();
    }
  }

  /**
   * Check if element is a "q" or "Q" to determine if player has quit game.
   *
   * @param s the next element from the Scanner
   */
  private void detectPlayerQuit(String s) {
    //check if string is "q" or "Q"
    //if it is print end game message and set didPlayerQuit to true
    if (s.equals("q") || s.equals("Q")) {
      printMessage("Game quit prematurely.");
      this.didPlayerQuit = true;
    }
  }

  /**
   * Print the given message.
   *
   * @param s the given message
   */
  private void printMessage(String s) {
    //append the messgae to the appendable and enter new line
    //handle IOException
    try {
      view.renderMessage(s);
      output.append("\n");
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }
}