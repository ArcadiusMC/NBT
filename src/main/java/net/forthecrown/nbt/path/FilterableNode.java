package net.forthecrown.nbt.path;

import java.util.function.Predicate;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.util.TagPredicate;
import org.jetbrains.annotations.Nullable;

abstract class FilterableNode implements Node, Predicate<BinaryTag> {
  private final Predicate<BinaryTag> filter;

  public FilterableNode(@Nullable Predicate<BinaryTag> filter) {
    this.filter = filter;
  }

  public Predicate<BinaryTag> getFilter() {
    return filter;
  }

  @Override
  public boolean test(BinaryTag tag) {
    return filter == null || filter.test(tag);
  }

  public boolean isFiltered() {
    return getFilter() != null;
  }

  void appendFilter(StringBuilder builder) {
    if (!(filter instanceof TagPredicate)) {
      return;
    }

    builder.append(filter);
  }
}