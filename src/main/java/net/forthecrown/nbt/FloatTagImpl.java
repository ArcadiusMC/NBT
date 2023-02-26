package net.forthecrown.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class FloatTagImpl implements FloatTag {

  public static final TagType<FloatTag> TYPE = new TagType<>() {
    @Override
    public void write(FloatTag tag, DataOutput output) throws IOException {
      output.writeFloat(tag.floatValue());
    }

    @Override
    public FloatTag read(ScopedDataInput input) throws IOException {
      return new FloatTagImpl(input.readFloat());
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      input.skipBytes(Float.BYTES);
    }

    @Override
    public byte getId() {
      return TypeIds.FLOAT;
    }

    @Override
    public String getName() {
      return "TAG_Float";
    }
  };

  private final float value;

  public FloatTagImpl(float value) {
    this.value = value;
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public float floatValue() {
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
    if (!(o instanceof FloatTag floatTag)) {
      return false;
    }
    return Float.compare(floatTag.floatValue(), value) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "FloatTag(" + value + ")";
  }
}