package net.forthecrown.nbt;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Tag composed of 32-bit integers
 */
public interface IntArrayTag extends CollectionTag, IntList {

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitIntArray(this);
  }
}