package net.forthecrown.nbt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import org.junit.jupiter.api.Test;

public class UuidTest {
  @Test
  void test() {
    String strUUID = "72db7e04-e677-4151-9581-4d2cacbf49a4";
    UUID uuid = UUID.fromString(strUUID);

    IntArrayTag arr = BinaryTags.saveUuid(uuid);
    UUID loaded = BinaryTags.loadUuid(arr);

    assertEquals(uuid, loaded);
  }
}