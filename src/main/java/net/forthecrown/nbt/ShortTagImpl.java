package net.forthecrown.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class ShortTagImpl implements ShortTag {
  public static final TagType<ShortTag> TYPE = new TagType<>() {
    @Override
    public void write(ShortTag tag, DataOutput output) throws IOException {
      output.writeShort(tag.shortValue());
    }

    @Override
    public ShortTag read(ScopedDataInput input) throws IOException {
      return new ShortTagImpl(input.readShort());
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      input.skipBytes(Byte.BYTES);
    }

    @Override
    public byte getId() {
      return TypeIds.SHORT;
    }

    @Override
    public String getName() {
      return "TAG_Short";
    }
  };

  private final short value;

  public ShortTagImpl(short value) {
    this.value = value;
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public short shortValue() {
    return value;
  }

  @Override
  public BinaryTag copy() {
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ShortTag shortTag)) {
      return false;
    }
    return value == shortTag.shortValue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "ShortTag(" + value + ")";
  }
}