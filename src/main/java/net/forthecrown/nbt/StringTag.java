package net.forthecrown.nbt;

/**
 * Representation of a string in Binary Tag form.
 */
public interface StringTag extends BinaryTag, CharSequence {

  /**
   * Gets this tag's string value
   * @return string value
   */
  String value();

  @Override
  StringTag copy();

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitString(this);
  }

  /** Same as {@link #value()} */
  String toString();
}