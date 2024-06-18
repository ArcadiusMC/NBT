package net.forthecrown.nbt.plugin;

import com.google.common.base.Preconditions;
import java.util.Objects;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
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
import org.slf4j.Logger;

public class NbtPlugin extends JavaPlugin {

  private Logger LOGGER;

  @Override
  public void onEnable() {
    LOGGER = getSLF4JLogger();

    testItemLoad();
    testGeneral();
    testDataContainer();
    testItemCustomData();
    testItemNoCustomData();
    testCommandStorage();
  }

  void testItemNoCustomData() {
    CompoundTag tag = BinaryTags.compoundTag();
    tag.putString("id", "minecraft:stone");
    tag.putInt("count", 1);

    ItemStack item = PaperNbt.loadItem(tag);
    ItemMeta meta = item.getItemMeta();

    CompoundTag customData = PaperNbt.getCustomData(meta);

    Validate.isTrue(customData.isEmpty(), "How is there any custom data???");
  }

  void testItemCustomData() {
    CompoundTag tag = BinaryTags.compoundTag();
    tag.putString("id", "minecraft:stone");
    tag.putInt("count", 1);

    CompoundTag components = BinaryTags.compoundTag();
    CompoundTag customData = BinaryTags.compoundTag();
    customData.putBoolean("this_is_true", true);

    components.put("minecraft:custom_data", customData);
    tag.put("components", components);

    ItemStack loaded = PaperNbt.loadItem(tag);
    ItemMeta meta = loaded.getItemMeta();

    CompoundTag gottenData = PaperNbt.getCustomData(meta);

    Validate.isTrue(gottenData.contains("this_is_true"), "this_is_true not found");
    Validate.isTrue(gottenData.getBoolean("this_is_true"), "wrong value gotten :(");

    LOGGER.debug("testItemCustomData");
    LOGGER.debug("tag={}", Snbt.toString(tag, true, true));
    LOGGER.debug("gottenData={}", Snbt.toString(gottenData, true, true));
    LOGGER.debug("---");
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

  void testCommandStorage() {
    NamespacedKey key = new NamespacedKey(this, "storage_test");

    CompoundTag tag = BinaryTags.compoundTag();
    tag.putString("str", "Hello, world!");
    tag.putInt("an_int", 23245);

    PaperNbt.setStoredData(key, tag);

    CompoundTag gotten = PaperNbt.getStoredData(key);

    Preconditions.checkState(
        BinaryTags.compareTags(tag, gotten, true),

        "Not the same :( tag=%s, gotten=%s",
        tag, gotten
    );
  }

  @Override
  public void onDisable() {

  }
}