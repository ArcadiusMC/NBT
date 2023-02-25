package net.forthecrown.nbt.path;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.path.TagPathImpl.BuilderImpl;
import net.forthecrown.nbt.string.TagParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A path within an NBT data structure.
 * @see <a href="https://minecraft.fandom.com/wiki/NBT_path_format">NBT Path Format</a>
 */
public interface TagPath {

  /**
   * Parses a tag path from a specified {@code string}
   * <p>
   * Delegate method for {@link #parse(Reader)}
   *
   * @param string Input
   * @return Parsed path
   *
   * @throws PathParseException If the input cannot be parsed
   * @throws TagParseException If a filter in the input cannot be parsed
   * @see #parse(Reader)
   */
  static TagPath parse(@NotNull String string)
      throws PathParseException, TagParseException
  {
    return parse(new StringReader(string));
  }

  /**
   * Parses a tag path from a specified {@code reader}.
   *
   * @param reader Input source
   * @return Parsed path
   *
   * @throws PathParseException If the input cannot be parsed
   * @throws TagParseException If a filter in the input cannot be parsed
   */
  static TagPath parse(@NotNull Reader reader)
      throws PathParseException, TagParseException
  {
    return new PathParser(reader).parse();
  }

  /**
   * Creates a new path builder
   * @return A builder
   */
  static Builder builder() {
    return new BuilderImpl();
  }

  /**
   * Gets all elements pointed to by this path
   * @param tag Tag to get elements from
   * @return All elements pointed to by this path, or
   *         {@link Collections#emptyList()}, if no tags were found
   */
  @NotNull List<BinaryTag> get(@NotNull BinaryTag tag);

  /**
   * Removes the element pointed to by this path
   * @param tag Tag to remove the element from
   * @return Amount of removed elements, 0, if nothing changed
   */
  int remove(@NotNull BinaryTag tag);

  /**
   * Sets the tag pointed to by this path
   * @param tag Tag to change
   * @param supplier Element to place into the {@code tag}
   * @return Amount of changed elements, 0, if nothing changed
   */
  int set(@NotNull BinaryTag tag, @NotNull Supplier<BinaryTag> supplier);

  /**
   * Sets the tag pointed to by this path
   * @param tag Tag to change
   * @param element Element to place into the {@code tag}
   * @return Amount of changed elements, 0, if nothing changed
   */
  default int set(@NotNull BinaryTag tag, @NotNull BinaryTag element) {
    Objects.requireNonNull(element, "Element");
    return set(tag, () -> element);
  }

  /**
   * Translates this path into parse-able input
   * @return A parse-able representation of this path
   */
  @NotNull String getInput();

  /**
   * A path builder to allow for creating {@link TagPath} instances
   * programmatically instead of parsing them
   */
  interface Builder {

    /**
     * Sets the filter applied to the root input.
     * <p>
     * The parse equivalent to this is simply prepending an NBT tag onto the
     * path parse input, for example:
     *
     * {@code {foo:'bar'}node1.node2}
     * @param filter Root input filter
     * @return This
     */
    Builder setRootFilter(@Nullable Predicate<BinaryTag> filter);

    /**
     * Adds a node that accesses a {@link net.forthecrown.nbt.CollectionTag}
     * with the specified {@code index}.
     * <p>
     * The parse equivalent to this is the {@code [#]} token, for example:
     * {@code nodeName[1]}
     *
     * @param index List index
     * @return This
     */
    Builder addIndexNode(int index);

    /**
     * Adds a node that will attempt to match all elements in a given
     * {@link net.forthecrown.nbt.CollectionTag} against a specified
     * {@code filter}.
     * <p>
     * The parse equivalent to this is {@code [{NBT tag}]}, for example:
     * {@code nodeName[{foo:'bar'}]}
     *
     * @param filter Filter to apply to array entries
     * @return This
     */
    Builder addMatchAll(@Nullable Predicate<BinaryTag> filter);

    /**
     * Adds a node that will return all entries in a
     * {@link net.forthecrown.nbt.CollectionTag}.
     * <p>
     * The parse equivalent to this is the {@code []} token, for example:
     * {@code nodeName[]}
     *
     * @return
     */
    Builder addMatchAll();

    /**
     * Adds a node that will return a mapping in a
     * {@link net.forthecrown.nbt.CompoundTag}.
     * <p>
     * The parse equivalent to this is the {@code nodeName} token, for example:
     * {@code foo.bar}
     *
     * @param name Name of the mapping to access
     * @return This
     */
    Builder addObjectNode(String name);

    /**
     * Adds a node that will return a filtered mapping in a
     * {@link net.forthecrown.nbt.CompoundTag}.
     * <p>
     * The parse equivalent to this is the {@code nodeName{NBT tag}} token, for
     * example: {@code foo{a_value:1b}.bar.foobar}
     *
     * @param name Name of the mapping to access
     * @param filter Filter to apply to mappings
     * @return This
     */
    Builder addObjectNode(String name, Predicate<BinaryTag> filter);

    /**
     * Builds the path
     * @return The built path
     */
    TagPath build();
  }
}