package net.forthecrown.nbt;

import it.unimi.dsi.fastutil.longs.LongList;

/**
 * Tag composed of an array of 64-bit integers
 */
public interface LongArrayTag extends LongList, CollectionTag {

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitLongArray(this);
  }
}