package net.forthecrown.nbt.paper;

import net.forthecrown.nbt.CompoundTag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface ItemNbtProvider {

  static ItemNbtProvider provider() {
    return ItemNbtProviderImpl.INSTANCE;
  }

  CompoundTag saveItem(ItemStack item);

  void saveItem(CompoundTag tag, ItemStack item);

  ItemStack loadItem(CompoundTag tag);

  CompoundTag getUnhandledTags(ItemMeta meta);

  void setUnhandledTags(ItemMeta meta, CompoundTag tag);
}