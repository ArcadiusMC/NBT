package net.forthecrown.nbt.path;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.CollectionTag;

class MatchAllNode extends FilterableNode {

  public MatchAllNode(Predicate<BinaryTag> filter) {
    super(filter);
  }

  @Override
  public void get(BinaryTag tag, List<BinaryTag> results) {
    if (!(tag instanceof CollectionTag c)) {
      return;
    }

    c.forEachTag(tag1 -> {
      if (!test(tag1)) {
        return;
      }

      results.add(tag1);
    });
  }

  @Override
  public int remove(BinaryTag tag) {
    if (!(tag instanceof CollectionTag c)) {
      return 0;
    }

    return c.removeMatchingTags(this);
  }

  @Override
  public int set(BinaryTag tag, Supplier<BinaryTag> supplier) {
    if (!(tag instanceof CollectionTag c)) {
      return 0;
    }

    var newTag = supplier.get();
    int changed = 0;

    for (int i = 0; i < c.size(); i++) {
      BinaryTag existing = c.getTag(i);

      if (!Objects.equals(newTag, existing) || !test(existing)) {
        continue;
      }

      c.setTag(i, supplier.get());
      changed++;
    }

    return changed;
  }

  @Override
  public String toString() {
    return "MatchAll(filtered=" + isFiltered() + ")";
  }

  @Override
  public void appendInput(boolean firstNode, StringBuilder builder) {
    if (isFiltered()) {
      builder.append("[");
      appendFilter(builder);
      builder.append("]");

      return;
    }

    builder.append("[]");
  }
}