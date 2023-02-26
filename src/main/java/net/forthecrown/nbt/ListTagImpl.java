package net.forthecrown.nbt;

import it.unimi.dsi.fastutil.objects.AbstractObjectList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

public class ListTagImpl extends AbstractObjectList<BinaryTag> implements ListTag {
  public static final TagType<ListTag> TYPE = new TagType<>() {
    @Override
    public void write(ListTag tag, DataOutput output) throws IOException {
      int size = tag.size();
      int typeId = tag.listType() == null
          ? TypeIds.END
          : tag.listType().getId();

      output.writeByte(typeId);
      output.writeInt(size);

      if (!tag.isEmpty()) {
        @SuppressWarnings("unchecked") // This will always be true
        TagType<BinaryTag> type = (TagType<BinaryTag>) tag.listType();

        for (var t: tag) {
          type.write(t, output);
        }
      }
    }

    @Override
    public ListTag read(ScopedDataInput input) throws IOException {
      byte typeId = input.readByte();
      int size = input.readInt();

      validateListStructure(typeId, size);
      ListTag listTag = new ListTagImpl();

      if (size <= 0) {
        return listTag;
      }

      input.enterScope();
      TagType<BinaryTag> type = TagTypes.getType(typeId);

      for (int i = 0; i < size; i++) {
        BinaryTag readTag = type.read(input);
        listTag.add(readTag);
      }

      input.endScope();
      return listTag;
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      byte typeId = input.readByte();
      int size = input.readInt();

      validateListStructure(typeId, size);

      TagType<?> type = TagTypes.getType(typeId);
      for (int i = 0; i < size; i++) {
        type.skip(input);
      }
    }

    @Override
    public byte getId() {
      return TypeIds.LIST;
    }

    @Override
    public String getName() {
      return "TAG_List";
    }
  };

  public static final Collector<BinaryTag, ListTag, ListTag> COLLECTOR
      = Collector.of(ListTagImpl::new, List::add, ListTag::merge);

  private final List<BinaryTag> list = new ObjectArrayList<>();

  public ListTagImpl() {
  }

  public ListTagImpl(Collection<BinaryTag> tags) {
    list.addAll(tags);
  }

  static void validateListStructure(byte typeId, int size) throws IOException {
    if (typeId == TypeIds.END && size > 0) {
      throw new IOException("ListTag is missing type");
    }
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public TagType<? extends BinaryTag> listType() {
    return isEmpty() ? null : get(0).getType();
  }

  private boolean testType(BinaryTag tag) {
    var listType = listType();
    return listType == null || listType.getId() == tag.getId();
  }

  public ListTag merge(ListTag other) {
    addAll(other);
    return this;
  }

  @Override
  public BinaryTag get(int index) {
    return list.get(index);
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public void add(int index, BinaryTag tag) {
    Objects.requireNonNull(tag);
    if (!testType(tag)) {
      return;
    }

    list.add(index, tag);
  }

  @Override
  public boolean add(BinaryTag tag) {
    Objects.requireNonNull(tag);

    if (!testType(tag)) {
      return false;
    }

    return list.add(tag);
  }

  @Override
  public BinaryTag remove(int i) {
    return list.remove(i);
  }

  @Override
  public BinaryTag set(int index, BinaryTag tag) {
    Objects.requireNonNull(tag);

    if (testType(tag)) {
      return null;
    }

    return list.set(index, tag);
  }

  @Override
  public void forEachTag(@NotNull Consumer<BinaryTag> consumer) {
    list.forEach(consumer);
  }

  @Override
  public boolean addTag(BinaryTag tag) {
    return add(tag);
  }

  @Override
  public void removeTag(int index) {
    remove(index);
  }

  @Override
  public BinaryTag getTag(int index) {
    return get(index);
  }

  @Override
  public boolean setTag(int index, BinaryTag newTag) {
    if (!testType(newTag)) {
      return false;
    }

    set(index, newTag);
    return true;
  }

  @Override
  public int removeMatchingTags(Predicate<BinaryTag> filter) {
    var it = iterator();
    int removed = 0;

    while (it.hasNext()) {
      if (!filter.test(it.next())) {
        continue;
      }

      removed++;
      it.remove();
    }

    return removed;
  }

  @Override
  public ListTag copy() {
    ListTag result = new ListTagImpl();

    forEach(tag -> {
      result.add(tag.copy());
    });

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ListTagImpl that)) {
      return false;
    }
    return list.equals(that.list);
  }

  @Override
  public int hashCode() {
    return list.hashCode();
  }

  @Override
  public String toString() {
    return toNbtString();
  }
}