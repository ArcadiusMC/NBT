package net.forthecrown.nbt.path;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.path.TagPathImpl.BuilderImpl;
import net.forthecrown.nbt.string.TagParseException;
import net.forthecrown.nbt.util.ReaderWrapper;
import net.forthecrown.nbt.util.TagPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A path within an NBT data structure used to access, insert, or remove values
 * with the structure.
 *
 * <p>
 * Tag path's can be parsed with {@link #parse(String)}, for example:
 * <pre>
 * String pathString = "{foo:1b}.key1.key2.array[]"
 * TagPath path = TagPath.parse(pathString);
 * </pre>
 *
 * Path objects can also be built programmatically with a path builder, created
 * with {@link #builder()}, for example, the builder equivalent of the above
 * example would be:
 * <pre>
 * CompoundTag rootFilter = BinaryTags.compoundTag();
 * rootFilter.putByte("foo", (byte) 1);
 *
 * TagPath path = TagPath.builder()
 *     .setRootFilter(rootFilter)
 *
 *     .addObjectNode("key1")
 *     .addObjectNode("key2")
 *     .addObjectNode("array")
 *
 *     .addMatchAll()
 *     .build();
 * </pre>
 *
 * Once you have a path object, you can access values with the {@link #get(BinaryTag)}
 * method, for example:
 * <pre>
 * // Accessing an item's JSON display name from its NBT data
 *
 * TagPath path = TagPath.parse("tag.display.Name");
 * CompoundTag itemData = // ...
 *
 * List&lt;BinaryTag&gt; tags = path.get(itemData);
 * assert tags.size() &lt; 2;
 *
 * System.out.println(tags.get(0));
 * </pre>
 *
 * Or insert items into the NBT:
 * <pre>
 * // Assume these have the same values as those above
 * TagPath path = // ...
 * CompoundTag tag = // ...
 *
 * StringTag insertValue = BinaryTags.stringTag("A cool item name");
 *
 * // Amount of tags that were changed during insertion,
 * // will be 0, if nothing changed
 * int changedValues = path.set(tag, insertValue);
 * </pre>
 * No need to worry about the full tag structure not existing when inserting,
 * when a value is inserted into a tag, the path object ensures that all the
 * parent tags required for the value's insertion exist.
 *
 * <p>
 * And finally remove values:
 * <pre>
 * TagPath path = // ...
 * CompoundTag tag = // ...
 *
 * // Result is basically the same as, set(), amount of tags changed
 * int removedTags = path.remove(tag);
 * </pre>
 *
 * If you need to convert a tag back to the input it was parsed from, or turn a
 * built path into parse-able input, use {@link #getInput()}. Note that if you
 * used any custom predicates in the builder while creating the path, they will
 * be omitted from the method's result
 *
 * @see <a href="https://minecraft.fandom.com/wiki/NBT_path_format">NBT Path Format</a>
 *
 * @see #get(BinaryTag) Accessing values using paths
 * @see #set(BinaryTag, BinaryTag) Inserting values
 * @see #remove(BinaryTag) Removing values
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
    return new PathParser(new ReaderWrapper(string)).parse();
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
    return new PathParser(new ReaderWrapper(reader)).parse();
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
   * @param element Element to place into the {@code tag}, will be copied each
   *                time it's inserted into a different container
   * @return Amount of changed elements, 0, if nothing changed
   */
  default int set(@NotNull BinaryTag tag, @NotNull BinaryTag element) {
    Objects.requireNonNull(element, "Element");
    return set(tag, element::copy);
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
     * path parse input, for example: {@code {foo:'bar'}node1.node2}
     * <p>
     * Note: If the specified {@code filter} is not a {@link TagPredicate} then
     * the filter will not be included in the {@link #getInput()} result
     *
     * @param filter Root input filter
     * @return This
     */
    Builder setRootFilter(@Nullable Predicate<BinaryTag> filter);

    /**
     * Sets the filter applied to the root input
     * <p>
     * Delegate for {@link #setRootFilter(Predicate)} which uses a
     * {@link TagPredicate} to allow the path to be completely stringified.
     * <p>
     * The tag is used with {@link net.forthecrown.nbt.BinaryTags#compareTags(BinaryTag, BinaryTag, boolean)}
     * to filter values
     *
     * @param filterTag Root input filter
     * @return This
     */
    default Builder setRootFilter(@NotNull BinaryTag filterTag) {
      Objects.requireNonNull(filterTag);
      return setRootFilter(new TagPredicate(filterTag));
    }

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
     * <p>
     * Note: If the specified {@code filter} is not a {@link TagPredicate} then
     * the filter will not be included in the {@link #getInput()} result.
     *
     * @param filter Filter to apply to array entries
     * @return This
     */
    Builder addMatchAll(@Nullable Predicate<BinaryTag> filter);

    /**
     * Adds a node that will attempt to match all elements in a given
     * {@link net.forthecrown.nbt.CollectionTag} against a specified
     * {@code filter}.
     * <p>
     * Delegate for {@link #addMatchAll(Predicate)} which uses a
     * {@link TagPredicate} to allow the path to be completely stringified.
     * <p>
     * The tag is used with {@link net.forthecrown.nbt.BinaryTags#compareTags(BinaryTag, BinaryTag, boolean)}
     * to filter values
     *
     * @param filterTag Filter to apply to array entries
     * @return This
     */
    default Builder addMatchAll(@NotNull BinaryTag filterTag) {
      Objects.requireNonNull(filterTag);
      return addMatchAll(new TagPredicate(filterTag));
    }

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
     * <p>
     * Note: If the specified {@code filter} is not a {@link TagPredicate} then
     * the filter will not be included in the {@link #getInput()} result.
     *
     * @param name Name of the mapping to access
     * @param filter Filter to apply to mappings
     * @return This
     */
    Builder addObjectNode(String name, Predicate<BinaryTag> filter);

    /**
     * Adds a node that will return a filtered mapping in a
     * {@link net.forthecrown.nbt.CompoundTag}.
     * <p>
     * Delegate for {@link #addObjectNode(String, Predicate)} which uses a
     * {@link TagPredicate} to allow for the path to be completely stringified.
     * <p>
     * The tag is used with {@link net.forthecrown.nbt.BinaryTags#compareTags(BinaryTag, BinaryTag, boolean)}
     * to filter values
     *
     * @param name Name of the mapping to access
     * @param filterTag Filter to apply to mappings
     *
     * @return This
     */
    default Builder addObjectNode(String name, @NotNull BinaryTag filterTag) {
      Objects.requireNonNull(filterTag);
      return addObjectNode(name, new TagPredicate(filterTag));
    }

    /**
     * Builds the path
     * @return The built path
     */
    TagPath build();
  }
}