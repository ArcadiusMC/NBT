package net.forthecrown.nbt.path;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.util.ReaderWrapper;
import org.jetbrains.annotations.Nullable;

class ObjectNode extends FilterableNode {

  private final String name;

  public ObjectNode(String name, @Nullable Predicate<BinaryTag> filter) {
    super(filter);
    this.name = Objects.requireNonNull(name);
  }

  public String getName() {
    return name;
  }

  @Override
  public void get(BinaryTag tag,
                  List<BinaryTag> results,
                  @Nullable Supplier<BinaryTag> supplier
  ) {
    if (!(tag instanceof CompoundTag c)) {
      return;
    }

    var found = c.get(name);

    if (found == null) {
      if (supplier == null) {
        return;
      }

      var res = supplier.get();
      c.put(name, res);
      results.add(res);
    } else if (test(found)) {
      results.add(found);
    }
  }

  @Override
  public int remove(BinaryTag tag) {
    if (!(tag instanceof CompoundTag c)) {
      return 0;
    }

    var found = c.get(name);

    if (found == null || !test(found)) {
      return 0;
    }

    c.remove(name);
    return 1;
  }

  @Override
  public int set(BinaryTag tag, Supplier<BinaryTag> supplier) {
    if (!(tag instanceof CompoundTag c)) {
      return 0;
    }

    var found = c.get(name);
    if (!test(found)) {
      return 0;
    }

    var newTag = supplier.get();
    if (Objects.equals(newTag, found)) {
      return 0;
    }

    c.put(name, newTag);
    return 1;
  }

  @Override
  public BinaryTag createParent() {
    return BinaryTags.compoundTag();
  }

  @Override
  public String toString() {
    return "Object("
        + "name='" + name
        + "', filtered=" + isFiltered()
        + ")";
  }

  @Override
  public void appendInput(boolean firstNode, StringBuilder builder) {
    if (!firstNode) {
      builder.append(".");
    }

    if (ReaderWrapper.isValidWord(name)) {
      builder.append(name);
    } else {
      builder.append('"')
          .append(name)
          .append('"');
    }

    appendFilter(builder);
  }
}