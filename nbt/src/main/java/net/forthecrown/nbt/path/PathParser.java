package net.forthecrown.nbt.path;

import java.io.IOException;
import java.util.function.Predicate;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.path.TagPathImpl.BuilderImpl;
import net.forthecrown.nbt.string.Snbt;
import net.forthecrown.nbt.util.ReaderWrapper;
import net.forthecrown.nbt.util.TagPredicate;

class PathParser {
  static final int FILTER_START = '{';
  static final int FILTER_END   = '}';
  static final int INDEX_START  = '[';
  static final int INDEX_END    = ']';
  static final int DELIMITER    = '.';

  private final ReaderWrapper reader;
  private final BuilderImpl builder = new BuilderImpl();

  public PathParser(ReaderWrapper reader) {
    this.reader = reader;
  }

  public static boolean isValidPathChar(int c) {
    return !Character.isWhitespace(c)
        && c != '"'
        && c != '\''
        && c != INDEX_START
        && c != INDEX_END
        && c != FILTER_START
        && c != FILTER_END
        && c != DELIMITER;
  }

  public ReaderWrapper getReader() {
    return reader;
  }

  public TagPath parse() {
    try {
      runParse();
    } catch (IOException exc) {
      var newException = new PathParseException(exc);
      fillContext(newException);

      throw newException;
    } catch (PathParseException exc) {
      fillContext(exc);
      throw exc;
    }

    return builder.build();
  }

  private void fillContext(PathParseException exc) {
    exc.setContext(reader.getContext().toString());
    exc.setPosition(reader.getPosition());
  }

  private void runParse() throws IOException {
    int peek = reader.peek();

    if (peek == FILTER_START) {
      var predicate = parseFilter();
      builder.setRootFilter(predicate);

      peek = reader.peek();

      if (peek != DELIMITER) {
        return;
      }

      reader.expect(DELIMITER);
    }

    while (true) {
      var node = parseNode();
      builder.addNode(node);

      if (reader.peek() == DELIMITER) {
        reader.read();
      } else if (reader.peek() != INDEX_START) {
        // Calling expect() when the char is there, will consume the character,
        // meaning we read more input than we're supposed to
        if (reader.peek() != -1 && reader.peek() != ' ') {
          reader.expect(' ');
        }

        break;
      }
    }
  }

  private Node parseNode() throws IOException {
    int peek = reader.peek();

    if (peek == INDEX_START) {
      return parseArrayAccess();
    }

    if (!ReaderWrapper.isValidUnquotedChar(peek)
        && !ReaderWrapper.isQuote(peek)
    ) {
      throw new PathParseException(
          "Unexpected token '"
              + (peek == -1 ? "EOF" : Character.toString(peek))
              + "'"
      );
    }

    String name;

    if (ReaderWrapper.isQuote(peek)) {
      reader.read();
      name = reader.readQuoted(peek);
    } else {
      name = reader.readString(PathParser::isValidPathChar);

      if (name.isEmpty()) {
        throw new PathParseException("Empty key");
      }
    }

    peek = reader.peek();

    if (peek == FILTER_START) {
      return new ObjectNode(name, parseFilter());
    } else {
      return new ObjectNode(name, null);
    }
  }

  private Node parseArrayAccess() throws IOException {
    reader.expect(INDEX_START);
    int peek = reader.peek();

    Node result;

    if (peek == INDEX_END) {
      result = new MatchAllNode(null);
    } else if (peek == FILTER_START) {
      var predicate = parseFilter();
      result = new MatchAllNode(predicate);
    } else {
      int index = reader.readInt();
      result = new IndexNode(index);
    }

    reader.expect(INDEX_END);
    return result;
  }

  private Predicate<BinaryTag> parseFilter() {
    BinaryTag tag = Snbt.parse(reader);
    return new TagPredicate(tag);
  }
}