package net.forthecrown.nbt;

import it.unimi.dsi.fastutil.bytes.ByteList;

/**
 * Byte array tag
 */
public interface ByteArrayTag extends CollectionTag, ByteList {

  @Override
  ByteArrayTag copy();

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitByteArray(this);
  }
}