package net.forthecrown.nbt.io;

import java.io.DataInput;
import java.io.IOException;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

class CountingDataInput implements ScopedDataInput {

  private final DataInput base;
  private final long maximumBytes;
  private long readBytes;
  private int depth;

  public CountingDataInput(DataInput base, long maximumBytes) {
    this.base = Objects.requireNonNull(base);
    this.maximumBytes = maximumBytes;
  }

  @Override
  public void enterScope() throws IOException {
    depth++;

    if (depth > MAX_DEPTH) {
      throw new IOException("Max tag depth (" + MAX_DEPTH + ") surpassed");
    }
  }

  @Override
  public void endScope() {
    depth--;
  }

  @Override
  public int depth() {
    return depth;
  }

  @Override
  public long accountedBytes() {
    return readBytes;
  }

  public long maxBytes() {
    return maximumBytes;
  }

  private void accountBytes(long bytes) throws IOException {
    readBytes += bytes;

    if (maximumBytes <= 0 || maximumBytes <= readBytes) {
      return;
    }

    throw new IOException(
        "NBT too large! tried to read "
            + readBytes + " bytes where only "
            + maximumBytes + " bytes are permitted"
    );
  }

  @Override
  public void readFully(@NotNull byte[] b) throws IOException {
    accountBytes(b.length);
    base.readFully(b);
  }

  @Override
  public void readFully(@NotNull byte[] b, int off, int len)
      throws IOException
  {
    accountBytes(len);
    base.readFully(b, off, len);
  }

  @Override
  public int skipBytes(int n) throws IOException {
    accountBytes(n);
    return base.skipBytes(n);
  }

  @Override
  public boolean readBoolean() throws IOException {
    accountBytes(Byte.BYTES);
    return base.readBoolean();
  }

  @Override
  public byte readByte() throws IOException {
    accountBytes(Byte.BYTES);
    return base.readByte();
  }

  @Override
  public int readUnsignedByte() throws IOException {
    accountBytes(Byte.BYTES);
    return base.readUnsignedByte();
  }

  @Override
  public short readShort() throws IOException {
    accountBytes(Short.BYTES);
    return base.readShort();
  }

  @Override
  public int readUnsignedShort() throws IOException {
    accountBytes(Short.BYTES);
    return base.readUnsignedShort();
  }

  @Override
  public char readChar() throws IOException {
    accountBytes(Character.BYTES);
    return base.readChar();
  }

  @Override
  public int readInt() throws IOException {
    accountBytes(Integer.BYTES);
    return base.readInt();
  }

  @Override
  public long readLong() throws IOException {
    accountBytes(Long.BYTES);
    return base.readLong();
  }

  @Override
  public float readFloat() throws IOException {
    accountBytes(Float.BYTES);
    return base.readFloat();
  }

  @Override
  public double readDouble() throws IOException {
    accountBytes(Double.BYTES);
    return base.readDouble();
  }

  @Override
  public String readLine() throws IOException {
    String line = base.readLine();

    if (line != null) {
      accountBytes(line.length() + 1);
    }

    return line;
  }

  @NotNull
  @Override
  public String readUTF() throws IOException {
    String utf = base.readUTF();
    accountBytes((utf.length() * 2L) + 2L);
    return utf;
  }
}