package net.forthecrown.nbt;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class IntArrayTagImpl extends IntArrayList implements IntArrayTag {

  public static final TagType<IntArrayTag> TYPE = new TagType<>() {
    @Override
    public void write(IntArrayTag tag, DataOutput output) throws IOException {
      output.writeInt(tag.size());
      for (int i: tag) {
        output.writeInt(i);
      }
    }

    @Override
    public IntArrayTag read(ScopedDataInput input) throws IOException {
      int size = input.readInt();
      int[] arr = new int[size];

      for (int i = 0; i < size; i++) {
        arr[i] = input.readInt();
      }

      return new IntArrayTagImpl(arr);
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      int size = input.readInt();
      input.skipBytes(size * Integer.BYTES);
    }

    @Override
    public byte getId() {
      return TypeIds.INT_ARRAY;
    }

    @Override
    public String getName() {
      return "TAG_Int_Array";
    }
  };

  public IntArrayTagImpl() {
  }

  public IntArrayTagImpl(int[] a) {
    super(a);
  }

  public IntArrayTagImpl(int capacity) {
    super(capacity);
  }

  public IntArrayTagImpl(Collection<? extends Integer> c) {
    super(c);
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public void forEachTag(@NotNull Consumer<BinaryTag> consumer) {
    forEach(integer -> consumer.accept(BinaryTags.intTag(integer)));
  }

  @Override
  public boolean addTag(BinaryTag tag) {
    if (!(tag instanceof IntTag i)) {
      return false;
    }

    return add(i.intValue());
  }

  @Override
  public void removeTag(int index) {
    removeInt(index);
  }

  @Override
  public BinaryTag getTag(int index) {
    return BinaryTags.intTag(getInt(index));
  }

  @Override
  public boolean setTag(int index, BinaryTag newTag) {
    if (!(newTag instanceof IntTag i)) {
      return false;
    }

    set(index, i.intValue());
    return true;
  }

  @Override
  public int removeMatchingTags(Predicate<BinaryTag> filter) {
    int removed = 0;
    var it = iterator();

    while (it.hasNext()) {
      if (!filter.test(BinaryTags.intTag(it.nextInt()))) {
        continue;
      }

      it.remove();
      removed++;
    }

    return removed;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof IntArrayTag)) {
      return false;
    }

    return super.equals(o);
  }

  @Override
  public String toString() {
    return toNbtString();
  }
}