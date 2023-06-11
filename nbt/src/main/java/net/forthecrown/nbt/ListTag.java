package net.forthecrown.nbt;

import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * A list based tag, only accepts 1 type of tag
 */
public interface ListTag extends CollectionTag, List<BinaryTag> {

  /**
   * Gets the tag type stored by this list.
   * <p>
   * This type is used to test any tag that's being placed into this list, if
   * it fails, it's not placed into the list
   *
   * @return List's type, or {@code null}, if the list is empty and has no type
   */
  @Nullable TagType<? extends BinaryTag> listType();

  /**
   * Delegate for {@link #addAll(Collection)} that simply returns {@code this}
   */
  ListTag merge(ListTag other);

  /**
   * Produces a copy of this tag
   * <p>
   * All tags contained in this list are copied over to the resulting list with
   * {@link BinaryTag#copy()}
   *
   * @return Copied list
   */
  @Override
  ListTag copy();

  /**
   * Tests if the specified {@code type} matches this list's type
   * @param type Type to test
   * @return {@code true}, if {@link #listType()} is {@code null} or is equal to {@code type},
   *         {@code false} otherwise
   */
  boolean typeMatches(TagType<?> type);

  /**
   * Gets an element at a specified {@code index} with a specified {@code type}
   * @param index Element index
   * @param type Desired element type
   * @return Found element type, or {@code null}, if the type didn't match
   * @throws IndexOutOfBoundsException If the specified {@code index} is out of the list's bounds
   */
  <T extends BinaryTag> T get(int index, TagType<T> type) throws IndexOutOfBoundsException;

  // Type-specific values
  //
  // Almost all of these were generated via a javascript script, because I
  // cannot be arsed to write so much repetitive code
  //

