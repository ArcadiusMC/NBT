package net.forthecrown.nbt;

/**
 * Unused ending tag.
 * <p>
 * <b>Note</b>: Should <b>NOT</b> be used alongside regular tags
 */
public interface EndTag extends BinaryTag {
  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitEnd(this);
  }
}