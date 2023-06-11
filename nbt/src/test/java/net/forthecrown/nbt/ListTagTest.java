package net.forthecrown.nbt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ListTagTest {
  @Test
  void addTest() {
    ListTag stringList = BinaryTags.stringList("string1", "string2");
    assertEquals(stringList.size(), 2);

    assertTrue(stringList.add(BinaryTags.stringTag("string3")));
    assertEquals(stringList.size(), 3);

    assertFalse(stringList.add(BinaryTags.intTag(12)));

    String str = stringList.getString(1);
    assertNotNull(str);
    assertNull(stringList.getDoubleArray(1));
  }
}