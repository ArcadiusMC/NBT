package net.forthecrown.nbt.string;

import java.io.IOException;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.ByteArrayTag;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.IntArrayTag;
import net.forthecrown.nbt.ListTag;
import net.forthecrown.nbt.LongArrayTag;
import net.forthecrown.nbt.NumberTag;
import net.forthecrown.nbt.StringTag;
import net.forthecrown.nbt.string.TagToken.Type;
import net.forthecrown.nbt.util.ReaderWrapper;

class SnbtParser {
  private final TagTokenizer tokenizer;

  public SnbtParser(ReaderWrapper reader) {
    this.tokenizer = new TagTokenizer(reader);
  }

  public void fillContext(TagParseException exc) {
    var reader = tokenizer.getReader();
    exc.setContext(reader.getContext().toString());
    exc.setParseOffset(reader.getPosition());
  }

  public BinaryTag parse() {
    return doSafe(this::parseValue);
  }

  public CompoundTag parseCompound() {
    return doSafe(this::parseCompoundInternal);
  }

  private <T> T doSafe(IoSupplier<T> supplier) {
    try {
      return supplier.get();
    } catch (IOException exc) {
      var err = new TagParseException(exc.getMessage());
      fillContext(err);
      throw err;
    } catch (TagParseException exc) {
      fillContext(exc);
      throw exc;
    }
  }

  public BinaryTag parseValue() throws IOException {
    TagToken peek = tokenizer.peekToken();
    Type type = peek.type();

    if (type == Type.COMMA
        || type == Type.ASSIGNMENT
        || type == Type.ARRAY_CLOSE
        || type == Type.COMPOUND_CLOSE
        || type == Type.EOF
    ) {
      throw new TagParseException("Unexpected token: " + type);
    }

    return switch (type) {
      case COMPOUND_OPEN -> parseCompoundInternal();
      case ARRAY_OPEN -> parseList();

      case BYTE_ARRAY_OPEN -> parseByteArray();
      case INT_ARRAY_OPEN -> parseIntArray();
      case LONG_ARRAY_OPEN -> parseLongArray();

      case STRING -> parseString();

      default -> parseNumber();
    };
  }

  public NumberTag parseNumber() throws IOException {
    var token = tokenizer.nextToken();

    if (!token.type().isNumber()) {
      throw new TagParseException(
          "Expected numeric token, found: " + token.type()
      );
    }

    var n = token.number();

    return switch (token.type()) {
      case BYTE   -> BinaryTags.byteTag(n.byteValue());
      case SHORT  -> BinaryTags.shortTag(n.shortValue());
      case INT    -> BinaryTags.intTag(n.intValue());
      case LONG   -> BinaryTags.longTag(n.longValue());
      case FLOAT  -> BinaryTags.floatTag(n.floatValue());
      case DOUBLE -> BinaryTags.doubleTag(n.doubleValue());
      default -> throw new IllegalStateException();
    };
  }

  private CompoundTag parseCompoundInternal() throws IOException {
    CompoundTag tag = BinaryTags.compoundTag();

    parseListBased(Type.COMPOUND_OPEN, Type.COMPOUND_CLOSE, () -> {
      var nameToken = tokenizer.expect(Type.STRING);
      tokenizer.expect(Type.ASSIGNMENT);

      BinaryTag parsed = parseValue();
      tag.put(nameToken.value(), parsed);
    });

    return tag;
  }

  public ListTag parseList() throws IOException {
    ListTag list = BinaryTags.listTag();

    parseListBased(Type.ARRAY_OPEN, Type.ARRAY_CLOSE, () -> {
      var value = parseValue();

      if (!list.add(value)) {
        throw new TagParseException(
            "Unexpected tag '"
                + value.getType() + "' in '"
                + list.listType().getName() + "' list"
        );
      }
    });

    return list;
  }

  public ByteArrayTag parseByteArray() throws IOException {
    ByteArrayTag arr = BinaryTags.byteArrayTag();

    parseListBased(Type.BYTE_ARRAY_OPEN, Type.ARRAY_CLOSE, () -> {
      var byteValue = tokenizer.expect(Type.BYTE).number().byteValue();
      arr.add(byteValue);
    });

    return arr;
  }

  public IntArrayTag parseIntArray() throws IOException {
    IntArrayTag arr = BinaryTags.intArrayTag();

    parseListBased(Type.INT_ARRAY_OPEN, Type.ARRAY_CLOSE, () -> {
      int intValue = tokenizer.expect(Type.INT).number().intValue();
      arr.add(intValue);
    });

    return arr;
  }

  public LongArrayTag parseLongArray() throws IOException {
    LongArrayTag arr = BinaryTags.longArrayTag();

    parseListBased(Type.LONG_ARRAY_OPEN, Type.ARRAY_CLOSE, () -> {
      long longValue = tokenizer.expect(Type.LONG).number().longValue();
      arr.add(longValue);
    });

    return arr;
  }

  public StringTag parseString() throws IOException {
    var token = tokenizer.expect(Type.STRING);
    return BinaryTags.stringTag(token.value());
  }

  private void parseListBased(Type start, Type end, ListEntryParser parser)
      throws IOException
  {
    tokenizer.expect(start);

    while (true) {
      parser.parseEntry();

      var peek = tokenizer.peekToken();

      if (peek.type() == Type.COMMA) {
        tokenizer.nextToken();
      } else if (peek.type() == end) {
        break;
      } else {
        throw new TagParseException(
            "Unexpected token " + peek.type() + ", expected "
                + Type.COMMA + " or " + end
        );
      }
    }

    tokenizer.expect(end);
  }

  interface ListEntryParser {
    void parseEntry() throws IOException;
  }

  interface IoSupplier<T> {
    T get() throws IOException;
  }
}