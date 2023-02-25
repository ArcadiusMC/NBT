package net.forthecrown.nbt;

/**
 * 64-bit floating point tag
 */
public interface DoubleTag extends NumberTag {

  @Override
  double doubleValue();

  @Override
  default byte byteValue() {
    return (byte) doubleValue();
  }

  @Override
  default short shortValue() {
    return (short) doubleValue();
  }

  @Override
  default int intValue() {
    return (int) doubleValue();
  }

  @Override
  default long longValue() {
    return (long) doubleValue();
  }

  @Override
  default float floatValue() {
    return (float) doubleValue();
  }

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitDouble(this);
  }
}