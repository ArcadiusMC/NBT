package net.forthecrown.nbt;

import java.io.DataOutput;
import java.io.IOException;
import net.forthecrown.nbt.io.ScopedDataInput;
import org.jetbrains.annotations.NotNull;

class EndTagImpl implements EndTag {
  static final TagType<EndTag> TYPE = new TagType<>() {
    @Override
    public void write(EndTag tag, DataOutput output) throws IOException {
    }

    @Override
    public EndTag read(ScopedDataInput input) throws IOException {
      return INSTANCE;
    }

    @Override
    public byte getId() {
      return TypeIds.END;
    }

    @Override
    public String getName() {
      return "TAG_End";
    }
  };

  static final EndTagImpl INSTANCE = new EndTagImpl();

  private EndTagImpl() {
  }

  @Override
  public @NotNull TagType<? extends BinaryTag> getType() {
    return TYPE;
  }

  @Override
  public String toString() {
    return getType().getName();
  }
}