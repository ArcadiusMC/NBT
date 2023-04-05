package net.forthecrown.nbt;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A map of {@link String} to {@link BinaryTag} values.
 * <p>
 * Order of mappings is not guaranteed
 */
public interface CompoundTag extends TagStructure, Map<String, BinaryTag> {

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitCompound(this);
  }

  /* ---------------------------- PUT METHODS ----------------------------- */

  /**
   * Places the specified string mapping into this compound.
   * @param name name of the mapping
   * @param value value to place into the compound
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putString(String name, String value) {
    return put(name, BinaryTags.stringTag(value));
  }

  /**
   * Places the specified byte mapping into this compound.
   * <p>
   * Only the lower 8 bits of the <code>value</code> parameter are placed into
   * the compound, the rest are ignored.
   *
   * @param name name of the mapping
   * @param value value to place into the compound
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putByte(String name, int value) {
    byte byteValue = (byte) (value & 0xFF);
    return put(name, BinaryTags.byteTag(byteValue));
  }

  /**
   * Places the specified boolean mapping into this compound.
   * <p>
   * Delegate for {@link #putByte(String, int)} where <code>true</code> maps to
   * <code>1</code> and <code>false</code> to <code>0</code>
   * @param name name of the mapping
   * @param value value to place into the mapping
   *
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putBoolean(String name, boolean value) {
    return putByte(name, value ? 1 : 0);
  }

  /**
   * Places the specified short mapping into this compound.
   * <p>
   * Only the lower 16 bits of the <code>value</code> parameter are placed into
   * the compound, the rest are ignored.
   *
   * @param name name of the mapping
   * @param value value to place into the compound
   *
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putShort(String name, int value) {
    short shortValue = (short) (value & 0xFFFF);
    return put(name, BinaryTags.shortTag(shortValue));
  }

  /**
   * Places the specified <code>int</code> mapping into this compound
   * @param name name of the mapping
   * @param value value to place into the compound
   *
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putInt(String name, int value) {
    return put(name, BinaryTags.intTag(value));
  }

  /**
   * Places the specified <code>long</code> mapping into this compound
   * @param name name of the mapping
   * @param value value to place into the compound
   *
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putLong(String name, long value) {
    return put(name, BinaryTags.longTag(value));
  }

  /**
   * Places the specified <code>float</code> mapping into this compound
   * @param name name of the mapping
   * @param value value to place into the compound
   *
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putFloat(String name, float value) {
    return put(name, BinaryTags.floatTag(value));
  }

  /**
   * Places the specified <code>double</code> mapping into this compound
   * @param name name of the mapping
   * @param value value to place into the compound
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putDouble(String name, double value) {
    return put(name, BinaryTags.doubleTag(value));
  }

  /**
   * Places the specified byte array mapping into this compound
   * @param name name of the mapping
   * @param values mapping value
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putByteArray(String name, byte... values) {
    Objects.requireNonNull(values);
    return put(name, BinaryTags.byteArrayTag(values));
  }

  /**
   * Places the specified byte array mapping into this compound
   * @param name name of the mapping
   * @param values mapping value
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putByteArray(String name, Collection<Byte> values) {
    Objects.requireNonNull(values);
    return put(name, BinaryTags.byteArrayTag(values));
  }

  /**
   * Places the specified int array mapping into this compound
   * @param name name of the mapping
   * @param values mapping value
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putIntArray(String name, int... values) {
    Objects.requireNonNull(values);
    return put(name, BinaryTags.intArrayTag(values));
  }

  /**
   * Places the specified int array mapping into this compound
   * @param name name of the mapping
   * @param values mapping value
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putIntArray(String name, Collection<Integer> values) {
    Objects.requireNonNull(values);
    return put(name, BinaryTags.intArrayTag(values));
  }

  /**
   * Places the specified long array mapping into this compound
   * @param name name of the mapping
   * @param values mapping value
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putLongArray(String name, long... values) {
    Objects.requireNonNull(values);
    return put(name, BinaryTags.longArrayTag(values));
  }

  /**
   * Places the specified long array mapping into this compound
   * @param name name of the mapping
   * @param values mapping value
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putLongArray(String name, Collection<Long> values) {
    Objects.requireNonNull(values);
    return put(name, BinaryTags.longArrayTag(values));
  }

  /**
   * Places the specified UUID mapping into this compound
   * @param name name of the mapping
   * @param uuid mapping value
   * @return the previously mapped value associated with {@code name}, or
   *         {@code null}, if no preexisting value existed
   */
  default BinaryTag putUUID(String name, UUID uuid) {
    Objects.requireNonNull(uuid);
    return put(name, BinaryTags.saveUuid(uuid));
  }

  /**
   * Places all mappings from the {@code source} compound into this compound
   * and return self.
   *
   * @param source Source to get mappings from
   * @return {@code this}
   */
  CompoundTag merge(CompoundTag source);

  /**
   * Produces a copy of this compound tag.
   * <p>
   * All tags contained in this compound are copied over to the resulting that
   * with {@link BinaryTag#copy()}
   *
   * @return Deep copy of this compound tag
   */
  @Override
  CompoundTag copy();

  /* ---------------------------- GET METHODS ----------------------------- */

  /**
   * Retrieves the value associated with the {@code name} mapping. If the value
   * doesn't exist, or its type doesn't match the specified {@code type} then
   * null is returned.
   *
   * @param name name of the mapping
   * @param type mapping's expected type
   * @return the mapped value, or {@code null} if not present, or its type
   *         doesn't match the {@code type} parameter
   */
  <T extends BinaryTag> @Nullable T get(String name, TagType<T> type);

  /**
   * Delegate for {@link #get(String, TagType)} which returns an {@link Optional}
   *
   * @param name name of the mapping
   * @param type mapping's expected type
   * @return An optional containing the mapped value, or an empty optional, if
   *         no matching mapping existed
   * @see #get(String, TagType)
   */
  default <T extends BinaryTag> Optional<T> getOptional(
      String name,
      TagType<T> type
  ) {
    return Optional.ofNullable(get(name, type));
  }

  /**
   * Tests if this map contains a value associated with {@code name} and that
   * the value's type matches the specified {@code type}
   * @param name name of the mapping
   * @param type mapping's expected type
   * @return {@code true}, if a mapping for {@code name} exists, and it's type
   *         matches the {@code type}. {@code false} otherwise.
   */
  default boolean contains(String name, TagType<?> type) {
    return get(name, type) != null;
  }

  /**
   * Tests if this map contains a value associated with {@code name}
   * @param name name of the mapping
   * @return {@code true} if {@code name} is mapped to a value, {@code false}
   *         otherwise
   */
  default boolean contains(String name) {
    return containsKey(name);
  }

  /**
   * Retrieves the {@link String} mapping at the specified {@code name}
   * @param name name of the mapping
   * @return String mapped to the {@code name}, or an empty string, if no string
   *         was mapped to {@code name}
   */
  default String getString(String name) {
    return getString(name, "");
  }

  /**
   * Retrieves the {@link String} mapping at the specified {@code name}
   * @param name name of the mapping
   * @param def Default return value, if no valid mapping is found
   * @return String mapped to the {@code name}, or {@code def} string, if no
   *         string was mapped to {@code name}
   */
  default String getString(String name, String def) {
    return getOptional(name, TagTypes.stringType())
        .map(StringTag::value)
        .orElse(def );
  }

  default boolean getBoolean(String name) {
    return getBoolean(name, false);
  }

  default boolean getBoolean(String name, boolean def) {
    return getByte(name, def ? 1 : 0) != 0;
  }

  /**
   * Retrieves the {@code byte} mapping at the specified {@code name}
   * <p>
   * Delegate for {@link #getByte(String, int)}
   * @see #getByte(String, int)
   */
  default byte getByte(String name) {
    return getByte(name, 0);
  }

  /**
   * Retrieves the {@code byte} mapping at the specified {@code name}
   * <p>
   * If no {@code byte} mapping is located at {@code name}, then {@code def}
   * is returned instead.
   * @param name name of the mapping
   * @param def default value to return, if no {@code byte} mapping is found
   *        at {@code name}
   * @return The gotten {@code byte} or {@code def}
   */
  default byte getByte(String name, int def) {
    return getOptional(name, TagTypes.byteType())
        .map(ByteTag::byteValue)
        .orElse((byte) def);
  }

  /**
   * Retrieves the {@code short} mapping at the specified {@code name}
   * <p>
   * Delegate for {@link #getShort(String, int)}
   * @see #getShort(String, int)
   */
  default short getShort(String name) {
    return getShort(name, 0);
  }

  /**
   * Retrieves the {@code short} mapping at the specified {@code name}
   * <p>
   * If no {@code short} mapping is located at {@code name}, then {@code def}
   * is returned instead.
   * @param name name of the mapping
   * @param def default value to return, if no {@code short} mapping is found
   *        at {@code name}
   * @return The gotten {@code short} or {@code def}
   */
  default short getShort(String name, int def) {
    return getOptional(name, TagTypes.shortType())
        .map(ShortTag::shortValue)
        .orElse((short) def);
  }

  /**
   * Retrieves the {@code int} mapping at the specified {@code name}
   * <p>
   * Delegate for {@link #getInt(String, int)}
   * @see #getInt(String, int)
   */
  default int getInt(String name) {
    return getInt(name, (int) 0);
  }

  /**
   * Retrieves the {@code int} mapping at the specified {@code name}
   * <p>
   * If no {@code int} mapping is located at {@code name}, then {@code def}
   * is returned instead.
   * @param name name of the mapping
   * @param def default value to return, if no {@code int} mapping is found
   *        at {@code name}
   * @return The gotten {@code int} or {@code def}
   */
  default int getInt(String name, int def) {
    return getOptional(name, TagTypes.intType())
        .map(IntTag::intValue)
        .orElse(def);
  }

  /**
   * Retrieves the {@code long} mapping at the specified {@code name}
   * <p>
   * Delegate for {@link #getLong(String, long)}
   * @see #getLong(String, long)
   */
  default long getLong(String name) {
    return getLong(name, (long) 0);
  }

  /**
   * Retrieves the {@code long} mapping at the specified {@code name}
   * <p>
   * If no {@code long} mapping is located at {@code name}, then {@code def}
   * is returned instead.
   * @param name name of the mapping
   * @param def default value to return, if no {@code long} mapping is found
   *        at {@code name}
   * @return The gotten {@code long} or {@code def}
   */
  default long getLong(String name, long def) {
    return getOptional(name, TagTypes.longType())
        .map(LongTag::longValue)
        .orElse(def);
  }

  /**
   * Retrieves the {@code float} mapping at the specified {@code name}
   * <p>
   * Delegate for {@link #getFloat(String, float)}
   * @see #getFloat(String, float)
   */
  default float getFloat(String name) {
    return getFloat(name, (float) 0);
  }

  /**
   * Retrieves the {@code float} mapping at the specified {@code name}
   * <p>
   * If no {@code float} mapping is located at {@code name}, then {@code def}
   * is returned instead.
   * @param name name of the mapping
   * @param def default value to return, if no {@code float} mapping is found
   *        at {@code name}
   * @return The gotten {@code float} or {@code def}
   */
  default float getFloat(String name, float def) {
    return getOptional(name, TagTypes.floatType())
        .map(FloatTag::floatValue)
        .orElse(def);
  }

  /**
   * Retrieves the {@code double} mapping at the specified {@code name}
   * <p>
   * Delegate for {@link #getDouble(String, double)}
   * @see #getDouble(String, double)
   */
  default double getDouble(String name) {
    return getDouble(name, (double) 0);
  }

  /**
   * Retrieves the {@code double} mapping at the specified {@code name}
   * <p>
   * If no {@code double} mapping is located at {@code name}, then {@code def}
   * is returned instead.
   * @param name name of the mapping
   * @param def default value to return, if no {@code double} mapping is found
   *        at {@code name}
   * @return The gotten {@code double} or {@code def}
   */
  default double getDouble(String name, double def) {
    return getOptional(name, TagTypes.doubleType())
        .map(DoubleTag::doubleValue)
        .orElse(def);
  }

  /**
   * Retrieves a byte array mapping at the specified {@code name}
   * @param name name of the mapping
   * @return The gotten array, or a zero-length array, if none found
   */
  default byte[] getByteArray(String name) {
    return getOptional(name, TagTypes.byteArrayType())
        .map(ByteArrayTag::toByteArray)
        .orElseGet(() -> new byte[0]);
  }

  /**
   * Retrieves an integer array mapping at the specified {@code name}
   * @param name name of the mapping
   * @return The gotten array, or a zero-length array, if none found
   */
  default int[] getIntArray(String name) {
    return getOptional(name, TagTypes.intArrayType())
        .map(IntArrayTag::toIntArray)
        .orElseGet(() -> new int[0]);
  }

  /**
   * Retrieves a long array mapping at the specified {@code name}
   * @param name name of the mapping
   * @return The gotten array, or a zero-length array, if none found
   */
  default long[] getLongArray(String name) {
    return getOptional(name, TagTypes.longArrayType())
        .map(LongArrayTag::toLongArray)
        .orElseGet(() -> new long[0]);
  }

  /**
   * Retrieves a compound mapping at the specified {@code name}
   * @param name name of the mapping
   * @return Found compound, or an empty compound, if no valid mapping was found
   */
  default @NotNull CompoundTag getCompound(String name) {
    return getOptional(name, TagTypes.compoundType())
        .orElseGet(BinaryTags::compoundTag);
  }

  /**
   * Retrieves a UUID mapping at the specified name
   * @param name name of the mapping
   * @return Found UUID, or {@code null}, if no UUID was mapped to {@code name}
   */
  default UUID getUUID(String name) {
    return getOptional(name, TagTypes.intArrayType())
        .map(BinaryTags::loadUuid)
        .orElse(null);
  }

  /**
   * Retrieves a list tag mapping at the specified name
   * @param name name of the mapping
   * @return Found list, or an empty list, if no mapping was found
   */
  default ListTag getList(String name) {
    return getOptional(name, TagTypes.listType())
        .orElseGet(BinaryTags::listTag);
  }

  /**
   * Retrieves a list tag mapping at the specified name and checks if the found
   * list has the expected type.
   *
   * @param name name of the mapping
   * @param listType list's expected type
   * @return Found list, or an empty list if no mapping was found, or if the
   *         found list's type did not match the specified type
   */
  default ListTag getList(String name, TagType<?> listType) {
    var list = getList(name);

    if (list.isEmpty()) {
      return list;
    }

    if (list.listType().getId() != listType.getId()) {
      return BinaryTags.listTag();
    }

    return list;
  }
}