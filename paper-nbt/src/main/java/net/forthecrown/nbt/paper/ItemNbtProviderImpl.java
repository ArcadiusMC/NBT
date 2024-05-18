package net.forthecrown.nbt.paper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.TagTypes;
import net.forthecrown.nbt.paper.TagTranslators.TagTranslator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.UnsafeValues;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
class ItemNbtProviderImpl implements ItemNbtProvider {
  static final int PRE_COMPONENT_DATA_VERSION = 3700;

  static ItemNbtProviderImpl INSTANCE = new ItemNbtProviderImpl();

  private static final Class<?> metaClass;
  private static final Field customTag;

  static {
    try {
      metaClass = Bukkit.getItemFactory()
          .getItemMeta(Material.TORCH)
          .getClass();

      customTag = metaClass.getDeclaredField("customTag");
      customTag.setAccessible(true);
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

      int currentDataVersion = unsafe.getDataVersion();

      if (tag.contains("tag") && currentDataVersion > PRE_COMPONENT_DATA_VERSION) {
        tag.putInt("DataVersion", PRE_COMPONENT_DATA_VERSION);
      } else {
        tag.putInt("DataVersion", currentDataVersion);
      }
    }

    byte[] arr = toArray(tag);
    return Bukkit.getUnsafe().deserializeItem(arr);
  }

  @Override
  public CompoundTag getCustomData(ItemMeta meta) {
    Objects.requireNonNull(meta, "ItemMeta");

    try {
      net.minecraft.nbt.CompoundTag tags = (net.minecraft.nbt.CompoundTag) customTag.get(meta);

      if (tags == null) {
        return BinaryTags.compoundTag();
      }

      return TagTranslators.COMPOUND.toApiType(tags);
    } catch (ReflectiveOperationException exc) {
      throw new IllegalStateException(exc);
    }
  }

  @Override
  public void setCustomData(ItemMeta meta, CompoundTag tag) {
    Objects.requireNonNull(meta, "ItemMeta");

    try {
      customTag.set(meta, TagTranslators.COMPOUND.toMinecraft(tag));
    } catch (ReflectiveOperationException exc) {
      throw new IllegalStateException(exc);
    }
  }
}