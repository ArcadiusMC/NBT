package net.forthecrown.nbt;

/**
 * Type ID constants
 * @see TagType
 * @see TagTypes
 */
public final class TypeIds {
  private TypeIds() {}

  public static final byte END = 0;

  public static final byte BYTE = 1;

  public static final byte SHORT = 2;

  public static final byte INT = 3;

  public static final byte LONG = 4;

  public static final byte FLOAT = 5;

  public static final byte DOUBLE = 6;

  public static final byte BYTE_ARRAY = 7;

  public static final byte STRING = 8;

  public static final byte LIST = 9;

  public static final byte COMPOUND = 10;

  public static final byte INT_ARRAY = 11;

  public static final byte LONG_ARRAY = 12;

  /** The last ID, the ID of the {@link #LONG_ARRAY} */
  public static final byte LAST_ID = LONG_ARRAY;

  /** Number of tag IDs */
  public static final byte ID_COUNT = LAST_ID + 1;
}