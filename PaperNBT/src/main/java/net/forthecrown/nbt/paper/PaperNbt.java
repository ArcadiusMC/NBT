package net.forthecrown.nbt.paper;

import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.CompoundTag;
import net.kyori.adventure.text.Component;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;

public final class PaperNbt {
  private PaperNbt() {}

  static final ItemNbtProvider items;
  static final PaperNbtProvider tags;

  static {
    items = ItemNbtProvider.provider();
    tags = PaperNbtProvider.provider();
  }

  /* ---------------------------- GENERAL TAGS ---------------------------- */

  public CompoundTag saveEntity(Entity entity) {
    return tags.saveEntity(entity);
  }

  public void loadEntity(Entity entity, CompoundTag tag) {
    tags.loadEntity(entity, tag);
  }

  public static CompoundTag saveBlockEntity(TileState state) {
    return tags.saveBlockEntity(state);
  }

  public static void loadBlockEntity(TileState state, CompoundTag tag) {
    tags.loadBlockEntity(state, tag);
  }

  public static CompoundTag saveBlockData(BlockData data) {
    return tags.saveBlockData(data);
  }

  public static BlockData loadBlockData(CompoundTag tag) {
    return tags.loadBlockData(tag);
  }

  public static CompoundTag fromDataContainer(
      PersistentDataContainer container
  ) {
    return tags.fromDataContainer(container);
  }

  public static PersistentDataContainer toDataContainer(
      CompoundTag tag,
      PersistentDataAdapterContext context
  ) {
    return tags.toDataContainer(tag, context);
  }

  /* ----------------------------- ITEM DATA ------------------------------ */

  public static CompoundTag saveItem(ItemStack itemStack) {
    return items.saveItem(itemStack);
  }

  public static ItemStack loadItem(CompoundTag tag) {
    return items.loadItem(tag);
  }

  public CompoundTag getUnhandledTags(ItemMeta meta) {
    return items.getUnhandledTags(meta);
  }

  public void setUnhandledTags(ItemMeta meta, CompoundTag tag) {
    items.setUnhandledTags(meta, tag);
  }

  /* ------------------------- COMPONENT-RELATED -------------------------- */

  public static Component asComponent(BinaryTag tag) {
    return new TextTagVisitor().visit(tag);
  }

  public static Component asComponent(BinaryTag tag, String indent) {
    return new TextTagVisitor(indent).visit(tag);
  }

  public static Component asComponent(BinaryTag tag,
                                      String indent,
                                      boolean collapsePrimitiveLists
  ) {
    return new TextTagVisitor(indent)
        .setCollapsePrimitiveLists(collapsePrimitiveLists)
        .visit(tag);
  }
}