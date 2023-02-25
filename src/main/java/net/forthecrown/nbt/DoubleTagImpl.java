package net.forthecrown.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class DoubleTagImpl implements DoubleTag {

  public static final TagType<DoubleTag> TYPE = new TagType<DoubleTag>() {
    @Override
    public void write(DoubleTag tag, DataOutput output) throws IOException {
      output.writeDouble(tag.doubleValue());
    }

    @Override
    public DoubleTag read(ScopedDataInput input) throws IOException {
      return new DoubleTagImpl(input.readDouble());
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      input.skipBytes(Double.BYTES);
    }

    @Override
    public byte getId() {
      return TypeIds.DOUBLE;
    }

    @Override
    public String getName() {
      return "TAG_Double";
    }
  };

  private final double value;

  public DoubleTagImpl(double value) {
    this.value = value;
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public double doubleValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DoubleTag doubleTag)) {
      return false;
    }
    return Double.compare(doubleTag.doubleValue(), value) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "DoubleTag(" + value + ")";
  }
}