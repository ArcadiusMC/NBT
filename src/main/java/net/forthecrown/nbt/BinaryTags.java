package net.forthecrown.nbt;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongList;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import net.forthecrown.nbt.io.ScopedDataInput;
import net.forthecrown.nbt.io.TagIo;

/**
 * Factory, utility and IO functions related to {@link BinaryTag}
 */
public class BinaryTags {

  /* ----------------------------- FACTORIES ------------------------------ */

  /**
   * Creates a string tag
   * @param value tag's value
   * @return created string tag
   */
  public static StringTag stringTag(String value) {
    return StringTagImpl.of(value);
  }

  /**
   * Creates a {@code byte} tag
   * @param value tag's value, cast to a byte
   * @return Created tag
   */
  public static ByteTag byteTag(int value) {
    return new ByteTagImpl((byte) value);
  }

  /**
   * Creates a {@code byte} tag
   * @param value tag's value
   * @return Created tag
   */
  public static ByteTag byteTag(byte value) {
    return new ByteTagImpl(value);
  }

  /**
   * Creates a {@code short} tag
   * @param value tag's value
   * @return Created tag
   */
  public static ShortTag shortTag(short value) {
    return new ShortTagImpl(value);
  }

  /**
   * Creates a {@code short} tag
   * @param value tag's value, cast to a short
   * @return Created tag
   */
  public static ShortTag shortTag(int value) {
    return new ShortTagImpl((short) (value & 0xFFFF));
  }

  /**
   * Creates a {@code int} tag
   * @param value tag's value
   * @return Created tag
   */
  public static IntTag intTag(int value) {
    return new IntTagImpl(value);
  }

  /**
   * Creates a {@code long} tag
   * @param value tag's value
   * @return Created tag
   */
  public static LongTag longTag(long value) {
    return new LongTagImpl(value);
  }

  /**
   * Creates a {@code float} tag
   * @param value tag's value
   * @return Created tag
   */
  public static FloatTag floatTag(float value) {
    return new FloatTagImpl(value);
  }

  /**
   * Creates a {@code double} tag
   * @param value tag's value
   * @return Created tag
   */
  public static DoubleTag doubleTag(double value) {
    return new DoubleTagImpl(value);
  }

  /**
   * Gets the 'TAG_End' singleton instance
   * @return End tag instance
   */
  public static EndTag endTag() {
    return EndTagImpl.INSTANCE;
  }

  /**
   * Creates a new {@link CompoundTag}
   * @return created tag
   */
  public static CompoundTag compoundTag() {
    return new CompoundTagImpl();
  }

  /**
   * Creates a new {@link CompoundTag} with the specified {@code tags}
   * @param tags Tags to include in the created tag
   * @return created tag
   */
  public static CompoundTag compoundTag(Map<String, BinaryTag> tags) {
    return new CompoundTagImpl(tags);
  }

  /**
   * Creates an empty byte array tag
   * @return created tag
   */
  public static ByteArrayTag byteArrayTag() {
    return new ByteArrayTagImpl();
  }

  /**
   * Creates a new byte array with a specified size
   * @param size Size of the created array
   * @return created tag
   */
  public static ByteArrayTag byteArrayTag(int size) {
    return new ByteArrayTagImpl(new byte[size]);
  }

  /**
   * Creates a new byte array with the specified values
   * @param values Array values
   * @return created tag
   */
  public static ByteArrayTag byteArrayTag(byte... values) {
    return new ByteArrayTagImpl(values);
  }

  /**
   * Creates a new byte array with the specified values
   * @param values Array values
   * @return created tag
   */
  public static ByteArrayTag byteArrayTag(Collection<Byte> values) {
    return new ByteArrayTagImpl(values);
  }

  /**
   * Creates an empty int array tag
   * @return created tag
   */
  public static IntArrayTag intArrayTag() {
    return new IntArrayTagImpl();
  }

  /**
   * Creates a new int array with a specified size
   * @param size Size of the created array
   * @return created tag
   */
  public static IntArrayTag intArrayTag(int size) {
    return new IntArrayTagImpl(size);
  }

  /**
   * Creates a new int array with the specified values
   * @param values Array values
   * @return created tag
   */
  public static IntArrayTag intArrayTag(int... values) {
    return new IntArrayTagImpl(values);
  }

  /**
   * Creates a new int array with the specified values
   * @param values Array values
   * @return created tag
   */
  public static IntArrayTag intArrayTag(Collection<Integer> values) {
    return new IntArrayTagImpl(values);
  }

