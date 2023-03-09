package net.forthecrown.nbt.io;

import static net.forthecrown.nbt.TypeIds.COMPOUND;
import static net.forthecrown.nbt.TypeIds.END;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.TagType;
import net.forthecrown.nbt.TagTypes;

final class TagIoImpl implements TagIo {
  static final TagIoImpl INSTANCE = new TagIoImpl();

  @Override
  public void write(OutputStream output, CompoundTag tag) throws IOException {
    DataOutput dataOutput = createOutput(output);
    writeNamedTag("", tag, dataOutput);
  }

  @Override
  public CompoundTag read(InputStream input, long maxBytes) throws IOException {
    ScopedDataInput dataInput = createInput(input, maxBytes);
    byte typeId = dataInput.readByte();

    if (typeId != COMPOUND) {
      throw new IOException(
          "Expected TAG_Compound (" + COMPOUND + "), found : " + typeId
      );
    }

    return readNamedTag(typeId, dataInput).getValue().asCompound();
  }

  @Override
  public void writeNamedTag(String name, BinaryTag value, DataOutput output)
      throws IOException
  {
    if (value.getId() == END) {
      throw new IOException("Unexpected TAG_End");
    }

    output.writeByte(value.getId());
    output.writeUTF(name);

    TagType<BinaryTag> type = (TagType<BinaryTag>) value.getType();
    type.write(value, output);
  }

  @Override
  public Entry<String, BinaryTag> readNamedTag(byte typeId,
                                               ScopedDataInput input
  ) throws IOException {
    String name = input.readUTF();

    TagType<BinaryTag> type = TagTypes.getType(typeId);
    BinaryTag read = type.read(input);

    return Map.entry(name, read);
  }

  public InputStream decompress(InputStream input) throws IOException {
    return new GZIPInputStream(input);
  }

  public OutputStream compress(OutputStream output) throws IOException {
    return new GZIPOutputStream(output);
  }

  static ScopedDataInput createInput(InputStream inputStream, long maxBytes) {
    return new CountingDataInput(new DataInputStream(inputStream), maxBytes);
  }

  static DataOutput createOutput(OutputStream outputStream) {
    return new DataOutputStream(outputStream);
  }
}