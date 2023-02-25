package net.forthecrown.nbt;

import java.io.DataInput;

/**
 * Static class for accessing various {@link TagType} instances and finding
 * tag types by their ID
 * @see TagType
 * @see TypeIds For the IDs of all types
 * @see <a href="https://minecraft.fandom.com/wiki/NBT_format#Data_types">NBT Data types, wiki</a>
 */
public final class TagTypes {
  private TagTypes() {}

  private static final TagType[] TYPES;

  static {
    TYPES = new TagType[TypeIds.ID_COUNT];
    addType(endType());
    addType(byteType());
    addType(shortType());
    addType(intType());
    addType(longType());
    addType(floatType());
    addType(doubleType());
    addType(byteArrayType());
    addType(stringType());
    addType(listType());
    addType(compoundType());
    addType(intArrayType());
    addType(longArrayType());
  }

  private static void addType(TagType<?> type) {
    TYPES[type.getId()] = type;
  }

  /**
   * Gets a type with the specified {@code id}
   * @param id Type ID
   * @return Tag type with the specified {@code id}, or
   *         {@link TagType#createUnknown(byte)}, if the id is invalid
   */
  public static TagType<BinaryTag> getType(byte id) {
    return id >= 0 && id < TYPES.length
        ? TYPES[id]
        : TagType.createUnknown(id);
  }

  /**
   * Gets an array of all {@link TagType} instances.
   * <p>
   * Each {@link TagType}s {@link TagType#getId()} can be used as an index in
   * this array.
   *
   * @return Cloned array of all types
   */
  public static TagType[] getTypes() {
    return TYPES.clone();
  }

  /**
   * Gets the 'TAG_End' type
   */
  public static TagType<EndTag> endType() {
    return EndTagImpl.TYPE;
  }

  /**
   * Gets the {@link StringTag} type.
   * <p>
   * String tag's, and strings in NBT altogether use a modified UTF-8 format,
   * specified in {@link DataInput#readUTF()}
   *
   * @return String tag's type
   */
  public static TagType<StringTag> stringType() {
    return StringTagImpl.TYPE;
  }

  /**
   * Gets the {@link ByteTag} type
   * <p>
   * Byte tags are primitive numeric tags, which only read/write a single byte
   *
   * @return Byte tag's type
   */
  public static TagType<ByteTag> byteType() {
    return ByteTagImpl.TYPE;
  }

  /**
   * Gets the {@link ShortTag} type
   * <p>
   * Short tags are primitive numeric tags, which read/write 2 bytes
   *
   * @return Short tag's type
   */
  public static TagType<ShortTag> shortType() {
    return ShortTagImpl.TYPE;
  }

  /**
   * Gets the {@link IntTag} type
   * <p>
   * Int tags are primitive numeric tags, which read/write 4 bytes
   *
   * @return Integer tag's type
   */
  public static TagType<IntTag> intType() {
    return IntTagImpl.TYPE;
  }

  /**
   * Gets the {@link LongTag} type
   * <p>
   * Long tags are primitive numeric tags, which read/write 8 bytes
   *
   * @return Long tag's type
   */
  public static TagType<LongTag> longType() {
    return LongTagImpl.TYPE;
  }

  /**
   * Gets the {@link FloatTag} type
   * <p>
   * Float tags are primitive numeric tags, which read/write 4 bytes
   *
   * @return Float tag's type
   */
  public static TagType<FloatTag> floatType() {
    return FloatTagImpl.TYPE;
  }

  /**
   * Gets the {@link DoubleTag} type
   * <p>
   * Double tags are primitive numeric tags, which read/write 4 bytes
   *
   * @return Double tag's type
   */
  public static TagType<DoubleTag> doubleType() {
    return DoubleTagImpl.TYPE;
  }

  /**
   * Gets the {@link CompoundTag} type
   * <p>
   * A compound tag is a list of named tags suffixed with a {@link TypeIds#END}.
   * This allows a list of named tags to read until a {@code 0} type ID
   * terminator is encountered.
   *
   * @return Compound tag's type
   */
  public static TagType<CompoundTag> compoundType() {
    return CompoundTagImpl.TYPE;
  }

  /**
   * Gets the {@link ByteArrayTag} type
   * <p>
   * The first 4 bytes of a byte array are the array size, and then the
   * following {@code length} bytes are the contents of the array
   *
   * @return Byte array tag's type
   */
  public static TagType<ByteArrayTag> byteArrayType() {
    return ByteArrayTagImpl.TYPE;
  }

  /**
   * Gets the {@link IntArrayTag} type
   * <p>
   * The first 4 bytes of an integer array are the array size, and then the
   * following {@code length * 4} bytes are the contents of the array
   *
   * @return Int array tag's type
   */
  public static TagType<IntArrayTag> intArrayType() {
    return IntArrayTagImpl.TYPE;
  }

  /**
   * Gets the {@link LongArrayTag} type
   * <p>
   * The first 4 bytes of a long array are the array size, and then the
   * following {@code length * 8} bytes are the contents of the array
   *
   * @return Long array tag's type
   */
  public static TagType<LongArrayTag> longArrayType() {
    return LongArrayTagImpl.TYPE;
  }

  /**
   * Gets the {@link ListTag} type
   * <p>
   * The first byte of the NBT list is the ID of the type of content the list
   * holds. The following 4 bytes are the list's size. The data after those
   * bytes are determined by the content of the list.
   *
   * @return List tag's type
   */
  public static TagType<ListTag> listType() {
    return ListTagImpl.TYPE;
  }
}