package net.forthecrown.nbt.string;

import static org.junit.jupiter.api.Assertions.*;

import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.IntArrayTag;
import org.junit.jupiter.api.Test;

class SnbtTest {

  @Test
  void testToString() {
    CompoundTag tag = BinaryTags.compoundTag();
    tag.putInt("key_1", 98798);
    tag.putBoolean("key_2", true);
    tag.putIntArray("x_y_z", 1, 2, 3);

    String s = Snbt.toString(tag);
    assertEquals(s, "{key_2:1b,key_1:98798,x_y_z:[I;1,2,3]}");
  }

  @Test
  void testParse() {
    CompoundTag tag = BinaryTags.compoundTag();
    tag.putInt("key_1", 98798);
    tag.putBoolean("key_2", true);
    tag.putIntArray("x_y_z", 1, 2, 3);
    tag.putLongArray("longs", 1L, 12131L, 34235L, -1564L);
    tag.putBoolean("boolean", true);
    tag.put("strings", BinaryTags.stringList("string_1", "string_2"));
    tag.put("empty_list", BinaryTags.listTag());
    tag.putString("escaped_string", "String\"Escaped");
    tag.putString("different_escape", "aaa\\bbb");
    tag.putDouble("double", 1.232355342D);

    CompoundTag nested = BinaryTags.compoundTag();
    nested.putDouble("x", 30498.98789);
    nested.putDouble("y", .98789);
    nested.putDouble("z", +30498.98789);

    tag.put("another_compound", nested);

    String nbtString = Snbt.toString(tag, true, true);

    System.out.println("nbtString:");
    System.out.println(nbtString);

    CompoundTag parsed = assertDoesNotThrow(() -> {
      return Snbt.parseCompound(nbtString);
    });

    System.out.println("Parsed:");
    System.out.println(Snbt.toString(parsed, true, true));

    assertTrue(BinaryTags.compareTags(tag, parsed, true));
  }

  @Test
  void testCustomString() {
    String toParse =
        """
        {
          "string_key": "a string",
          "no_leading_zero": .76980,
          "no_trailing": 8798.,
          "notation": 7.78e10,
          "int_array": [I;1926987268,-428392111,-1786688212,-1396749916]
        }
        """;

    var parsed = assertDoesNotThrow(() -> {
      return Snbt.parse(toParse);
    });

    int[] arr = parsed.asCompound().getIntArray("int_array");
    assertEquals(arr.length, 4);
    assertEquals(arr[0], 1926987268);
    assertEquals(arr[1], -428392111);
    assertEquals(arr[2], -1786688212);
    assertEquals(arr[3], -1396749916);

    System.out.println(Snbt.toString(parsed, true, true));
  }

  @Test
  void parseUUID() {
    BinaryTag tag = assertDoesNotThrow(() -> {
      return Snbt.parse("[I;1926987268,-428392111,-1786688212,-1396749916]");
    });

    var tagArray = assertInstanceOf(IntArrayTag.class, tag);
    var arr = tagArray.toIntArray();

    assertEquals(arr.length, 4);
    assertEquals(arr[0], 1926987268);
    assertEquals(arr[1], -428392111);
    assertEquals(arr[2], -1786688212);
    assertEquals(arr[3], -1396749916);
  }
}