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

  @Override
  ListTag copy();

  @Override
  default void visit(BinaryTagVisitor visitor) {
    visitor.visitList(this);
  }
}