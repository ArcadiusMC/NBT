package net.forthecrown.nbt;

/**
 * 16-bit integer tag
 */
public interface ShortTag extends NumberTag {

  @Override
  short shortValue();

  @Override
  default byte byteValue() {
    return (byte) shortValue();
  }

  @Override
  default int intValue() {
    return shortValue();
  }

  @Override
  default long longValue() {
    return shortValue();
  }

  @Override
  default float floatValue() {
    return shortValue();
  }

  @Override
  default double doubleValue() {
    return shortValue();
  }

  @Override
  ShortTag copy();

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitShort(this);
  }
}