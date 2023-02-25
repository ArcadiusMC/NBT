package net.forthecrown.nbt;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class LongArrayTagImpl extends LongArrayList implements LongArrayTag {

  public static final TagType<LongArrayTag> TYPE = new TagType<>() {
    @Override
    public void write(LongArrayTag tag, DataOutput output) throws IOException {
      output.writeInt(tag.size());
      for (long l: tag) {
        output.writeLong(l);
      }
    }

    @Override
    public LongArrayTag read(ScopedDataInput input) throws IOException {
      int size = input.readInt();
      long[] arr = new long[size];

      for (int i = 0; i < size; i++) {
        arr[i] = input.readLong();
      }

      return new LongArrayTagImpl(arr);
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      int size = input.readInt();
      input.skipBytes(size * Long.BYTES);
    }

    @Override
    public byte getId() {
      return TypeIds.LONG_ARRAY;
    }

    @Override
    public String getName() {
      return "TAG_Long_Array";
    }
  };

  public LongArrayTagImpl() {
  }

  public LongArrayTagImpl(long[] a) {
    super(a);
  }

  public LongArrayTagImpl(int capacity) {
    super(capacity);
  }

  public LongArrayTagImpl(Collection<? extends Long> c) {
    super(c);
  }

  @Override
  public LongStream longStream() {
    return super.longStream();
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public void forEachTag(@NotNull Consumer<BinaryTag> consumer) {
    forEach(aLong -> consumer.accept(BinaryTags.longTag(aLong)));
  }

  @Override
  public boolean addTag(BinaryTag tag) {
    if (!(tag instanceof LongTag l)) {
      return false;
    }

    return add(l.longValue());
  }

  @Override
  public void removeTag(int index) {
    removeLong(index);
  }

  @Override
  public BinaryTag getTag(int index) {
    long l = removeLong(index);
    return BinaryTags.longTag(l);
  }

  @Override
  public boolean setTag(int index, BinaryTag newTag) {
    if (!(newTag instanceof LongTag l)) {
      return false;
    }

    set(index, l.longValue());
    return true;
  }

  @Override
  public int removeMatchingTags(Predicate<BinaryTag> filter) {
    int removed = 0;
    var it = iterator();

    while (it.hasNext()) {
      if (!filter.test(BinaryTags.longTag(it.nextLong()))) {
        continue;
      }

      it.remove();
      removed++;
    }

    return removed;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof LongArrayTag tag)) {
      return false;
    }

    return super.equals(tag);
  }

  @Override
  public String toString() {
    return toNbtString();
  }
}