package net.forthecrown.nbt;

public interface ByteTag extends NumberTag {

  @Override
  byte byteValue();

  @Override
  default short shortValue() {
    return byteValue();
  }

  @Override
  default int intValue() {
    return byteValue();
  }

  @Override
  default long longValue() {
    return byteValue();
  }

  @Override
  default float floatValue() {
    return byteValue();
  }

  @Override
  default double doubleValue() {
    return byteValue();
  }

  @Override
  ByteTag copy();

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitByte(this);
  }
}