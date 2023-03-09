package net.forthecrown.nbt.path;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CollectionTag;
import org.jetbrains.annotations.Nullable;

class IndexNode implements Node {

  private final int index;

  public IndexNode(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  @Override
  public void get(BinaryTag tag,
                  List<BinaryTag> results,
                  @Nullable Supplier<BinaryTag> supplier
  ) {
    if (!(tag instanceof CollectionTag c)
        || index >= c.size()
    ) {
      return;
    }

    int index = this.index < 0
        ? c.size() + this.index
        : this.index;

    BinaryTag element = c.getTag(index);
    results.add(element);
  }

  @Override
  public int remove(BinaryTag tag) {
    if (!(tag instanceof CollectionTag c)) {
      return 0;
    }

    int index = this.index < 0
        ? c.size() + this.index
        : this.index;

    if (index >= c.size()) {
      return 0;
    }

    c.removeTag(index);
    return 1;
  }

  @Override
  public int set(BinaryTag tag, Supplier<BinaryTag> supplier) {
    if (!(tag instanceof CollectionTag c)) {
      return 0;
    }

    int index = this.index < 0
        ? c.size() + this.index
        : this.index;

    BinaryTag existing = c.getTag(index);
    BinaryTag newTag = supplier.get();

    if (Objects.equals(existing, newTag)) {
      return 0;
    }

    return c.setTag(index, newTag) ? 1 : 0;
  }

  @Override
  public BinaryTag createParent() {
    return BinaryTags.listTag();
  }

  @Override
  public String toString() {
    return "Index(" + index + ")";
  }

  @Override
  public void appendInput(boolean firstNode, StringBuilder builder) {
    builder.append("[")
        .append(index)
        .append("]");
  }
}