package net.forthecrown.nbt.paper;

import net.forthecrown.nbt.CompoundTag;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;

public interface PaperNbtProvider {

  static PaperNbtProvider provider() {
    return PaperNbtProviderImpl.INSTANCE;
  }

  CompoundTag saveBlockEntity(TileState state);

  void loadBlockEntity(TileState state, CompoundTag tag);

  CompoundTag saveBlockData(BlockData data);

  BlockData loadBlockData(CompoundTag tag);


  CompoundTag saveEntity(Entity entity);

  void loadEntity(Entity entity, CompoundTag tag);

  CompoundTag fromDataContainer(PersistentDataContainer container);

  PersistentDataContainer toDataContainer(
      CompoundTag tag,
      PersistentDataAdapterContext context
  );

  void mergeToContainer(PersistentDataContainer container, CompoundTag tag);
}