package net.forthecrown.nbt.paper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.TagTypes;
import net.forthecrown.nbt.string.Snbt;
import net.minecraft.nbt.Tag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.UnsafeValues;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
class ItemNbtProviderImpl implements ItemNbtProvider {
  static ItemNbtProviderImpl INSTANCE = new ItemNbtProviderImpl();

  private static final Class<?> metaClass;
  private static final Field unhandledTags;
  private static final MethodHandle applyToItem;

  static {
    try {
      metaClass = Bukkit.getItemFactory()
          .getItemMeta(Material.TORCH)
          .getClass();

      unhandledTags = metaClass.getDeclaredField("unhandledTags");
      unhandledTags.setAccessible(true);

      var mApplyToItem = metaClass.getDeclaredMethod("applyToItem", net.minecraft.nbt.CompoundTag.class);
      mApplyToItem.setAccessible(true);

      applyToItem = MethodHandles.privateLookupIn(metaClass, MethodHandles.lookup())
          .unreflect(mApplyToItem);

    } catch (ReflectiveOperationException exc) {
      throw new IllegalStateException(exc);
    }
  }

  private CompoundTag fromArray(byte[] arr) {
    try {
      return BinaryTags.readCompressed(new ByteArrayInputStream(arr));
    } catch (IOException exc) {
      throw new RuntimeException(exc);
    }
  }

  private byte[] toArray(CompoundTag tag) {
    try {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      BinaryTags.writeCompressed(stream, tag);
      return stream.toByteArray();
    } catch (IOException exc) {
      throw new RuntimeException(exc);
    }
  }

  @Override
  public CompoundTag saveItem(ItemStack item) {
    byte[] saved = Bukkit.getUnsafe().serializeItem(item);
    return fromArray(saved);
  }

  @Override
  public void saveItem(CompoundTag tag, ItemStack item) {
    tag.merge(saveItem(item));
  }

  @Override
  public ItemStack loadItem(CompoundTag tag) {
    UnsafeValues unsafe = Bukkit.getUnsafe();

    // Assume item is up-to-date if DataVersion tag is missing
    if (!tag.contains("DataVersion", TagTypes.intType())) {
      // Do not modify input directly
      tag = tag.copy();
      tag.putInt("DataVersion", unsafe.getDataVersion());
    }

    byte[] arr = toArray(tag);
    return Bukkit.getUnsafe().deserializeItem(arr);
  }

  @Override
  public CompoundTag getUnhandledTags(ItemMeta meta) {
    Objects.requireNonNull(meta, "ItemMeta");

    try {
      @SuppressWarnings("unchecked") // The field is a Map<String, Tag>
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
      @SuppressWarnings("unchecked") // The field is a Map<String, Tag>
      Map<String, Tag> tags = (Map<String, Tag>) unhandledTags.get(meta);
      tags.clear();
      tags.putAll(TagTranslators.COMPOUND.toMinecraft(tag).tags);
    } catch (ReflectiveOperationException exc) {
      throw new IllegalStateException(exc);
    }
  }

  @Override
  public CompoundTag saveMeta(ItemMeta meta) {
    net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();

    try {
      applyToItem.invoke(meta, tag);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }

    return TagTranslators.COMPOUND.toApiType(tag);
  }

  @Override
  public ItemMeta loadMeta(CompoundTag tag, Material metaType) {
    // Yeah, I gave up here, this is easier than finding the item-meta class of
    // the material and reflectively calling its constructor with a vanilla
    // CompoundTag
    ItemStack item = new ItemStack(metaType, 1);
    item = Bukkit.getUnsafe().modifyItemStack(item, Snbt.toString(tag));
    return item.getItemMeta();
  }
}