package net.forthecrown.nbt.paper;

import java.lang.invoke.MethodHandle;
import java.util.Objects;
import net.forthecrown.nbt.CompoundTag;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

class PaperNbtProviderImpl implements PaperNbtProvider {
  static final PaperNbtProviderImpl INSTANCE = new PaperNbtProviderImpl();

  static final HolderLookup.Provider LOOKUP_PROVIDER
      = DedicatedServer.getServer().registryAccess();

  @Override
  public CompoundTag saveBlockEntity(TileState state) {
    CraftBlockEntityState craft = (CraftBlockEntityState) state;

    net.minecraft.nbt.CompoundTag tag
        = craft.getTileEntity().saveWithFullMetadata(LOOKUP_PROVIDER);

    return TagTranslators.COMPOUND.toApiType(tag);
  }

  @Override
  public void loadBlockEntity(TileState state, CompoundTag tag) {
    CraftBlockEntityState craft = (CraftBlockEntityState) state;
    BlockEntity tile = craft.getTileEntity();

    tile.loadWithComponents(TagTranslators.COMPOUND.toMinecraft(tag), LOOKUP_PROVIDER);
  }

  @Override
  public CompoundTag saveBlockData(BlockData data) {
    var tag = NbtUtils.writeBlockState(((CraftBlockData) data).getState());
    return TagTranslators.COMPOUND.toApiType(tag);
  }

  @Override
  public BlockData loadBlockData(CompoundTag tag) {
    net.minecraft.nbt.CompoundTag nms = TagTranslators.COMPOUND.toMinecraft(tag);
    var state = NbtUtils.readBlockState(
        BuiltInRegistries.BLOCK,
        nms
    );

    return state.createCraftBlockData();
  }

  @Override
  public CompoundTag saveEntity(Entity entity) {
    net.minecraft.world.entity.Entity nms = ((CraftEntity) entity).getHandle();
    net.minecraft.nbt.CompoundTag nmsTag = new net.minecraft.nbt.CompoundTag();
    nms.saveWithoutId(nmsTag);
    return TagTranslators.COMPOUND.toApiType(nmsTag);
  }

  @Override
  public void loadEntity(Entity entity, CompoundTag tag) {
    net.minecraft.nbt.CompoundTag nms = TagTranslators.COMPOUND.toMinecraft(tag);
    net.minecraft.world.entity.Entity entity1 = ((CraftEntity) entity).getHandle();
    entity1.load(nms);
  }

  @Override
  public CompoundTag fromDataContainer(PersistentDataContainer container) {
    CraftPersistentDataContainer craft = (CraftPersistentDataContainer) container;
    net.minecraft.nbt.CompoundTag vanillaTag = craft.toTagCompound();
    return TagTranslators.COMPOUND.toApiType(vanillaTag);
  }

  @Override
  public PersistentDataContainer toDataContainer(
      CompoundTag tag,
      PersistentDataAdapterContext context
  ) {
    PersistentDataContainer container = context.newPersistentDataContainer();
    mergeToContainer(container, tag);
    return container;
  }

  @Override
  public void mergeToContainer(PersistentDataContainer container, CompoundTag tag) {
    CraftPersistentDataContainer craft = (CraftPersistentDataContainer) container;
    craft.putAll(TagTranslators.COMPOUND.toMinecraft(tag));
  }

  @Override
  public CompoundTag getStoredData(@NotNull NamespacedKey key) {
    Objects.requireNonNull(key, "Null key");

    net.minecraft.nbt.CompoundTag nmsTag = DedicatedServer.getServer()
        .getCommandStorage()
        .get(CraftNamespacedKey.toMinecraft(key));

    return TagTranslators.COMPOUND.toApiType(nmsTag);
  }

  @Override
  public void setStoredData(@NotNull NamespacedKey k, @NotNull CompoundTag tag) {
    Objects.requireNonNull(k, "Null key");
    Objects.requireNonNull(tag, "Null tag");

    net.minecraft.nbt.CompoundTag nmsTag = TagTranslators.COMPOUND.toMinecraft(tag);

    DedicatedServer.getServer()
        .getCommandStorage()
        .set(CraftNamespacedKey.toMinecraft(k), nmsTag);
  }
}