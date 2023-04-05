package net.forthecrown.nbt;

import net.forthecrown.nbt.string.Snbt;
import org.jetbrains.annotations.NotNull;

/**
 * A binary tag.
 * <p>
 * To instantiate and use NBT objects, see the factory and other methods in the
 * {@link BinaryTags} class
 *
 * @see BinaryTags
 */
public interface BinaryTag {

  /**
   * Gets the tag's type
   * @return The tag's type
   */
  @NotNull
  TagType<? extends BinaryTag> getType();

  /**
   * Gets this tag's type's ID
   * @return this tag's type's ID
   */
  default byte getId() {
    return getType().getId();
  }

  /**
   * Visits this tag
   * @param visitor The visitor
   */
  void visit(BinaryTagVisitor visitor);

  /**
   * Tests if this tag is a {@link CompoundTag}
   * @return {@code false}, if this is an instance of a {@link CompoundTag},
   *         {@code false}, otherwise
   */
  default boolean isCompound() {
    return this instanceof CompoundTag;
  }

  /**
   * Tests if this tag is a {@link ListTag}
   * @return {@code false}, if this is an instance of a {@link ListTag},
   *         {@code false}, otherwise
   */
  default boolean isList() {
    return this instanceof ListTag;
  }

  /**
   * Tests if this tag is a {@link StringTag}
   * @return {@code false}, if this is an instance of a {@link StringTag},
   *         {@code false}, otherwise
   */
  default boolean isString() {
    return this instanceof StringTag;
  }

  /**
   * Tests if this tag is a {@link NumberTag}
   * @return {@code false}, if this is an instance of a {@link NumberTag},
   *         {@code false}, otherwise
   */
  default boolean isNumber() {
    return this instanceof NumberTag;
  }

  /**
   * Casts this tag to a {@link CompoundTag}
   * @return this
   * @throws ClassCastException If this is not a {@link CompoundTag}
   */
  default CompoundTag asCompound() throws ClassCastException {
    return (CompoundTag) this;
  }

  /**
   * Casts this tag to a {@link ListTag}
   * @return this
   * @throws ClassCastException If this is not a {@link ListTag}
   */
  default ListTag asList() throws ClassCastException {
    return (ListTag) this;
  }

  /**
   * Casts this tag to a {@link StringTag}
   * @return this
   * @throws ClassCastException If this is not a {@link StringTag}
   */
  default StringTag asString() throws ClassCastException {
    return (StringTag) this;
  }

  /**
   * Casts this tag to a {@link NumberTag}
   * @return this
   * @throws ClassCastException If this is not a {@link NumberTag}
   */
  default NumberTag asNumber() throws ClassCastException {
    return (NumberTag) this;
  }

  /**
   * Converts this tag into an SNBT representation
   * @return SNBT representation of this tag
   * @see Snbt#toString(BinaryTag)
   */
  default String toNbtString() {
    return Snbt.toString(this);
  }

  /**
   * Creates a copy of this tag.
   * <p>
   * Note: Value tags, like {@link StringTag} and {@link IntTag}, will return
   * {@code this}, as value objects are immutable so copying serves no purpose.
   * <br>
   * Non-value types like {@link CompoundTag} and {@link ListTag} will create a
   * deep copy of the tag by copying over each element contained in them by
   * calling method.
   *
   * @return Copy of this tag
   */
  BinaryTag copy();
}