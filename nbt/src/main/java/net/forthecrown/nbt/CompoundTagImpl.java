package net.forthecrown.nbt;

import static net.forthecrown.nbt.TypeIds.END;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CompoundTagImpl
    extends Object2ObjectOpenHashMap<String, BinaryTag>
    implements CompoundTag
{
  public static final TagType<CompoundTag> TYPE = new TagType<>() {
    @Override
    public void write(CompoundTag tag, DataOutput output) throws IOException {
      var entries = tag.entrySet();
      for (var e: entries) {
        BinaryTags.writeNamedTag(e, output);
      }

      output.writeByte(END);
    }

    @Override
    public CompoundTag read(ScopedDataInput input) throws IOException {
      CompoundTag tag = new CompoundTagImpl();
      input.enterScope();

      byte typeId;

      while ((typeId = input.readByte()) != END) {
        var entry = BinaryTags.readNamedTag(typeId, input);
        tag.put(entry.getKey(), entry.getValue());
      }

      input.endScope();
      return tag;
    }

    @Override
    public void skip(ScopedDataInput input) throws IOException {
      byte typeId;

      while ((typeId = input.readByte()) != END) {
        StringTagImpl.skipString(input);

        var type = TagTypes.getType(typeId);
        type.skip(input);
      }
    }

    @Override
    public byte getId() {
      return TypeIds.COMPOUND;
    }

    @Override
    public String getName() {
      return "TAG_Compound";
    }
  };

  public CompoundTagImpl() {
  }

  public CompoundTagImpl(int expected) {
    super(expected);
  }

  public CompoundTagImpl(Map<? extends String, ? extends BinaryTag> m) {
    super(m);
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public CompoundTag merge(CompoundTag source) {
    for (var e: source.entrySet()) {
      if (e.getValue() instanceof CompoundTag compoundTag) {
        CompoundTag ours = getCompound(e.getKey());
        ours.merge(compoundTag);

        put(e.getKey(), ours);
        continue;
      }

      put(e.getKey(), e.getValue());
    }

    return this;
  }

  @Override
  public <T extends BinaryTag> @Nullable T get(String name, TagType<T> type) {
    BinaryTag tag = get(name);
    return tag == null || tag.getId() != type.getId() ? null : (T) tag;
  }

  @Override
  public CompoundTag copy() {
    CompoundTag result = new CompoundTagImpl(size());

    for (var e: entrySet()) {
      result.put(e.getKey(), e.getValue().copy());
    }

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CompoundTag)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  public String toString() {
    return toNbtString();
  }
}