package net.forthecrown.nbt.path;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.forthecrown.nbt.BinaryTag;

/**
 * A single node within a path
 */
interface Node {
  void get(BinaryTag tag, List<BinaryTag> results);

  int remove(BinaryTag tag);

  int set(BinaryTag tag, Supplier<BinaryTag> supplier);

  default List<BinaryTag> get(List<BinaryTag> tags) {
    List<BinaryTag> result = new ArrayList<>();

    for (var t: tags) {
      get(t, result);
    }

    return result;
  }

  void appendInput(boolean firstNode, StringBuilder builder);

  default int remove(List<BinaryTag> tags) {
    return apply(tags, this::remove);
  }

  default int set(List<BinaryTag> tags, Supplier<BinaryTag> supplier) {
    return apply(tags, (tag) -> this.set(tag, supplier));
  }

  default int apply(List<BinaryTag> list, ToIntFunction<BinaryTag> op) {
    return list.stream()
        .mapToInt(op)
        .sum();
  }
}