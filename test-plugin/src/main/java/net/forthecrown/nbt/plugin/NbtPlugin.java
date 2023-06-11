package net.forthecrown.nbt.plugin;

import com.google.common.base.Preconditions;
import java.util.Objects;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.paper.ItemNbtProvider;
import net.forthecrown.nbt.paper.PaperNbt;
import net.forthecrown.nbt.string.Snbt;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class NbtPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    CompoundTag tag = BinaryTags.compoundTag();
    tag.putInt("CustomModelData", 12);

    ItemMeta meta = ItemNbtProvider.provider().loadMeta(tag, Material.STONE);

    Validate.isTrue(
        meta.hasCustomModelData() && meta.getCustomModelData() == 12,
        "Mismatch between model data"
    );

    getSLF4JLogger().debug("meta.customModelData={}, tag={}",
        meta.getCustomModelData(),
        Snbt.toString(tag, true, false)
    );

    CompoundTag saved = ItemNbtProvider.provider().saveMeta(meta);
    Validate.isTrue(
        saved.getInt("CustomModelData") == 12,
        "Invalid model data in saved"
    );

    testItemLoad();
    testGeneral();
    testDataContainer();
  }

  void testItemLoad() {
    CompoundTag tag = BinaryTags.compoundTag();
    tag.putString("id", "minecraft:red_banner");
    tag.putInt("Count", 12);

    CompoundTag itemTag = BinaryTags.compoundTag();
    itemTag.putInt("test_value", 12);
    itemTag.putInt("CustomModelData", 13);

    tag.put("tag", itemTag);

    ItemStack loaded = PaperNbt.loadItem(tag);

    Validate.isTrue(
        loaded.getAmount() == 12,
        "Amount mismatch"
    );

    Validate.isTrue(
        loaded.getType() == Material.RED_BANNER,
        "Material mismatch"
    );

    var meta = loaded.getItemMeta();
    Validate.isTrue(
        meta.hasCustomModelData() && meta.getCustomModelData() == 13,
        "CustomModelData mismatch"
    );

    CompoundTag savedMeta = ItemNbtProvider.provider().saveMeta(meta);
    Validate.isTrue(
        savedMeta.getInt("test_value") == 12,
        "test_value mismatch"
    );
  }

  void testGeneral() {
    Material material = Material.STONE_STAIRS;
    var data = material.createBlockData();

    // Shouldn't throw any exceptions
    CompoundTag tag = PaperNbt.saveBlockData(data);
    BlockData loaded = PaperNbt.loadBlockData(tag);
  }

  void testDataContainer() {
    ItemStack item = new ItemStack(Material.STONE, 1);
    ItemMeta meta = item.getItemMeta();

    PersistentDataContainer container = meta.getPersistentDataContainer();
    PersistentDataAdapterContext ctx = container.getAdapterContext();

    container.set(NamespacedKey.fromString("test:test_value"), PersistentDataType.INTEGER, 12);

    // If everything is good, this shouldn't throw exceptions
    CompoundTag tag = PaperNbt.fromDataContainer(container);
    PersistentDataContainer container1 = PaperNbt.toDataContainer(tag, ctx);

    Preconditions.checkState(
        Objects.equals(container1, container),

        "Container objects not equal! container=%s, container1=%s",
        container, container1
    );
  }

  @Override
  public void onDisable() {

  }
}