  /**
   * Creates an empty long array tag
   * @return created tag
   */
  public static LongArrayTag longArrayTag() {
    return new LongArrayTagImpl();
  }

  /**
   * Creates a new long array with a specified size
   * @param size Size of the created array
   * @return created tag
   */
  public static LongArrayTag longArrayTag(int size) {
    return new LongArrayTagImpl(size);
  }

  /**
   * Creates a new long array with the specified values
   * @param values Array values
   * @return created tag
   */
  public static LongArrayTag longArrayTag(long... values) {
    return new LongArrayTagImpl(values);
  }

  /**
   * Creates a new long array with the specified values
   * @param values Array values
   * @return created tag
   */
  public static LongArrayTag longArrayTag(Collection<Long> values) {
    return new LongArrayTagImpl(values);
  }

  /**
   * Creates an empty {@link ListTag}
   * @return new list tag
   */
  public static ListTag listTag() {
    return new ListTagImpl();
  }

  /**
   * Creates a new {@link ListTag} with the specified values
   * @param values Values to create the list with
   * @return created tag
   */
  public static ListTag listTag(Collection<BinaryTag> values) {
    return new ListTagImpl(values);
  }

  /**
   * Creates a new {@link ListTag} with the specified values
   * @param values Values to create the list with
   * @return created tag
   */
  public static ListTag listTag(BinaryTag... values) {
    return listTag(Arrays.asList(values));
  }

  /**
   * Creates a new list {@link ListTag} with the specified double values
   * @param values Values to add to the created list
   * @return created tag
   */
  public static ListTag doubleList(double... values) {
    return Arrays.stream(values)
        .mapToObj(BinaryTags::doubleTag)
        .collect(tagCollector());
  }

  /**
   * Creates a new list {@link ListTag} with the specified double values
   * @param values Values to add to the created list
   * @return created tag
   */
  public static ListTag doubleList(Collection<Double> values) {
    return values.stream()
        .map(BinaryTags::doubleTag)
        .collect(tagCollector());
  }

  /**
   * Creates a new list {@link ListTag} with the specified float values
   * @param values Values to add to the created list
   * @return created tag
   */
  public static ListTag floatList(float... values) {
    ListTag list = listTag();
    for (float f: values) {
      list.add(floatTag(f));
    }
    return list;
  }

  /**
   * Creates a new list {@link ListTag} with the specified float values
   * @param values Values to add to the created list
   * @return created tag
   */
  public static ListTag floatList(Collection<Float> values) {
    return values.stream()
        .map(BinaryTags::floatTag)
        .collect(tagCollector());
  }

  /**
   * Creates a {@link ListTag} of strings
   * @param values The list values
   * @return Created list
   */
  public static ListTag stringList(String... values) {
    return Arrays.stream(values)
        .map(BinaryTags::stringTag)
        .collect(tagCollector());
  }

  /**
   * Creates a {@link ListTag} of strings
   * @param values The list values
   * @return Created list
   */
  public static ListTag stringList(Collection<String> values) {
    return values.stream()
        .map(BinaryTags::stringTag)
        .collect(tagCollector());
  }

  /**
   * Gets a collector that collects {@link BinaryTag}s into a {@link ListTag}
   * @return {@link ListTag} collector
   */
  public static Collector<BinaryTag, ListTag, ListTag> tagCollector() {
    return ListTagImpl.COLLECTOR;
  }

  /**
   * Collects a stream of {@code int}s into a {@link IntArrayTag}
   * @param stream Stream to collect
   * @return Collected array
   */
  public static IntArrayTag collectInts(IntStream stream) {
    return stream.collect(
        BinaryTags::intArrayTag,
        IntList::add,
        IntList::addAll
    );
  }

  /**
   * Collects a stream of {@code long}s into a {@link LongArrayTag}
   * @param stream Stream to collect
   * @return Collected array
   */
  public static LongArrayTag collectLongs(LongStream stream) {
    return stream.collect(
        BinaryTags::longArrayTag,
        LongList::add,
        LongList::addAll
    );
  }

  /* --------------------------------- IO --------------------------------- */

