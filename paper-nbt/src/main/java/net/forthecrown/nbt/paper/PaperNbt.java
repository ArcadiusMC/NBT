package net.forthecrown.nbt.paper;

import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.CompoundTag;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

/**
 * General utility class that provides access to NBT tags in Bukkit, Spigot and
 * Paper
 */
public final class PaperNbt {
  private PaperNbt() {}

  static final ItemNbtProvider items;
  static final PaperNbtProvider tags;

  static {
    items = ItemNbtProvider.provider();
    tags = PaperNbtProvider.provider();
  }

  /* ---------------------------- GENERAL TAGS ---------------------------- */

  /**
   * Saves the specified entity into an NBT tag, the returned tag will not have
   * the 'id' tag of the entity which specifies the entity's type
   *
   * @param entity Entity to save
   * @return Entity NBT
   */
  public static CompoundTag saveEntity(Entity entity) {
    return tags.saveEntity(entity);
  }

  /**
   * Loads the specified entity from the specified tag
   * @param entity Entity to load
   * @param tag Tag to load from
   */
  public static void loadEntity(Entity entity, CompoundTag tag) {
    tags.loadEntity(entity, tag);
  }

  /**
   * Saves a block entity's data
   * @param state Block entity to save
   * @return Block entity NBT
   */
  public static CompoundTag saveBlockEntity(TileState state) {
    return tags.saveBlockEntity(state);
  }

  /**
   * Loads a block entity from specified {@code tag}
   * @param state Block Entity to load
   * @param tag Tag to load from
   */
  public static void loadBlockEntity(TileState state, CompoundTag tag) {
    tags.loadBlockEntity(state, tag);
  }

  /**
   * Saves block data. This includes the material and the data's properties
   * @param data Data to save
   * @return Block NBT
   */
  public static CompoundTag saveBlockData(BlockData data) {
    return tags.saveBlockData(data);
  }

  /**
   * Loads block data saved by {@link #saveBlockData(BlockData)}
   * @param tag Tag to load from
   * @return Loaded block data
   */
  public static BlockData loadBlockData(CompoundTag tag) {
    return tags.loadBlockData(tag);
  }

  /**
   * Creates a NBT tag from a {@link PersistentDataContainer} instance
   *
   * @param container Container to create the NBT Tag from
   * @return Created NBT tag
   */
  public static CompoundTag fromDataContainer(
      PersistentDataContainer container
  ) {
    return tags.fromDataContainer(container);
  }

  /**
   * Converts an NBT tag into a {@link PersistentDataContainer}
   * @param tag Tag to convert to a container
   * @param context Context, required to instantiate the container
   * @return The created container
   */
  public static PersistentDataContainer toDataContainer(
      CompoundTag tag,
      PersistentDataAdapterContext context
  ) {
    return tags.toDataContainer(tag, context);
  }

  /**
   * Puts all entries in the specified {@code tag} into the specified {@code container}
   *
   * @param container Container to insert entries into
   * @param tag Tag to take values from
   */
  public static void mergeToContainer(PersistentDataContainer container, CompoundTag tag) {
    tags.mergeToContainer(container, tag);
  }

  /**
   * Gets a data assocated with the specified {@code key} in the server's command storage.
   * <p>
   * Any values in the command storage can also be accessed with the
   * {@code /data get storage <key>} command.
   *
   * @param key Storage key
   *
   * @return Stored command data, or an empty tag, if there was no data associated with
   *         the specified {@code key}
   */
  public static CompoundTag getStoredData(@NotNull NamespacedKey key) {
    return tags.getStoredData(key);
  }

  /**
   * Sets a data value in the server's command storage.
   * <p>
   * Any values in the command storage can also be modified wit the
   * {@code /data merge storage <key> <nbt data>} command.
   *
   * @param key Storage key
   * @param tag Storage value
   */
  public static void setStoredData(@NotNull NamespacedKey key, @NotNull CompoundTag tag) {
    tags.setStoredData(key, tag);
  }

  /* ----------------------------- ITEM DATA ------------------------------ */

  /**
   * Saves an item
   * @param itemStack Item to save
   * @return Item NBT
   */
  public static CompoundTag saveItem(ItemStack itemStack) {
    return items.saveItem(itemStack);
  }

  /**
   * Loads an item
   * @param tag Tag to load from
   * @return Loaded item
   */
  public static ItemStack loadItem(CompoundTag tag) {
    return items.loadItem(tag);
  }

  /**
   * Gets the unhandled tags inside an item meta.
   * <p>
   * Unhandled tags in this function's context is just the {@code custom_data}
   *
   * @deprecated Misleading name
   * @param meta Meta to get tags from
   * @return Unhandled tags
   */
  @Deprecated
  public static CompoundTag getUnhandledTags(ItemMeta meta) {
    return items.getCustomData(meta);
  }

  /**
   * Gets the value of an item's {@code custom_data} component
   * @param meta Meta to get custom data of
   * @return Custom data, or an empty compound tag, if no data was found
   */
  public static CompoundTag getCustomData(ItemMeta meta) {
    return items.getCustomData(meta);
  }

  /**
   * Sets the unhandled tags inside an item meta
   * <p>
   * Unhandled tags in this function's context is just the {@code custom_data}
   *
   * @param meta Meta to set the tags of
   * @param tag New value for the unhandled tags
   * @deprecated Misleading name
   */
  @Deprecated
  public static void setUnhandledTags(ItemMeta meta, CompoundTag tag) {
    items.setCustomData(meta, tag);
  }

  /**
   * Sets the value of the {@code custom_data} component of an item.
   * @param meta Item meta to edit
   * @param tag Custom data tag
   */
  public static void setCustomData(ItemMeta meta, CompoundTag tag) {
    items.setCustomData(meta, tag);
  }

  /* ------------------------- COMPONENT-RELATED -------------------------- */

  /**
   * Converts the specified tag into a text component representation, exactly
   * how the {@code /data view <arguments>} command would.
   *
   * @param tag Tag to convert
   * @return The Tag's textual representation
   *
   * @see #asComponent(BinaryTag, String)
   * @see #asComponent(BinaryTag, String, boolean)
   */
  public static Component asComponent(BinaryTag tag) {
    return new TextTagVisitor().visit(tag);
  }

  /**
   * Converts the specified tag into a text component representation, exactly
   * how the {@code /data view <arguments>} command would.
   *
   * @param tag Tag to convert
   * @param indent String that the returned text will be indented with
   * @return The Tag's textual representation
   *
   * @see #asComponent(BinaryTag, String, boolean)
   */
  public static Component asComponent(BinaryTag tag, String indent) {
    return new TextTagVisitor(indent).visit(tag);
  }

  /**
   * Converts the specified tag into a text component representation, exactly
   * how the {@code /data view <arguments>} command would.
   *
   * @param tag Tag to convert
   * @param indent String that the returned text will be indented with
   * @param collapsePrimitiveLists {@code true}, to collapse all primitive
   *                               arrays into single lines, even if pretty
   *                               printing is enabled
   *
   * @return The Tag's textual representation
   *
   * @see #asComponent(BinaryTag, String, boolean)
   */
  public static Component asComponent(BinaryTag tag,
                                      String indent,
                                      boolean collapsePrimitiveLists
  ) {
    return new TextTagVisitor(indent)
        .setCollapsePrimitiveLists(collapsePrimitiveLists)
        .visit(tag);
  }
}