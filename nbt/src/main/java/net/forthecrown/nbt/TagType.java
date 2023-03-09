package net.forthecrown.nbt;

import java.io.DataOutput;
import java.io.IOException;
import net.forthecrown.nbt.io.ScopedDataInput;

/**
 * A Binary Tag's type which specifies how tags are written and read from data
 * streams.
 *
 * @param <T> Tag type
 * @see <a href="https://minecraft.fandom.com/wiki/NBT_format#Data_types">NBT Data types, wiki</a>
 * @see TagTypes For accessing the types of each tag
 */
public interface TagType<T extends BinaryTag> {

  /**
   * Writes the specified {@code tag} to the {@code output}
   * @param tag Tag to write
   * @param output Output to write to
   * @throws IOException If an IO error occurs
   */
  void write(T tag, DataOutput output) throws IOException;

  /**
   * Reads a tag from the {@code input}
   * @param input Input to read from
   * @return Read tag
   * @throws IOException If an IO error occurs
   */
  T read(ScopedDataInput input) throws IOException;

  /**
   * Skips this tag's data in the {@code input}
   * @param input The input to skip
   * @throws IOException If an IO error occurs
   */
  default void skip(ScopedDataInput input) throws IOException {
    read(input);
  }

  /**
   * Gets this type's ID.
   * <p>
   * This ID can be written to an output stream, read, and then used as the
   * index for the type with {@link TagTypes#getType(byte)}
   *
   * @return This type's ID
   */
  byte getId();

  /**
   * Returns the type's name, for example: 'TAG_String'
   * @return type name
   */
  String getName();

  /**
   * Creates an invalid tag type.
   * <p>
   * The returned type will throw an IO exception anytime
   * {@link #read(ScopedDataInput)}, {@link #skip(ScopedDataInput)} or
   * {@link #write(BinaryTag, DataOutput)} is called.
   *
   * @param id Invalid type's ID
   * @return The created, invalid, type
   */
  static TagType<BinaryTag> createUnknown(byte id) {
    return new TagType<BinaryTag>() {
      IOException error() {
        return new IOException("Unknown type: " + id);
      }

      @Override
      public void write(BinaryTag tag, DataOutput output) throws IOException {
        throw error();
      }

      @Override
      public BinaryTag read(ScopedDataInput input) throws IOException {
        throw error();
      }

      @Override
      public void skip(ScopedDataInput input) throws IOException {
        throw error();
      }

      @Override
      public byte getId() {
        return id;
      }

      @Override
      public String getName() {
        return "UNKNOWN_" + id;
      }
    };
  }
}