package net.forthecrown.nbt.string;

import java.io.Reader;
import java.io.StringReader;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.util.ReaderWrapper;

/**
 * SNBT, or Stringified NBT, is a format used to represent NBT data in a string
 * form, this class provides functions that allow tag to string conversion, and
 * vice versa.
 *
 */
public final class Snbt {
  private Snbt() {}

  /* --------------------------- TO NBT STRING ---------------------------- */

  /**
   * Formats the specified {@code tag} to an SNBT string
   * @param tag Tag to format
   * @return SNBT-formatted result
   */
  public static String toString(BinaryTag tag) {
    return toString(tag, false, false);
  }

  /**
   * Formats the specified {@code tag} to an SNBT string
   *
   * @param tag            Tag to format
   * @param prettyPrinting {@code true} to allow for newline characters,
   *                       indentation, {@code false} to format to a flat
   *                       string
   *
   * @return SNBT-formatted result
   */
  public static String toString(BinaryTag tag, boolean prettyPrinting) {
    return toString(tag, prettyPrinting, false);
  }

  /**
   * Formats the specified {@code tag} to an SNBT string
   * @param tag Tag to format
   * @param prettyPrinting {@code true} to allow for newline characters,
   *                       indentation, {@code false} to format to a flat
   *                       string
   * @param collapsePrimitiveArrays {@code true}, to always keep primitive
   *                                arrays as single line even when
   *                                {@code prettyPrinting} is enabled,
   *                                {@code false} otherwise
   * @return SNBT-formatted result
   */
  public static String toString(BinaryTag tag,
                                boolean prettyPrinting,
                                boolean collapsePrimitiveArrays
  ) {
    SnbtVisitor visitor = new SnbtVisitor();
    visitor.setPrettyPrinting(prettyPrinting);
    visitor.setCollapsePrimitiveLists(collapsePrimitiveArrays);

    tag.visit(visitor);
    return visitor.toString();
  }

  /* ------------------------------ PARSING ------------------------------- */

  /**
   * Parses the specified {@code input} into a tag.
   * <p>
   * This method only parses a tag at the beginning of the string.
   * <p>
   * Calls {@link #parse(Reader)} by wrapping the string in a
   * {@link StringReader}
   *
   * @param input Input to parse
   * @return Parsed tag
   * @throws TagParseException If the tag fails to parse
   * @see #parse(Reader)
   * @see #parse(ReaderWrapper)
   */
  public static BinaryTag parse(String input) throws TagParseException {
    return parse(new ReaderWrapper(input));
  }

  /**
   * Parses the specified {@code reader}'s input into a tag.
   * <p>
   * This method only parses a tag at the beginning of the reader's input.
   * <p>
   * Calls {@link #parse(ReaderWrapper)} by wrapping the specified reader
   *
   * @param reader Input reader
   * @return Parsed tag
   * @throws TagParseException If the tag fails to parse
   * @see #parse(ReaderWrapper)
   */
  public static BinaryTag parse(Reader reader) throws TagParseException {
    return parse(new ReaderWrapper(reader));
  }

  /**
   * Parses the specified {@code reader}'s input into a tag.
   * <p>
   * This method only parses a tag at the beginning of the reader's input
   *
   * @param reader Input reader
   * @return Parsed tag
   * @throws TagParseException If the tag fails to parse
   */
  public static BinaryTag parse(ReaderWrapper reader) throws TagParseException {
    return new SnbtParser(reader).parse();
  }

  public static CompoundTag parseCompound(String input)
      throws TagParseException
  {
    return parseCompound(new ReaderWrapper(input));
  }

  public static CompoundTag parseCompound(Reader reader)
      throws TagParseException
  {
    return parseCompound(new ReaderWrapper(reader));
  }

  public static CompoundTag parseCompound(ReaderWrapper reader)
      throws TagParseException
  {
    return new SnbtParser(reader).parseCompound();
  }
}