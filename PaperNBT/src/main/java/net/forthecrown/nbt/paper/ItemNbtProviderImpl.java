package net.forthecrown.nbt.paper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

class ItemNbtProviderImpl implements ItemNbtProvider {
  static ItemNbtProviderImpl INSTANCE = new ItemNbtProviderImpl();

  private static final Field unhandledTags;

  static {
    String packageName = CraftItemStack.class.getPackageName();

    try {
      Class<?> metaClass = Class.forName(packageName + ".CraftMetaItem");
      unhandledTags = metaClass.getDeclaredField("unhandledTags");
      unhandledTags.setAccessible(true);
    } catch (ReflectiveOperationException exc) {
      throw new IllegalStateException(exc);
    }
  }

  @Override
  public CompoundTag saveItem(ItemStack item) {
    net.minecraft.nbt.CompoundTag nmsTag = new net.minecraft.nbt.CompoundTag();
    var nmsItem = CraftItemStack.asNMSCopy(item);
    nmsItem.save(nmsTag);
    return TagTranslators.COMPOUND.toApiType(nmsTag);
  }

  @Override
  public void saveItem(CompoundTag tag, ItemStack item) {
    tag.merge(saveItem(item));
  }

  @Override
  public ItemStack loadItem(CompoundTag tag) {
    net.minecraft.nbt.CompoundTag nmsTag
        = TagTranslators.COMPOUND.toMinecraft(tag);

    var nmsItem = net.minecraft.world.item.ItemStack.of(nmsTag);
    return CraftItemStack.asBukkitCopy(nmsItem);
  }

  @Override
  public CompoundTag getUnhandledTags(ItemMeta meta) {
    Objects.requireNonNull(meta, "ItemMeta");

    try {
      Map<String, Tag> tags = (Map<String, Tag>) unhandledTags.get(meta);

      CompoundTag result = BinaryTags.compoundTag();
      tags.forEach((s, tag) -> {
        result.put(s, TagTranslators.toApi(tag));
      });

      return result;
    } catch (ReflectiveOperationException exc) {
      throw new IllegalStateException(exc);
    }
  }

  @Override
  public void setUnhandledTags(ItemMeta meta, CompoundTag tag) {
    Objects.requireNonNull(meta, "ItemMeta");

    try {
      Map<String, Tag> tags = (Map<String, Tag>) unhandledTags.get(meta);
      tags.clear();
      tags.putAll(tags);
    } catch (ReflectiveOperationException exc) {
      throw new IllegalStateException(exc);
    }
  }
}