package net.forthecrown.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class LongTagImpl implements LongTag {

  public static final TagType<LongTag> TYPE = new TagType<>() {
    @Override
    public void write(LongTag tag, DataOutput output) throws IOException {
      output.writeLong(tag.longValue());
    }

    @Override
    public LongTag read(ScopedDataInput input) throws IOException {
      return new LongTagImpl(input.readLong());
    }

    @Override
    public byte getId() {
      return TypeIds.LONG;
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      input.skipBytes(Long.BYTES);
    }

    @Override
    public String getName() {
      return "TAG_Long";
    }
  };

  private final long value;

  public LongTagImpl(long value) {
    this.value = value;
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public long longValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LongTag longTag)) {
      return false;
    }
    return value == longTag.longValue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "LongTag(" + value + ")";
  }
}