  /**
   * Writes a named tag.
   * <p>
   * The written data follows the NBT specification. The first byte is the ID
   * of the tag being written, then a modified UTF-8 string and then the tag's
   * own data
   *
   * @param entry Named tag to write
   * @param output Output to write to
   * @throws IOException If an IO error occurs
   * @see TagIo#writeNamedTag(Entry, DataOutput)
   */
  public static void writeNamedTag(Entry<String, BinaryTag> entry,
                                   DataOutput output
  ) throws IOException {
    TagIo.tagIo().writeNamedTag(entry, output);
  }

  /**
   * Writes a named tag.
   * <p>
   * The written data follows the NBT specification. The first byte is the ID
   * of the tag being written, then a modified UTF-8 string and then the tag's
   * own data
   *
   * @param name Tag's name
   * @param tag Tag to write
   * @param output Output to write to
   * @throws IOException If an IO error occurs
   * @see TagIo#writeNamedTag(String, BinaryTag, DataOutput)
   */
  public static void writeNamedTag(String name,
                                   BinaryTag tag,
                                   DataOutput output
  ) throws IOException {
    TagIo.tagIo().writeNamedTag(name, tag, output);
  }

  /**
   * Reads a named tag.
   * <p>
   * This method attempts to read a UTF-8 string, then the tag's own data,
   * specified by the {@link TagType} registered with the {@code typeId}
   *
   * @param typeId ID of the tag's type
   * @param input Input to read from
   * @return Read tag
   * @throws IOException If an IO error occurs, if more than {@code maxBytes} is
   *                     read or the tag depth goes above
   *                     {@link ScopedDataInput#MAX_DEPTH}
   */
  public static Entry<String, BinaryTag> readNamedTag(
      byte typeId,
      ScopedDataInput input
  ) throws IOException {
    return TagIo.tagIo().readNamedTag(typeId, input);
  }

  /**
   * Writes the specified {@code tag} to the {@code outputStream}
   * @param outputStream Stream to write to
   * @param tag Tag to write
   * @throws IOException If an IO error occurs
   * @see TagIo#write(OutputStream, CompoundTag)
   */
  public static void write(OutputStream outputStream, CompoundTag tag)
      throws IOException
  {
    TagIo.tagIo().write(outputStream, tag);
  }

  /**
   * Compresses the {@code outputStream} and then writes the specified
   * {@code tag} to it.
   *
   * @param outputStream Stream to write to
   * @param tag Tag to write
   * @throws IOException If an IO error occurs
   * @see TagIo#writeCompressed(OutputStream, CompoundTag)
   */
  public static void writeCompressed(OutputStream outputStream,
                                     CompoundTag tag
  ) throws IOException {
    TagIo.tagIo().writeCompressed(outputStream, tag);
  }

  /**
   * Reads a {@link CompoundTag} from an {@code inputStream}. This method has no
   * byte read limit
   *
   * @param inputStream Stream to read
   * @return Read tag
   * @throws IOException If an IO error occurs, or the tag depth goes above
   *                     {@link ScopedDataInput#MAX_DEPTH}
   * @see #read(InputStream, long)
   */
  public static CompoundTag read(InputStream inputStream) throws IOException {
    return read(inputStream, 0);
  }

  /**
   * Reads a {@link CompoundTag} from the {@code inputStream}
   * @param inputStream Stream to read
   * @param maxBytes Maximum bytes that can be read, {@code 0} or less, for
   *                 no limit
   * @return Read tag
   * @throws IOException If an IO error occurs, if more than {@code maxBytes} is
   *                     read or the tag depth goes above
   *                     {@link ScopedDataInput#MAX_DEPTH}
   * @see TagIo#read(InputStream, long)
   */
  public static CompoundTag read(InputStream inputStream, long maxBytes)
      throws IOException
  {
    return TagIo.tagIo().read(inputStream, maxBytes);
  }

  /**
   * Decompresses the {@code inputStream} and then reads a {@link CompoundTag}
   * from the stream. This method has no byte read limit
   * @param inputStream Stream to read from
   * @return Read tag
   * @throws IOException If an IO error occurs
   * @see #readCompressed(InputStream, long)
   */
  public static CompoundTag readCompressed(InputStream inputStream)
      throws IOException
  {
    return readCompressed(inputStream, 0);
  }

