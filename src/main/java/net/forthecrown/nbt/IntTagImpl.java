package net.forthecrown.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class IntTagImpl implements IntTag {
  public static final TagType<IntTag> TYPE = new TagType<>() {
    @Override
    public void write(IntTag tag, DataOutput output) throws IOException {
      output.writeInt(tag.intValue());
    }

    @Override
    public IntTag read(ScopedDataInput input) throws IOException {
      return new IntTagImpl(input.readInt());
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      input.skipBytes(Integer.BYTES);
    }

    @Override
    public byte getId() {
      return TypeIds.INT;
    }

    @Override
    public String getName() {
      return "TAG_Int";
    }
  };

  private int value;

  public IntTagImpl(int value) {
    this.value = value;
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public int intValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof IntTag intTag)) {
      return false;
    }
    return value == intTag.intValue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "IntTag(" + value + ")";
  }
}