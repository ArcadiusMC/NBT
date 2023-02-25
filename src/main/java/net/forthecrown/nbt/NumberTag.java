package net.forthecrown.nbt;

public interface NumberTag extends BinaryTag {
  byte byteValue();

  default boolean booleanValue() {
    return byteValue() != 0;
  }

  short shortValue();

  int intValue();

  long longValue();

  float floatValue();

  double doubleValue();
}