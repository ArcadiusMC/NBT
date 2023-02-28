package net.forthecrown.nbt.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;

public class ReaderWrapper {

  // -1 = EOF, so -2 is reserved for no peek for the 'peeked' field
  static final int NO_PEEK = -2;
  static final int CONTEXT_LENGTH = 35;

  private static final Pattern VALID_WORD
      = Pattern.compile("[a-zA-Z0-9+_.-]+");

  private final Reader reader;
  private int peeked = NO_PEEK;

  private int position;
  private final StringBuilder context = new StringBuilder();
  private final StringBuilder input = new StringBuilder();

  public ReaderWrapper(Reader reader) {
    this.reader = Objects.requireNonNull(reader);
  }

  /**
   * Tests if the specified {@code string} matches the {@code [a-zA-Z0-9+_.-]+}
   * pattern for words that do not require quotes.
   *
   * @param string String to test
   * @return {@code true}, if the string is a valid word, {@code false}
   *         otherwise
   */
  public static boolean isValidWord(String string) {
    return VALID_WORD.matcher(string).matches();
  }

  /**
   * A single character version of {@link #isValidWord(String)}
   * @param c Character to test
   * @return True, if {@code c} is allowed inside an unquoted string
   */
  public static boolean isValidUnquotedChar(int c) {
    return (c >= 'a' && c <= 'z')
        || (c >= 'A' && c <= 'Z')
        || c == '_'
        || isNumericChar(c);
  }

  public static boolean isNumericChar(int c) {
    return (c >= '0' && c <= '9')
        || c == '-'
        || c == '+'
        || c == '.';
  }

  public static boolean isQuote(int c) {
    return c == '"' || c == '\'';
  }

  /**
   * Gets the reader's position, actually just a counter of how many characters
   * have been read
   *
   * @return Read character count
   */
  public int getPosition() {
    return position;
  }

  /**
   * Gets the last {@link #CONTEXT_LENGTH} characters to provide some context
   * for error messages.
   *
   * @return Context
   */
  public StringBuilder getContext() {
    return context;
  }

  /**
   * Gets the currently read input.
   * <p>
   * Note: Since this implementation uses a plain java {@link Reader}, the
   * returned input will only include the parts of the underlying input that
   * have been read by this wrapper
   *
   * @return Currently read input
   */
  public String getInput() {
    return input.toString();
  }

  /**
   * Expects the specified character
   * @param c The character to expect
   * @throws IOException If an IO error occurs, or if the read character is not
   *                     the expected character
   */
  public void expect(int c) throws IOException {
    int read = read();

    if (read != c) {
      String charName = read == -1 ? "EOF" : Character.toString(read);

      throw new IOException(
          "Expected '" + Character.toString(c) + "', found '" + charName + "'"
      );
    }
  }

  /**
   * Peeks at the next character
   * <p>
   * If the underlying reader supports the {@link Reader#mark(int)} function,
   * this will use that function and reset after having read a single character.
   * Otherwise, this either returns {@link #peeked} or reads a single character
   * from the underlying reader and sets {@link #peeked} to be that value
   *
   * @return Peeked character, or -1, if EOF has been encountered
   * @throws IOException If an IO error occurs
   */
  public int peek() throws IOException {
    if (reader.markSupported()) {
      reader.mark(2);
      int readChar = reader.read();
      reader.reset();
      return readChar;
    }

    if (peeked != NO_PEEK) {
      return peeked;
    }

    return peeked = readInternal();
  }

  /**
   * Reads a single character from the underlying {@link Reader}, or returns the
   * last peeked character, if the underlying reader doesn't support the
   * {@link Reader#mark(int)} function
   *
   * @return The read character
   * @throws IOException If an IO error occurs
   */
  public int read() throws IOException {
    if (peeked != NO_PEEK) {
      int c = peeked;
      peeked = NO_PEEK;
      return c;
    }

    return readInternal();
  }

  private int readInternal() throws IOException {
    int read = reader.read();

    if (read == -1) {
      return read;
    }

    input.appendCodePoint(read);

    if (read == '\n' || read == '\r' || read == ' ') {
      return read;
    }

    position++;
    context.appendCodePoint(read);
    if (context.length() > CONTEXT_LENGTH) {
      context.delete(0, context.length() - CONTEXT_LENGTH);
    }

    return read;
  }

  /**
   * Reads a single world
   * @return Read string
   * @throws IOException If an IO error occurs
   * @see #isValidUnquotedChar(int) What determines a valid 'word' character
   */
  public String readWord() throws IOException {
    return readString(ReaderWrapper::isValidUnquotedChar);
  }

  /**
   * Reads a string until the specified {@code characterPredicate} fails a
   * potential character
   *
   * @param characterPredicate Predicate that determines how long to read for,
   *                           once this returns false, this method stops
   *                           reading and returns the string
   * @return Read string, may be empty
   * @throws IOException If an IO error occurs
   */
  public String readString(IntPredicate characterPredicate)
      throws IOException
  {
    StringBuilder builder = new StringBuilder();

    while (peek() != -1 && characterPredicate.test(peek())) {
      builder.appendCodePoint(read());
    }

    return builder.toString();
  }

  /**
   * Reads an integer from the input
   * @return Read integer
   * @throws IOException If an IO error occurs, or if the read number is not a
   *                     valid integer
   */
  public int readInt() throws IOException {
    var str = readString(ReaderWrapper::isNumericChar);

    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException exc) {
      throw new IOException(exc.getMessage(), exc);
    }
  }

  /**
   * Reads a quoted string, only '\\' sequences and quote escape sequences are
   * translated, every other escape remains as a string literal.
   * <p>
   * <b>Note</b>: This method expects the quote char to already have been read,
   * so this method doesn't check if the input begins with a quote character
   *
   * @param quoteChar Character to use as a quote
   * @return Read String
   * @throws IOException If an IO error occurs
   */
  public String readQuoted(int quoteChar) throws IOException {
    StringBuilder builder = new StringBuilder();
    int codePoint;
    boolean escaped = false;

    while (true) {
      codePoint = peek();

      if (escaped) {
        if (codePoint == quoteChar) {
          builder.appendCodePoint(quoteChar);
          read();
          escaped = false;

          continue;
        }

        builder.append("\\");
        escaped = false;
      }

      read();

      if (codePoint == quoteChar) {
        break;
      }

      if (codePoint == '\\') {
        escaped = true;
        continue;
      }

      builder.appendCodePoint(codePoint);
    }

    return builder.toString();
  }

  /**
   * Skips all white space characters.
   * @throws IOException If an IO error occurs
   * @see Character#isWhitespace(int) What qualifies as 'whitespace'
   */
  public void skipEmpty() throws IOException {
    while (peek() != -1 && Character.isWhitespace(peek())) {
      read();
    }
  }
}