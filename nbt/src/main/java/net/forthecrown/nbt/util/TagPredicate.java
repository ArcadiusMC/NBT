package net.forthecrown.nbt.util;

import java.util.function.Predicate;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.BinaryTags;

/**
 * Implementation of {@link Predicate} that returns a Tag's SNBT string when
 * {@link #toString()} is called
 */
public record TagPredicate(BinaryTag tag) implements Predicate<BinaryTag> {

  @Override
  public boolean test(BinaryTag tag) {
    return BinaryTags.compareTags(this.tag, tag, true);
  }

  @Override
  public String toString() {
    return tag.toNbtString();
  }
}