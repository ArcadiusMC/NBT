package net.forthecrown.nbt.paper;

import static net.kyori.adventure.text.Component.text;

import java.util.List;
import net.forthecrown.nbt.BinaryTag;
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

class TextTagVisitor implements BinaryTagVisitor, ComponentLike {

  private static final Component STRING_QUOTE   = text("'");
  private static final Component ARRAY_START    = text("[");
  private static final Component ARRAY_END      = text("]");
  private static final Component COMPOUND_START = text("{");
  private static final Component COMPOUND_END   = text("}");
  private static final Component EMPTY_ARRAY    = text("[]");
  private static final Component EMPTY_COMPOUND = text("{}");
  private static final Component COMMA          = text(",");
  private static final Component ASSIGNMENT     = text(":");

  private static final Style STRING_STYLE = Style.style(NamedTextColor.GREEN);
  private static final Style NUMBER_STYLE = Style.style(NamedTextColor.GOLD);
  private static final Style KEY_STYLE = Style.style(NamedTextColor.AQUA);

  private static final Style PRIMITIVE_SUFFIX
      = Style.style(NamedTextColor.RED);


  /* -------------------------- INSTANCE FIELDS --------------------------- */

  private final TextComponent.Builder builder = text();
  private int indent;
  private final String indentString;
  private boolean collapsePrimitiveLists;

  public TextTagVisitor() {
    this(null);
  }

  public TextTagVisitor(String indentString) {
    this.indentString = indentString;
  }

  public boolean getCollapsePrimitiveLists() {
    return collapsePrimitiveLists;
  }

  public TextTagVisitor setCollapsePrimitiveLists(boolean collapsePrimitiveLists) {
    this.collapsePrimitiveLists = collapsePrimitiveLists;
    return this;
  }

  public Component visit(BinaryTag tag) {
    tag.visit(this);
    return asComponent();
  }

  public boolean isPrettyPrinting() {
    return indentString != null && !indentString.isEmpty();
  }

  @Override
  public @NotNull Component asComponent() {
    return builder.build();
  }

  void indent() {
    indent++;
  }

  void unIndent() {
    indent--;
  }

  void newLineIndent() {
    if (!isPrettyPrinting()) {
      return;
    }

    builder.append(text("\n" + (indentString.repeat(indent))));
  }

  @Override
  public void visitString(StringTag tag) {
    appendQuoted(tag.value(), STRING_STYLE);
  }

  private void appendQuoted(String s, Style style) {
    builder.append(STRING_QUOTE)
        .append(text(escapeString(s), style))
        .append(STRING_QUOTE);
  }

  private String escapeString(String s) {
    return s.replace("\\", "\\\\").replace("'", "\\'");
  }

  @Override
  public void visitByte(ByteTag tag) {
    appendPrimitive(tag.byteValue(), 'b');
  }

  @Override
  public void visitShort(ShortTag tag) {
    appendPrimitive(tag.shortValue(), 's');
  }

  @Override
  public void visitInt(IntTag tag) {
    appendPrimitive(tag.intValue(), null);
  }

  @Override
  public void visitLong(LongTag tag) {
    appendPrimitive(tag.longValue(), 'L');
  }

  @Override
  public void visitFloat(FloatTag tag) {
    appendPrimitive(tag.floatValue(), 'f');
  }

  @Override
  public void visitDouble(DoubleTag tag) {
    appendPrimitive(tag.doubleValue(), 'd');
  }

  private void appendPrimitive(Number number, Character suffix) {
    builder.append(text(number.toString(), NUMBER_STYLE));

    if (suffix != null) {
      builder.append(text(suffix, PRIMITIVE_SUFFIX));
    }
  }

  @Override
  public void visitByteArray(ByteArrayTag tag) {
    appendPrimitiveArray(tag, 'B', 'b');
  }

  @Override
  public void visitIntArray(IntArrayTag tag) {
    appendPrimitiveArray(tag, 'I', null);
  }

  @Override
  public void visitLongArray(LongArrayTag tag) {
    appendPrimitiveArray(tag, 'L', 'L');
  }

  private <T> void appendPrimitiveArray(List<T> list,
                                        char prefix,
                                        Character suffix
  ) {
    builder.append(ARRAY_START)
        .append(text(prefix + ";", PRIMITIVE_SUFFIX));

    var it = list.iterator();
    indent();

    while (it.hasNext()) {
      if (!collapsePrimitiveLists) {
        newLineIndent();
      } else {
        builder.appendSpace();
      }

      var n = it.next();
      String s = String.valueOf(n);
      builder.append(text(s, NUMBER_STYLE));

      if (suffix != null) {
        builder.append(text(suffix, PRIMITIVE_SUFFIX));
      }

      if (it.hasNext()) {
        builder.append(COMMA);
      }
    }

    unIndent();

    if (!collapsePrimitiveLists) {
      newLineIndent();
    }

    builder.append(ARRAY_END);
  }

  @Override
  public void visitList(ListTag tag) {
    if (tag.isEmpty()) {
      builder.append(EMPTY_ARRAY);
      return;
    }

    builder.append(ARRAY_START);
    indent();

    var it = tag.iterator();

    while (it.hasNext()) {
      newLineIndent();
      var n = it.next();
      n.visit(this);

      if (it.hasNext()) {
        builder.append(COMMA);
      }
    }

    unIndent();
    newLineIndent();
    builder.append(ARRAY_END);
  }

  @Override
  public void visitCompound(CompoundTag tag) {
    if (tag.isEmpty()) {
      builder.append(EMPTY_COMPOUND);
      return;
    }

    builder.append(COMPOUND_START);
    indent();

    var it = tag.entrySet().iterator();

    while (it.hasNext()) {
      var n = it.next();
      newLineIndent();

      var key = n.getKey();
      if (ReaderWrapper.isValidWord(key)) {
        builder.append(text(key, KEY_STYLE));
      } else {
        appendQuoted(key, KEY_STYLE);
      }

      builder.append(ASSIGNMENT).appendSpace();

      n.getValue().visit(this);

      if (it.hasNext()) {
        builder.append(COMMA);
      }
    }

    unIndent();
    newLineIndent();

    builder.append(COMPOUND_END);
  }
}