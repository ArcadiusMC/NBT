package net.forthecrown.nbt;

/**
 * Visitor applied to a {@link BinaryTag} to navigate a tag structure
 * @see BinaryTag#visit(BinaryTagVisitor)
 */
public interface BinaryTagVisitor {

  /**
   * Visits a {@code String} tag
   * @param tag tag being visited
   */
  void visitString(StringTag tag);

  /**
   * Visits an end tag
   * @param tag tag being visited
   */
  default void visitEnd(EndTag tag) {
    // Noop
  }

  /**
   * Visits a {@code byte} tag
   * @param tag tag being visited
   */
  void visitByte(ByteTag tag);

  /**
   * Visits a {@code short} tag
   * @param tag tag being visited
   */
  void visitShort(ShortTag tag);

  /**
   * Visits an {@code int} tag
   * @param tag tag being visited
   */
  void visitInt(IntTag tag);

  /**
   * Visits a {@code long} tag
   * @param tag tag being visited
   */
  void visitLong(LongTag tag);

  /**
   * Visits a {@code float} tag
   * @param tag tag being visited
   */
  void visitFloat(FloatTag tag);

  /**
   * Visits a {@code double} tag
   * @param tag tag being visited
   */
  void visitDouble(DoubleTag tag);

  /**
   * Visits a {@link ByteArrayTag} tag
   * @param tag tag being visited
   */
  void visitByteArray(ByteArrayTag tag);

  /**
   * Visits a {@link IntArrayTag} tag
   * @param tag tag being visited
   */
  void visitIntArray(IntArrayTag tag);

  /**
   * Visits a {@link LongArrayTag} tag
   * @param tag tag being visited
   */
  void visitLongArray(LongArrayTag tag);

  /**
   * Visits a {@link ListTag} tag
   * @param tag tag being visited
   */
  void visitList(ListTag tag);

  /**
   * Visits a {@link CompoundTag} tag
   * @param tag tag being visited
   */
  void visitCompound(CompoundTag tag);
}