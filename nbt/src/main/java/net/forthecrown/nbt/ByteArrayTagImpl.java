package net.forthecrown.nbt;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class ByteArrayTagImpl
    extends ByteArrayList
    implements ByteArrayTag
{
  public static final TagType<ByteArrayTag> TYPE = new TagType<>() {
    @Override
    public void write(ByteArrayTag tag, DataOutput output) throws IOException {
      output.writeInt(tag.size());

      for (var b: tag) {
        output.writeByte(b);
      }
    }

    @Override
    public ByteArrayTag read(ScopedDataInput input) throws IOException {
      int length = input.readInt();
      byte[] arr = new byte[length];

      for (int i = 0; i < length; i++) {
        arr[i] = input.readByte();
      }

      return new ByteArrayTagImpl(arr);
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      int size = input.readInt();
      input.skipBytes(size);
    }

    @Override
    public byte getId() {
      return TypeIds.BYTE_ARRAY;
    }

    @Override
    public String getName() {
      return "TAG_Byte_Array";
    }
  };

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  public ByteArrayTagImpl() {
  }

  public ByteArrayTagImpl(byte[] a) {
    super(a);
  }

  public ByteArrayTagImpl(Collection<? extends Byte> c) {
    super(c);
  }

  @Override
  public void forEachTag(@NotNull Consumer<BinaryTag> consumer) {
    forEach(aByte -> consumer.accept(BinaryTags.byteTag(aByte)));
  }

  @Override
  public boolean addTag(BinaryTag tag) {
    if (!(tag instanceof ByteTag b)) {
      return false;
    }

    return add(b.byteValue());
  }

  @Override
  public void removeTag(int index) {
    removeByte(index);
  }

  @Override
  public BinaryTag getTag(int index) {
    byte b = getByte(index);
    return BinaryTags.byteTag(b);
  }

  @Override
  public boolean setTag(int index, BinaryTag tag) {
    if (!(tag instanceof ByteTag b)) {
      return false;
    }

    set(index, b.byteValue());
    return true;
  }

  @Override
  public int removeMatchingTags(Predicate<BinaryTag> filter) {
    int result = 0;
    var it = iterator();

    while (it.hasNext()) {
      if (!filter.test(BinaryTags.byteTag(it.nextByte()))) {
        continue;
      }

      it.remove();
      result++;
    }

    return result;
  }

  @Override
  public ByteArrayTagImpl copy() {
    return new ByteArrayTagImpl(toByteArray());
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ByteArrayTag)) {
      return false;
    }

    return super.equals(o);
  }

  @Override
  public String toString() {
    return toNbtString();
  }
}