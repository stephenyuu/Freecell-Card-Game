import java.io.IOException;

/**
 * The {@code FailingAppendable} class represents an invalid appendable whose main purpose
 * is to throw the exception. This class is used to test the case in which IOException is thrown.
 */
public class FailingAppendable implements Appendable {
  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException();
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException();
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException();
  }
}