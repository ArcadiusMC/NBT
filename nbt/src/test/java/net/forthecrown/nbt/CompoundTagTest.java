package net.forthecrown.nbt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.forthecrown.nbt.string.Snbt;
import org.junit.jupiter.api.Test;

public class CompoundTagTest {
  @Test
  void testMerge() {
    CompoundTag tag1 = Snbt.parseCompound("{obj_1:{val_1:'foo'},obj_2:'bar'}");
    CompoundTag tag2 = Snbt.parseCompound("{obj_1:{val_2:12},obj_2:'foobar'}");

    tag1.merge(tag2);

    assertEquals(tag1.getString("obj_2"), "foobar");
    assertEquals(tag1.getCompound("obj_1").size(), 2);
  }
}