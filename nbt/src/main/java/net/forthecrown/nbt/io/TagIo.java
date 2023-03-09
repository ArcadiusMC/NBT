package net.forthecrown.nbt.io;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.CompoundTag;

public interface TagIo {

  static TagIo tagIo() {
    return TagIoImpl.INSTANCE;
  }

  void write(OutputStream output, CompoundTag tag)
      throws IOException;

  default void writeCompressed(OutputStream stream, CompoundTag tag)
      throws IOException
  {
    var out = compress(stream);
    write(out, tag);
    out.close();
  }

  CompoundTag read(InputStream input, long maxBytes)
      throws IOException;

  default CompoundTag read(InputStream inputStream) throws IOException {
    return read(inputStream, 0);
  }

  default CompoundTag readCompressed(InputStream input, long maxBytes)
      throws IOException
  {
    return read(decompress(input), maxBytes);
  }

  default CompoundTag readCompressed(InputStream stream) throws IOException {
    return readCompressed(stream, 0);
  }

  default void writeNamedTag(Entry<String, BinaryTag> namedTag,
                             DataOutput output
  ) throws IOException {
    writeNamedTag(namedTag.getKey(), namedTag.getValue(), output);
  }

  void writeNamedTag(String name, BinaryTag tag, DataOutput output)
      throws IOException;

  Entry<String, BinaryTag> readNamedTag(byte typeId, ScopedDataInput input)
      throws IOException;

  InputStream decompress(InputStream input) throws IOException;

  OutputStream compress(OutputStream out) throws IOException;
}