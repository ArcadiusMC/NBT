package net.forthecrown.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.IntStream;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class StringTagImpl implements StringTag {

  public static final TagType<StringTag> TYPE = new TagType<>() {
    @Override
    public void write(StringTag tag, DataOutput output) throws IOException {
      output.writeUTF(tag.value());
    }

    @Override
    public StringTag read(ScopedDataInput input) throws IOException {
      return StringTagImpl.of(input.readUTF());
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      skipString(input);
    }

    @Override
    public byte getId() {
      return TypeIds.STRING;
    }

    @Override
    public String getName() {
      return "TAG_String";
    }
  };

  public static final StringTagImpl EMPTY = new StringTagImpl("");

  private final String value;

  public StringTagImpl(String value) {
    this.value = value;
  }

  static StringTagImpl of(String s) {
    Objects.requireNonNull(s);
    return s.isEmpty() ? EMPTY : new StringTagImpl(s);
  }

  static void skipString(DataInput input) throws IOException {
    input.skipBytes(input.readUnsignedShort());
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public String value() {
    return value;
  }

  @Override
  public int length() {
    return value.length();
  }

  @Override
  public char charAt(int index) {
    return value.charAt(index);
  }

  @NotNull
  @Override
  public CharSequence subSequence(int start, int end) {
    return value.subSequence(start, end);
  }

  @NotNull
  @Override
  public IntStream codePoints() {
    return value().codePoints();
  }

  @NotNull
  @Override
  public IntStream chars() {
    return value().chars();
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public StringTag copy() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof StringTag str)) {
      return false;
    }

    return str.value().equals(value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}