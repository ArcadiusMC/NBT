package net.forthecrown.nbt.paper;

import net.forthecrown.nbt.CompoundTag;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R2.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_19_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R2.persistence.CraftPersistentDataContainer;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;

class PaperNbtProviderImpl implements PaperNbtProvider {
  static final PaperNbtProviderImpl INSTANCE = new PaperNbtProviderImpl();

  private static <T> HolderGetter<T> lookup(
      ResourceKey<? extends Registry<? extends T>> key
  ) {
    return DedicatedServer.getServer()
        .registryAccess()
        .lookupOrThrow(key);
  }

  @Override
  public CompoundTag saveBlockEntity(TileState state) {
    CraftBlockEntityState craft = (CraftBlockEntityState) state;
    var tag = craft.getTileEntity().saveWithFullMetadata();
    return TagTranslators.COMPOUND.toApiType(tag);
  }

  @Override
  public void loadBlockEntity(TileState state, CompoundTag tag) {
    CraftBlockEntityState craft = (CraftBlockEntityState) state;
    craft.getTileEntity().load(TagTranslators.COMPOUND.toMinecraft(tag));
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
        lookup(Registries.BLOCK),
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
    CraftPersistentDataContainer c = (CraftPersistentDataContainer) container;
    return TagTranslators.COMPOUND.toApiType(c.toTagCompound());
  }

  @Override
  public PersistentDataContainer toDataContainer(
      CompoundTag tag,
      PersistentDataAdapterContext context
  ) {
    CraftPersistentDataContainer container
        = (CraftPersistentDataContainer) context.newPersistentDataContainer();

    container.putAll(TagTranslators.COMPOUND.toMinecraft(tag));
    return container;
  }
}