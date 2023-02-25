package net.forthecrown.nbt.string;

import static net.forthecrown.nbt.string.Tokens.ARRAY_END;
import static net.forthecrown.nbt.string.Tokens.ARRAY_START;
import static net.forthecrown.nbt.string.Tokens.ASSIGNMENT;
import static net.forthecrown.nbt.string.Tokens.COMMA;
import static net.forthecrown.nbt.string.Tokens.COMPOUND_END;
import static net.forthecrown.nbt.string.Tokens.COMPOUND_START;
import static net.forthecrown.nbt.string.Tokens.STR_QUOTE;
import static net.forthecrown.nbt.string.Tokens.SUFFIX_BYTE;
import static net.forthecrown.nbt.string.Tokens.SUFFIX_DOUBLE;
import static net.forthecrown.nbt.string.Tokens.SUFFIX_FLOAT;
import static net.forthecrown.nbt.string.Tokens.SUFFIX_INT;
import static net.forthecrown.nbt.string.Tokens.SUFFIX_LONG;
import static net.forthecrown.nbt.string.Tokens.SUFFIX_SHORT;

import java.util.List;
import net.forthecrown.nbt.BinaryTagVisitor;
import net.forthecrown.nbt.ByteArrayTag;
import net.forthecrown.nbt.ByteTag;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.DoubleTag;
import net.forthecrown.nbt.FloatTag;
import net.forthecrown.nbt.IntArrayTag;
import net.forthecrown.nbt.IntTag;
import net.forthecrown.nbt.ListTag;
import net.forthecrown.nbt.LongArrayTag;
import net.forthecrown.nbt.LongTag;
import net.forthecrown.nbt.ShortTag;
import net.forthecrown.nbt.StringTag;
import net.forthecrown.nbt.util.ReaderWrapper;

class SnbtVisitor implements BinaryTagVisitor {
  public static final int INDENT_CHANGE = 2;

  private final StringBuilder builder;
  private boolean prettyPrinting = false;
  private boolean collapsePrimitiveLists = false;

  private int indent = 0;

  public SnbtVisitor(StringBuilder builder) {
    this.builder = builder;
  }

  public SnbtVisitor() {
    this(new StringBuilder());
  }

  public boolean getPrettyPrinting() {
    return prettyPrinting;
  }

  public void setPrettyPrinting(boolean prettyPrinting) {
    this.prettyPrinting = prettyPrinting;
  }

  public boolean getCollapsePrimitiveLists() {
    return collapsePrimitiveLists;
  }

  public void setCollapsePrimitiveLists(boolean collapsePrimitiveLists) {
    this.collapsePrimitiveLists = collapsePrimitiveLists;
  }

  @Override
  public void visitString(StringTag tag) {
    builder.append(STR_QUOTE)
        .append(tag)
        .append(STR_QUOTE);
  }

  @Override
  public void visitByte(ByteTag tag) {
    builder.append(tag.byteValue()).append(SUFFIX_BYTE);
  }

  @Override
  public void visitShort(ShortTag tag) {
    builder.append(tag.shortValue()).append(SUFFIX_SHORT);
  }

  @Override
  public void visitInt(IntTag tag) {
    builder.append(tag.intValue());
  }

  @Override
  public void visitLong(LongTag tag) {
    builder.append(tag.longValue()).append(SUFFIX_LONG);
  }

  @Override
  public void visitFloat(FloatTag tag) {
    builder.append(tag.floatValue()).append(SUFFIX_FLOAT);
  }

  @Override
  public void visitDouble(DoubleTag tag) {
    builder.append(tag.doubleValue()).append(SUFFIX_DOUBLE);
  }

  @Override
  public void visitByteArray(ByteArrayTag tag) {
    appendPrimitiveList(tag, SUFFIX_BYTE);
  }

  @Override
  public void visitIntArray(IntArrayTag tag) {
    appendPrimitiveList(tag, SUFFIX_INT);
  }

  @Override
  public void visitLongArray(LongArrayTag tag) {
    appendPrimitiveList(tag, SUFFIX_LONG);
  }

  private void appendPrimitiveList(List<?> list, char prefix) {
    builder.append(ARRAY_START)
        .append(prefix)
        .append(';');

    var it = list.iterator();
    increaseIndent();

    while (it.hasNext()) {
      Object l = it.next();

      if (!collapsePrimitiveLists) {
        nlIndent();
      } else {
        builder.append(" ");
      }

      builder.append(l);

      if (!(list instanceof IntArrayTag)) {
        builder.append(prefix);
      }

      if (it.hasNext()) {
        builder.append(COMMA);
      }
    }

    decreaseIndent();

    if (!collapsePrimitiveLists) {
      nlIndent();
    }

    builder.append(ARRAY_END);
  }

  @Override
  public void visitList(ListTag tag) {
    builder.append(ARRAY_START);

    var it = tag.iterator();
    increaseIndent();

    while (it.hasNext()) {
      var next = it.next();

      nlIndent();
      next.visit(this);

      if (it.hasNext()) {
        builder.append(COMMA);
      }
    }

    decreaseIndent();
    nlIndent();

    builder.append(ARRAY_END);
  }

  @Override
  public void visitCompound(CompoundTag tag) {
    if (tag.isEmpty()) {
      builder.append(COMPOUND_START).append(COMPOUND_END);
      return;
    }

    builder.append(COMPOUND_START);

    var set = tag.entrySet();
    var it = set.iterator();
    increaseIndent();

    while (it.hasNext()) {
      var entry = it.next();

      nlIndent();

      String key = entry.getKey();

      if (ReaderWrapper.isValidWord(key)) {
        builder.append(key);
      } else {
        builder.append(STR_QUOTE)
            .append(key)
            .append(STR_QUOTE);
      }

      builder.append(ASSIGNMENT)
          .append(!prettyPrinting ? "" : " ");

      entry.getValue().visit(this);

      if (it.hasNext()) {
        builder.append(COMMA);
      }
    }

    decreaseIndent();
    nlIndent();

    builder.append(COMPOUND_END);
  }

  private void increaseIndent() {
    indent += INDENT_CHANGE;
  }

  private void decreaseIndent() {
    indent -= INDENT_CHANGE;
  }

  private void nlIndent() {
    if (!prettyPrinting) {
      return;
    }

    builder.append("\n")
        .append(" ".repeat(indent));
  }

  @Override
  public String toString() {
    return builder.toString();
  }
}