package net.forthecrown.nbt.string;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;
import net.forthecrown.nbt.string.TagToken.Type;
import net.forthecrown.nbt.util.ReaderWrapper;
import org.intellij.lang.annotations.Language;

class TagTokenizer {

  @Language("RegExp")
  static final String NUMERIC_PATTERN = "[+-]?[0-9]+";

  static final Pattern BYTE_PATTERN
      = Pattern.compile(NUMERIC_PATTERN + "[bB]");

  static final Pattern SHORT_PATTERN
      = Pattern.compile(NUMERIC_PATTERN + "[sS]");

  static final Pattern INT_PATTERN
      = Pattern.compile(NUMERIC_PATTERN);

  static final Pattern LONG_PATTERN
      = Pattern.compile(NUMERIC_PATTERN + "+[lL]");

  static final Pattern FLOAT_PATTERN
      = Pattern.compile("[+-]?([0-9]+)?(\\.([0-9]+([eE][+-]?[0-9]+)?)?)?[fF]");

  static final Pattern DOUBLE_PATTERN
      = Pattern.compile("[+-]?([0-9]+)?(\\.([0-9]+([eE][+-]?[0-9]+)?)?)?[dD]?");

  private TagToken peekedToken;
  private final ReaderWrapper reader;

  public TagTokenizer(ReaderWrapper reader) {
    this.reader = Objects.requireNonNull(reader);
  }

  public ReaderWrapper getReader() {
    return reader;
  }

  public static boolean isByteSuffix(int c) {
    return c == 'B' || c == 'b';
  }

  public static boolean isShortSuffix(int c) {
    return c == 'S' || c == 's';
  }

  public static boolean isIntSuffix(int c) {
    return c == 'i' || c == 'I';
  }

  public static boolean isLongSuffix(int c) {
    return c == 'L' || c == 'l';
  }

  public static boolean isFloatSuffix(int c) {
    return c == 'F' || c == 'f';
  }

  public static boolean isDoubleSuffix(int c) {
    return c == 'D' || c == 'd';
  }

  public TagToken expect(Type expected) throws IOException, TagParseException {
    var token = nextToken();

    if (token.type() != expected) {
      throw new TagParseException(
          "Expected " + expected + ", found " + token.type()
      );
    }

    return token;
  }

  public TagToken peekToken() throws IOException {
    if (peekedToken != null) {
      return peekedToken;
    }

    return peekedToken = readToken();
  }

  public TagToken nextToken() throws IOException {
    if (peekedToken != null) {
      var peek = peekedToken;
      peekedToken = null;
      return peek;
    }

    return readToken();
  }

  private TagToken readToken() throws IOException {
    reader.skipEmpty();
    int read = reader.read();

    if (read == -1) {
      return Type.EOF.token();
    }

    if (ReaderWrapper.isQuote(read)) {
      String s = reader.readQuoted(read);
      return Type.STRING.token(s);
    }

    switch (read) {
      case Tokens.COMPOUND_START:
        return Type.COMPOUND_OPEN.token();

      case Tokens.COMPOUND_END:
        return Type.COMPOUND_CLOSE.token();

      case Tokens.ASSIGNMENT:
        return Type.ASSIGNMENT.token();

      case Tokens.COMMA:
        return Type.COMMA.token();

      case Tokens.ARRAY_END:
        return Type.ARRAY_CLOSE.token();

      default:
    }

    if (read == '[') {
      int peek = reader.peek();

      if (isByteSuffix(peek)) {
        reader.read();
        reader.expect(';');
        return Type.BYTE_ARRAY_OPEN.token();
      }

      if (isIntSuffix(peek)) {
        reader.read();
        reader.expect(';');
        return Type.INT_ARRAY_OPEN.token();
      }

      if (isLongSuffix(peek)) {
        reader.read();
        reader.expect(';');
        return Type.LONG_ARRAY_OPEN.token();
      }

      return Type.ARRAY_OPEN.token();
    }

    return readWordOrNumber(read);
  }

  private TagToken readWordOrNumber(int startChar) throws IOException {
    String str = Character.toString(startChar) + reader.readWord();

    if ("true".equalsIgnoreCase(str)) {
      return Type.BYTE.token(1);
    }

    if ("false".equalsIgnoreCase(str)) {
      return Type.BYTE.token(0);
    }

    try {
      var token = readNumeric(str);

      if (token != null) {
        return token;
      }
    } catch (NumberFormatException ignored) {
      // Exception being thrown means it's not a number
    }

    return Type.STRING.token(str);
  }

  private TagToken readNumeric(String str) {
    String trimmed = str.substring(0, str.length()-1);

    if (BYTE_PATTERN.matcher(str).matches()) {
      return Type.BYTE.token(Byte.parseByte(trimmed));
    }

    if (SHORT_PATTERN.matcher(str).matches()) {
      return Type.SHORT.token(Short.parseShort(trimmed));
    }

    if (INT_PATTERN.matcher(str).matches()) {
      return Type.INT.token(Integer.parseInt(str));
    }

    if (LONG_PATTERN.matcher(str).matches()) {
      return Type.LONG.token(Long.parseLong(trimmed));
    }

    if (FLOAT_PATTERN.matcher(str).matches()) {
      return Type.FLOAT.token(Float.parseFloat(trimmed));
    }

    if (DOUBLE_PATTERN.matcher(str).matches()) {
      if (str.endsWith("d") || str.endsWith("D")) {
        return Type.DOUBLE.token(Double.parseDouble(trimmed));
      }

      return Type.DOUBLE.token(Double.parseDouble(str));
    }

    return null;
  }
}