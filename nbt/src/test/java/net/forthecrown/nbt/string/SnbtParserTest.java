package net.forthecrown.nbt.string;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import net.forthecrown.nbt.StringTag;
import org.junit.jupiter.api.Test;

public class SnbtParserTest {
  @Test
  void parseString_escaped() {
    StringTag tag = assertDoesNotThrow(() -> {
      return Snbt.parse("'a_string\\'escaped quote'").asString();
    });

    assertEquals(tag.value(), "a_string'escaped quote");
  }

  @Test
  void parseCompound() {
    var tag = assertDoesNotThrow(() -> {
      return Snbt.parseCompound("{tag_1:'foo',tag_2:'bar'}");
    });

    assertEquals(tag.size(), 2);
    assertEquals(tag.getString("tag_1"), "foo");
    assertEquals(tag.getString("tag_2"), "bar");
  }

  @Test
  void parseEmptyCompound() {
    var tag = assertDoesNotThrow(() -> {
      return Snbt.parseCompound("{}");
    });

    assertEquals(tag.size(), 0);
  }
}