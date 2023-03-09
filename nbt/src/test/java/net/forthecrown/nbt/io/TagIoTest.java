package net.forthecrown.nbt.io;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import net.forthecrown.nbt.string.Snbt;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import org.junit.jupiter.api.Test;

class TagIoTest {

  @Test
  void writeCompressed() {
    CompoundTag beforeWrite = BinaryTags.compoundTag();
    beforeWrite.putString("a_string", "Hello, world!");
    beforeWrite.putInt("an_integer", 8979);
    beforeWrite.putIntArray("some_bytes", 12, 12, 12, 12);

    ByteArrayOutputStream out1 = new ByteArrayOutputStream();
    assertDoesNotThrow(() -> {
      BinaryTags.writeCompressed(out1, beforeWrite);
    });

    ByteArrayInputStream inputStream
        = new ByteArrayInputStream(out1.toByteArray());

    CompoundTag afterWrite = assertDoesNotThrow(() -> {
      return BinaryTags.readCompressed(inputStream);
    });

    System.out.println(Snbt.toString(beforeWrite, true, true));
    System.out.println(Snbt.toString(afterWrite, true, true));

    assertTrue(BinaryTags.compareTags(beforeWrite, afterWrite, false));
  }

  @Test
  void readCompressed() {
    InputStream input = Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream("level.dat");

    assertNotNull(input, "No level.dat present???");

    var tag = assertDoesNotThrow(() -> BinaryTags.readCompressed(input));
    System.out.println(Snbt.toString(tag, true, true));
  }
}