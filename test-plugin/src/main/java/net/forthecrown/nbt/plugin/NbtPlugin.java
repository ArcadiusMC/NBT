package net.forthecrown.nbt.plugin;

import com.google.common.base.Preconditions;
import java.util.Objects;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.paper.PaperNbt;
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
import org.slf4j.Logger;

public class NbtPlugin extends JavaPlugin {

  private Logger LOGGER;

  @Override
  public void onEnable() {
    LOGGER = getSLF4JLogger();

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

    LOGGER.debug("testItemLoad, tag={}", tag);

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

    LOGGER.debug("loaded={}", loaded);
    LOGGER.debug("resaved={}", PaperNbt.saveItem(loaded).toNbtString());
  }

  void testGeneral() {
    Material material = Material.STONE_STAIRS;
    var data = material.createBlockData();

    // Shouldn't throw any exceptions
    CompoundTag tag = PaperNbt.saveBlockData(data);
    BlockData loaded = PaperNbt.loadBlockData(tag);

    LOGGER.debug("testGeneral, data={} tag={}, loaded={}", data, tag, loaded);
    Validate.isTrue(loaded.equals(data), "Loaded block data mismatch");
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