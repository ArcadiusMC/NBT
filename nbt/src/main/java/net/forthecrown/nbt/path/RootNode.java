package net.forthecrown.nbt.path;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

class RootNode extends FilterableNode {

  public RootNode(@Nullable Predicate<BinaryTag> filter) {
    super(filter);
  }

  @Override
  public void get(BinaryTag tag,
                  List<BinaryTag> results,
                  @Nullable Supplier<BinaryTag> supplier
  ) {
    if (!(tag instanceof CompoundTag c) || !test(c)) {
      return;
    }

    results.add(c);
  }

  @Override
  public int remove(BinaryTag tag) {
    return 0;
  }

  @Override
  public int set(BinaryTag tag, Supplier<BinaryTag> supplier) {
    return 0;
  }

  @Override
  public String toString() {
    return "Root(filtered=" + isFiltered() + ")";
  }

  @Override
  public BinaryTag createParent() {
    return BinaryTags.compoundTag();
  }

  @Override
  public void appendInput(boolean firstNode, StringBuilder builder) {
    appendFilter(builder);
  }
}