  /**
   * Adds a {@code String} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addString(String value) {
    var tag = BinaryTags.stringTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code String} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addString(int index, String value) {
    var tag = BinaryTags.stringTag(value);
    add(index, tag);
  }

  /**
   * Removes a {@code String} value
   * @param value Value to remove
   * @return {@code true}, if the specified {@code value} was removed,
   *         {@code false} otherwise
   */
  default boolean removeString(String value) {
    var tag = BinaryTags.stringTag(value);
    return remove(tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setString(int index, String value) {
    var tag = BinaryTags.stringTag(value);
    set(index, tag);
  }

  /**
   * Gets a {@code String} value from the tag
   * @param index Element index
   * @return Found value, or {@code 0}, if the entry wasn't found
   */
  default String getString(int index) {
    return getString(index, null);
  }

  /**
   * Gets a {@code String} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default String getString(int index, String defaultValue) {
    if (!typeMatches(TagTypes.stringType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    StringTag tag = get(index, TagTypes.stringType());
    return tag.value();
  }

  /**
   * Adds a {@code byte} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addByte(byte value) {
    var tag = BinaryTags.byteTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code byte} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addByte(int index, byte value) {
    var tag = BinaryTags.byteTag(value);
    add(index, tag);
  }

  /**
   * Removes a {@code byte} value
   * @param value Value to remove
   * @return {@code true}, if the specified {@code value} was removed,
   *         {@code false} otherwise
   */
  default boolean removeByte(byte value) {
    var tag = BinaryTags.byteTag(value);
    return remove(tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setByte(int index, byte value) {
    var tag = BinaryTags.byteTag(value);
    set(index, tag);
  }

  /**
   * Gets a {@code byte} value from the tag
   * @param index Element index
   * @return Found value, or {@code (byte) 0}, if the entry wasn't found
   */
  default byte getByte(int index) {
    return getByte(index, (byte) 0);
  }

  /**
   * Gets a {@code byte} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default byte getByte(int index, byte defaultValue) {
    if (!typeMatches(TagTypes.byteType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    ByteTag tag = get(index, TagTypes.byteType());
    return tag.byteValue();
  }

  /**
   * Adds a {@code short} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addShort(short value) {
    var tag = BinaryTags.shortTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code short} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addShort(int index, short value) {
    var tag = BinaryTags.shortTag(value);
    add(index, tag);
  }

  /**
   * Removes a {@code short} value
   * @param value Value to remove
   * @return {@code true}, if the specified {@code value} was removed,
   *         {@code false} otherwise
   */
  default boolean removeShort(short value) {
    var tag = BinaryTags.shortTag(value);
    return remove(tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setShort(int index, short value) {
    var tag = BinaryTags.shortTag(value);
    set(index, tag);
  }

  /**
   * Gets a {@code short} value from the tag
   * @param index Element index
   * @return Found value, or {@code (short) 0}, if the entry wasn't found
   */
  default short getShort(int index) {
    return getShort(index, (short) 0);
  }

  /**
   * Gets a {@code short} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default short getShort(int index, short defaultValue) {
    if (!typeMatches(TagTypes.shortType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    ShortTag tag = get(index, TagTypes.shortType());
    return tag.shortValue();
  }

  /**
   * Adds a {@code int} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addInt(int value) {
    var tag = BinaryTags.intTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code int} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addInt(int index, int value) {
    var tag = BinaryTags.intTag(value);
    add(index, tag);
  }

  /**
   * Removes a {@code int} value
   * @param value Value to remove
   * @return {@code true}, if the specified {@code value} was removed,
   *         {@code false} otherwise
   */
  default boolean removeInt(int value) {
    var tag = BinaryTags.intTag(value);
    return remove(tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setInt(int index, int value) {
    var tag = BinaryTags.intTag(value);
    set(index, tag);
  }

  /**
   * Gets a {@code int} value from the tag
   * @param index Element index
   * @return Found value, or {@code 0}, if the entry wasn't found
   */
  default int getInt(int index) {
    return getInt(index, 0);
  }

  /**
   * Gets a {@code int} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default int getInt(int index, int defaultValue) {
    if (!typeMatches(TagTypes.intType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    IntTag tag = get(index, TagTypes.intType());
    return tag.intValue();
  }

  /**
   * Adds a {@code long} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addLong(long value) {
    var tag = BinaryTags.longTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code long} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addLong(int index, long value) {
    var tag = BinaryTags.longTag(value);
    add(index, tag);
  }

  /**
   * Removes a {@code long} value
   * @param value Value to remove
   * @return {@code true}, if the specified {@code value} was removed,
   *         {@code false} otherwise
   */
  default boolean removeLong(long value) {
    var tag = BinaryTags.longTag(value);
    return remove(tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setLong(int index, long value) {
    var tag = BinaryTags.longTag(value);
    set(index, tag);
  }

  /**
   * Gets a {@code long} value from the tag
   * @param index Element index
   * @return Found value, or {@code 0}, if the entry wasn't found
   */
  default long getLong(int index) {
    return getLong(index, 0);
  }

  /**
   * Gets a {@code long} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default long getLong(int index, long defaultValue) {
    if (!typeMatches(TagTypes.longType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    LongTag tag = get(index, TagTypes.longType());
    return tag.longValue();
  }

  /**
   * Adds a {@code double} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addDouble(double value) {
    var tag = BinaryTags.doubleTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code double} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addDouble(int index, double value) {
    var tag = BinaryTags.doubleTag(value);
    add(index, tag);
  }

  /**
   * Removes a {@code double} value
   * @param value Value to remove
   * @return {@code true}, if the specified {@code value} was removed,
   *         {@code false} otherwise
   */
  default boolean removeDouble(double value) {
    var tag = BinaryTags.doubleTag(value);
    return remove(tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setDouble(int index, double value) {
    var tag = BinaryTags.doubleTag(value);
    set(index, tag);
  }

  /**
   * Gets a {@code double} value from the tag
   * @param index Element index
   * @return Found value, or {@code 0}, if the entry wasn't found
   */
  default double getDouble(int index) {
    return getDouble(index, 0);
  }

  /**
   * Gets a {@code double} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default double getDouble(int index, double defaultValue) {
    if (!typeMatches(TagTypes.doubleType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    DoubleTag tag = get(index, TagTypes.doubleType());
    return tag.doubleValue();
  }

  /**
   * Adds a {@code float} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addFloat(float value) {
    var tag = BinaryTags.floatTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code float} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addFloat(int index, float value) {
    var tag = BinaryTags.floatTag(value);
    add(index, tag);
  }

  /**
   * Removes a {@code float} value
   * @param value Value to remove
   * @return {@code true}, if the specified {@code value} was removed,
   *         {@code false} otherwise
   */
  default boolean removeFloat(float value) {
    var tag = BinaryTags.floatTag(value);
    return remove(tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setFloat(int index, float value) {
    var tag = BinaryTags.floatTag(value);
    set(index, tag);
  }

  /**
   * Gets a {@code float} value from the tag
   * @param index Element index
   * @return Found value, or {@code 0}, if the entry wasn't found
   */
  default float getFloat(int index) {
    return getFloat(index, 0);
  }

  /**
   * Gets a {@code float} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default float getFloat(int index, float defaultValue) {
    if (!typeMatches(TagTypes.floatType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    FloatTag tag = get(index, TagTypes.floatType());
    return tag.floatValue();
  }

  /**
   * Adds a {@code byte[]} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addByteArray(byte... value) {
    var tag = BinaryTags.byteArrayTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code byte[]} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addByteArray(int index, byte... value) {
    var tag = BinaryTags.byteArrayTag(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setByteArray(int index, byte... value) {
    var tag = BinaryTags.byteArrayTag(value);
    set(index, tag);
  }

  /**
   * Gets a {@code byte[]} value from the tag
   * @param index Element index
   * @return Found value, or {@code (byte[]) null}, if the entry wasn't found
   */
  default byte[] getByteArray(int index) {
    return getByteArray(index, (byte[]) null);
  }

  /**
   * Gets a {@code byte[]} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default byte[] getByteArray(int index, byte... defaultValue) {
    if (!typeMatches(TagTypes.byteArrayType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    ByteArrayTag tag = get(index, TagTypes.byteArrayType());
    return tag.toByteArray();
  }

  /**
   * Adds a {@code List<Byte>} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addByteArray(List<Byte> value) {
    var tag = BinaryTags.byteArrayTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code List<Byte>} value to the list
   * @param index Index to add the value at
   * @param value Valueto add
   */
  default void addByteArray(int index, List<Byte> value) {
    var tag = BinaryTags.byteArrayTag(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setByteArray(int index, List<Byte> value) {
    var tag = BinaryTags.byteArrayTag(value);
    set(index, tag);
  }

  /**
   * Adds a {@code int[]} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addIntArray(int... value) {
    var tag = BinaryTags.intArrayTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code int[]} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addIntArray(int index, int... value) {
    var tag = BinaryTags.intArrayTag(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setIntArray(int index, int... value) {
    var tag = BinaryTags.intArrayTag(value);
    set(index, tag);
  }

  /**
   * Gets a {@code int[]} value from the tag
   * @param index Element index
   * @return Found value, or {@code (int[]) null}, if the entry wasn't found
   */
  default int[] getIntArray(int index) {
    return getIntArray(index, (int[]) null);
  }

  /**
   * Gets a {@code int[]} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default int[] getIntArray(int index, int... defaultValue) {
    if (!typeMatches(TagTypes.intArrayType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    IntArrayTag tag = get(index, TagTypes.intArrayType());
    return tag.toIntArray();
  }

  /**
   * Adds a {@code List<Integer>} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addIntArray(List<Integer> value) {
    var tag = BinaryTags.intArrayTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code List<Integer>} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addIntArray(int index, List<Integer> value) {
    var tag = BinaryTags.intArrayTag(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setIntArray(int index, List<Integer> value) {
    var tag = BinaryTags.intArrayTag(value);
    set(index, tag);
  }

  /**
   * Adds a {@code long[]} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addLongArray(long... value) {
    var tag = BinaryTags.longArrayTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code long[]} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addLongArray(int index, long... value) {
    var tag = BinaryTags.longArrayTag(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setLongArray(int index, long... value) {
    var tag = BinaryTags.longArrayTag(value);
    set(index, tag);
  }

  /**
   * Gets a {@code long[]} value from the tag
   * @param index Element index
   * @return Found value, or {@code (long[]) null}, if the entry wasn't found
   */
  default long[] getLongArray(int index) {
    return getLongArray(index, (long[]) null);
  }

  /**
   * Gets a {@code long[]} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default long[] getLongArray(int index, long... defaultValue) {
    if (!typeMatches(TagTypes.longArrayType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    LongArrayTag tag = get(index, TagTypes.longArrayType());
    return tag.toLongArray();
  }

  /**
   * Adds a {@code List<Long>} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addLongArray(List<Long> value) {
    var tag = BinaryTags.longArrayTag(value);
    return add(tag);
  }

  /**
   * Adds a {@code List<Long>} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addLongArray(int index, List<Long> value) {
    var tag = BinaryTags.longArrayTag(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setLongArray(int index, List<Long> value) {
    var tag = BinaryTags.longArrayTag(value);
    set(index, tag);
  }

  /**
   * Adds a {@code double[]} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addDoubleArray(double... value) {
    var tag = BinaryTags.doubleList(value);
    return add(tag);
  }

  /**
   * Adds a {@code double[]} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addDoubleArray(int index, double... value) {
    var tag = BinaryTags.doubleList(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setDoubleArray(int index, double... value) {
    var tag = BinaryTags.doubleList(value);
    set(index, tag);
  }

  /**
   * Gets a {@code double[]} value from the tag
   * @param index Element index
   * @return Found value, or {@code (double[]) null}, if the entry wasn't found
   */
  default double[] getDoubleArray(int index) {
    return getDoubleArray(index, (double[]) null);
  }

  /**
   * Gets a {@code double[]} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default double[] getDoubleArray(int index, double... defaultValue) {
    if (!typeMatches(TagTypes.listType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    ListTag tag = get(index, TagTypes.listType());

    if (!tag.typeMatches(TagTypes.doubleType())) {
      return defaultValue;
    }

    return tag.stream()
        .mapToDouble(value -> value.asNumber().doubleValue())
        .toArray();
  }

  /**
   * Adds a {@code List<Double>} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addDoubleArray(List<Double> value) {
    var tag = BinaryTags.doubleList(value);
    return add(tag);
  }

  /**
   * Adds a {@code List<Double>} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addDoubleArray(int index, List<Double> value) {
    var tag = BinaryTags.doubleList(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setDoubleArray(int index, List<Double> value) {
    var tag = BinaryTags.doubleList(value);
    set(index, tag);
  }

  /**
   * Adds a {@code float[]} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addFloatArray(float... value) {
    var tag = BinaryTags.floatList(value);
    return add(tag);
  }

  /**
   * Adds a {@code float[]} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addFloatArray(int index, float... value) {
    var tag = BinaryTags.floatList(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setFloatArray(int index, float... value) {
    var tag = BinaryTags.floatList(value);
    set(index, tag);
  }

  /**
   * Gets a {@code float[]} value from the tag
   * @param index Element index
   * @return Found value, or {@code (float[]) null}, if the entry wasn't found
   */
  default float[] getFloatArray(int index) {
    return getFloatArray(index, (float[]) null);
  }

  /**
   * Gets a {@code float[]} value from the tag* @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default float[] getFloatArray(int index, float... defaultValue) {
    if (!typeMatches(TagTypes.listType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    ListTag tag = get(index, TagTypes.listType());

    if (!tag.typeMatches(TagTypes.floatType())) {
      return defaultValue;
    }

    float[] arr = new float[tag.size()];
    for (int i = 0; i < tag.size(); i++) {
      arr[i] = tag.get(i).asNumber().floatValue();
    }
    return arr;
  }

  /**
   * Adds a {@code List<Float>} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addFloatArray(List<Float> value) {
    var tag = BinaryTags.floatList(value);
    return add(tag);
  }

  /**
   * Adds a {@code List<Float>} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addFloatArray(int index, List<Float> value) {
    var tag = BinaryTags.floatList(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setFloatArray(int index, List<Float> value) {
    var tag = BinaryTags.floatList(value);
    set(index, tag);
  }

  /**
   * Adds a {@code String[]} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addStringArray(String... value) {
    var tag = BinaryTags.stringList(value);
    return add(tag);
  }

  /**
   * Adds a {@code String[]} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addStringArray(int index, String... value) {
    var tag = BinaryTags.stringList(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setStringArray(int index, String... value) {
    var tag = BinaryTags.stringList(value);
    set(index, tag);
  }

  /**
   * Gets a {@code String[]} value from the tag
   * @param index Element index
   * @return Found value, or {@code (String[]) null}, if the entry wasn't found
   */
  default String[] getStringArray(int index) {
    return getStringArray(index, (String[]) null);
  }

  /**
   * Gets a {@code String[]} value from the tag
   * @param index Element index
   * @param defaultValue Default return value
   * @return Found value, or {@code defaultValue}, if the entry wasn't found
   */
  default String[] getStringArray(int index, String... defaultValue) {
    if (!typeMatches(TagTypes.listType()) || index < 0 || index >= size()) {
      return defaultValue;
    }

    ListTag tag = get(index, TagTypes.listType());

    if (!tag.typeMatches(TagTypes.stringType())) {
      return defaultValue;
    }

    return tag.stream()
        .map(tag1 -> tag1.asString().value())
        .toArray(String[]::new);
  }

  /**
   * Adds a {@code List<String>} value to the list
   * @param value Value to add
   * @return {@code true}, if the value was inserted into the list, {@code false} otherwise
   */
  default boolean addStringArray(List<String> value) {
    var tag = BinaryTags.stringList(value);
    return add(tag);
  }

  /**
   * Adds a {@code List<String>} value to the list
   * @param index Index to add the value at
   * @param value Value to add
   */
  default void addStringArray(int index, List<String> value) {
    var tag = BinaryTags.stringList(value);
    add(index, tag);
  }

  /**
   * Sets a value inside this list
   * @param index Index to set
   * @param value Value to set
   */
  default void setStringArray(int index, List<String> value) {
    var tag = BinaryTags.stringList(value);
    set(index, tag);
  }

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitList(this);
  }
}