package net.forthecrown.nbt;

/**
 * 64-bit integer tag
 */
public interface LongTag extends NumberTag {

  @Override
  long longValue();

  @Override
  default byte byteValue() {
    return (byte) longValue();
  }

  @Override
  default short shortValue() {
    return (short) longValue();
  }

  @Override
  default int intValue() {
    return (int) longValue();
  }

  @Override
  default float floatValue() {
    return longValue();
  }

  @Override
  default double doubleValue() {
    return longValue();
  }

  @Override
  LongTag copy();

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitLong(this);
  }
}