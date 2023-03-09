package net.forthecrown.nbt;

/**
 * 32-bit floating point tag
 */
public interface FloatTag extends NumberTag {

  @Override
  float floatValue();

  @Override
  default byte byteValue() {
    return (byte) floatValue();
  }

  @Override
  default short shortValue() {
    return byteValue();
  }

  @Override
  default int intValue() {
    return (int) floatValue();
  }

  @Override
  default long longValue() {
    return (long) floatValue();
  }

  @Override
  default double doubleValue() {
    return floatValue();
  }

  @Override
  FloatTag copy();

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitFloat(this);
  }
}