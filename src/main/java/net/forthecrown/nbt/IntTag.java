package net.forthecrown.nbt;

/**
 * 32-bit integer tag
 */
public interface IntTag extends NumberTag {

  @Override
  int intValue();

  @Override
  default byte byteValue() {
    return (byte) intValue();
  }

  @Override
  default short shortValue() {
    return (short) intValue();
  }

  @Override
  default long longValue() {
    return intValue();
  }

  @Override
  default float floatValue() {
    return intValue();
  }

  @Override
  default double doubleValue() {
    return intValue();
  }

  @Override
  IntTag copy();

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitInt(this);
  }
}