  /**
   * Decompresses the {@code inputStream} and then reads a {@link CompoundTag}
   * from the stream.
   * 
   * @param inputStream Stream to read
   * @param maxBytes Maximum bytes that can be read, {@code 0} or less for
   *                 no limit
   * @return Read tag
   * @throws IOException If an IO error occurs, if more than {@code maxBytes} is
   *                     read or the tag depth goes above
   *                     {@link ScopedDataInput#MAX_DEPTH}
   * @see TagIo#readCompressed(InputStream) 
   */
  public static CompoundTag readCompressed(InputStream inputStream,
                                           long maxBytes
  ) throws IOException {
    return TagIo.tagIo().readCompressed(inputStream, maxBytes);
  }

  /* ----------------------------- UTILITIES ------------------------------ */

  /**
   * Converts an {@link IntArrayTag} to a {@link UUID}
   * <p>
   * This method serves as the inverse to {@link #saveUuid(UUID)}
   * @param arr Array to load from
   * @return Loaded UUID
   * @throws IllegalArgumentException If the {@code arr} length is not 4
   * @see #saveUuid(UUID)
   */
  public static UUID loadUuid(IntArrayTag arr) throws IllegalArgumentException {
    if (arr.size() != 4) {
      throw new IllegalArgumentException("IntArrayTag size must be 4 for UUID");
    }

    int[] intArray = arr.toIntArray();

    long most = (long) intArray[0] << Integer.SIZE;
    most |= intArray[1];

    long least = (long) intArray[2] << Integer.SIZE;
    least |= intArray[3];

    return new UUID(most, least);
  }

  /**
   * Saves a {@link UUID} into a {@link IntArrayTag}
   * <p>
   * Saving is done by creating an integer array of 4 elements, the values at
   * each index are as follows:
   * <pre>
   * [0] - Highest 32 bits of {@link UUID#getMostSignificantBits()}
   * [1] - Lowest 32 bits of {@link UUID#getMostSignificantBits()}
   * [2] - Highest 32 bits of {@link UUID#getLeastSignificantBits()}
   * [3] - Lowest 32 bits of {@link UUID#getLeastSignificantBits()}
   * </pre>
   * @param uuid UUID to save
   * @return Saved UUID
   */
  public static IntArrayTag saveUuid(UUID uuid) {
    Objects.requireNonNull(uuid);

    long most = uuid.getMostSignificantBits();
    long least = uuid.getLeastSignificantBits();

    return intArrayTag(
        (int) (most >> Integer.SIZE),
        (int) most,
        (int) (least >> Integer.SIZE),
        (int) least
    );
  }

  /**
   * Compares 2 tags, returning {@code true} if the tags are 'matching',
   * {@code false}, otherwise.
   * <p>
   * If {@code predicate == null}, this returns {@code true}.
   * <p>
   * For most tags, this simply calls {@link Object#equals(Object)}.
   * <p>
   * In the case that {@link CompoundTag}s are being compared, the subject must
   * contain each key value mapping of the {@code predicate} parameter,
   * additionally, each of those mappings must also pass this method.
   * <p>
   * For {@link ListTag} comparisons, the subject must contain each value of
   * the predicate tag. If {@code ignoreListOrder == true}, then the list order
   * is ignored, otherwise lists are compared like other tags.
   *
   * @param predicate Filter tag, if {@code null}, returns {@code true}
   * @param subject Tag to test against the filter tag
   * @param ignoreListOrder {@code true}, to ignore list order if the tags are
   *                        array based
   *
   * @return {@code true}, if the tags are matching, {@code false}, otherwise
   */
  public static boolean compareTags(BinaryTag predicate,
                                    BinaryTag subject,
                                    boolean ignoreListOrder
  ) {
    if (predicate == null) {
      return true;
    }

    if (subject == null || predicate.getId() != subject.getId()) {
      return false;
    }

    if (predicate instanceof CompoundTag predicateCompound) {
      CompoundTag subjectCompound = subject.asCompound();
      var entries = predicateCompound.entrySet();

      for (var e: entries) {
        String key = e.getKey();
        var value = e.getValue();

        if (!compareTags(value, subjectCompound.get(key), ignoreListOrder)) {
          return false;
        }
      }

      return true;
    }

    if (predicate instanceof ListTag list && ignoreListOrder) {
      ListTag subjectList = subject.asList();

      if (list.isEmpty()) {
        return subjectList.isEmpty();
      }

      for (var tag: list) {
        boolean matchFound = false;

        for (var subjectTag: subjectList) {
          if (compareTags(tag, subjectTag, true)) {
            matchFound = true;
            break;
          }
        }

        if (!matchFound) {
          return false;
        }
      }

      return true;
    }

    return Objects.equals(predicate, subject);
  }
}