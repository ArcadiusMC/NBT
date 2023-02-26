package net.forthecrown.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class ByteTagImpl implements ByteTag {
  public static final TagType<ByteTag> TYPE = new TagType<>() {
    @Override
    public void write(ByteTag tag, DataOutput output) throws IOException {
      output.writeByte(tag.byteValue());
    }

    @Override
    public ByteTag read(ScopedDataInput input) throws IOException {
      return new ByteTagImpl(input.readByte());
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      input.skipBytes(Byte.BYTES);
    }

    @Override
    public byte getId() {
      return TypeIds.BYTE;
    }

    @Override
    public String getName() {
      return "TAG_Byte";
    }
  };

  private final byte value;

  public ByteTagImpl(byte value) {
    this.value = value;
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public byte byteValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ByteTag byteTag)) {
      return false;
    }
    return value == byteTag.byteValue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "ByteTag(" + value + ")";
  }

  @Override
  public ByteTag copy() {
    return this;
  }
}