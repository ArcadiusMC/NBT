package net.forthecrown.nbt;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

/**
 * A tag which holds a collection of tags
 */
public interface CollectionTag extends TagStructure {

  /**
   * @see Collection#clear()
   */
  void clear();

  /**
   * Iterates through each tag in this collection
   * @param consumer Action
   */
  void forEachTag(@NotNull Consumer<BinaryTag> consumer);

  /**
   * @see Collection#size()
   * @return Amount of items in this collection
   */
  int size();

  /**
   * Adds a tag into this collection
   * @param tag Tag to add
   * @return {@code true}, if the tag was added, {@code false} if it didn't
   *         match the collection's type
   */
  boolean addTag(BinaryTag tag);

  /**
   * Removes an element at a specified index
   * @see java.util.List#remove(int)
   * @param index Index of the element
   */
  void removeTag(int index);

  /**
   * @see java.util.List#get(int)
   * @param index index of the element to return
   * @return Element at the {@code index}
   */
  BinaryTag getTag(int index);

  /**
   * Tests if the collection is empty
   * @return {@code true}, if this collection contains no elements,
   *         {@code false} otherwise
   * @see Collection#isEmpty()
   */
  boolean isEmpty();

  /**
   * Sets the tag at a specified index
   * @param index Index of the tag
   * @param newTag Tag to insert
   * @return {@code true}, if it was inserted, {@code false} if the tag's type
   *         didn't match the collection's type
   */
  boolean setTag(int index, BinaryTag newTag);

  /**
   * Removes all tags matching the filter
   * @param filter Tag filter
   * @return amount of removed elements
   */
  int removeMatchingTags(Predicate<BinaryTag> filter);
}