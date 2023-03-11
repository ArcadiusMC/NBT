package net.forthecrown.nbt.paper;

import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.CompoundTag;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;

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
   * Unhandled tags are any tags that Bukkit have not handled by themselves,
   * allowing us to edit the custom NBT inside items without saving and loading
   * the entire item-stack
   *
   * @param meta Meta to get tags from
   * @return Unhandled tags
   */
  public static CompoundTag getUnhandledTags(ItemMeta meta) {
    return items.getUnhandledTags(meta);
  }

  /**
   * Sets the unhandled tags inside an item meta
   * <p>
   * Unhandled tags are any tags that Bukkit have not handled by themselves,
   * allowing us to edit the custom NBT inside items without saving and loading
   * the entire item-stack
   *
   * @param meta Meta to set the tags of
   * @param tag New value for the unhandled tags
   */
  public static void setUnhandledTags(ItemMeta meta, CompoundTag tag) {
    items.setUnhandledTags(meta, tag);
  }

  /**
   * Saves the specified metadata of the item. This is pretty much the 'tag'
   * part of the item's NBT
   *
   * @param meta meta to save
   * @return saved meta
   */
  public static CompoundTag saveMeta(ItemMeta meta) {
    return items.saveMeta(meta);
  }

  /**
   * Loads item meta
   * <p>
   * This method loads the 'tag' part of an item's NBT data
   *
   * @param tag tag to load from
   * @param material the meta's type
   * @return Loaded meta
   */
  public static ItemMeta loadMeta(CompoundTag tag, Material material) {
    return items.loadMeta(tag, material);
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