package net.forthecrown.nbt.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.ByteTag;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.IntArrayTag;
import net.forthecrown.nbt.IntTag;
import net.forthecrown.nbt.ListTag;
import net.forthecrown.nbt.StringTag;
import org.junit.jupiter.api.Test;

class TagPathTest {
  static final String HELLO_WORLD = "Hello, world!";
  static final ByteTag SET_VALUE = BinaryTags.byteTag(1);

  static CompoundTag createTestTag() {
    CompoundTag t1 = BinaryTags.compoundTag();
    CompoundTag t2 = BinaryTags.compoundTag();

    t1.put("obj", t2);

    t2.putString("string", HELLO_WORLD);
    t1.putInt("an_int", 1000);

    ListTag strings = BinaryTags.stringList("Foo", "Bar", "Foobar");
    IntArrayTag intArr = BinaryTags.intArrayTag(1, 2, 3, 4, 5, 6);

    t2.put("integers", intArr);
    t2.putLong("a_long", 98709987897698L);
    t2.putString("string1", "String");

    t1.put("string_list", strings);
    t1.putFloat("float", 1.0564f);

    return t1;
  }

  @Test
  void get() {
    var testTag = createTestTag();

    TagPath anIntPath = TagPath.parse("an_int");
    TagPath stringPath = TagPath.parse("obj.string");

    var intList = anIntPath.get(testTag);
    assertEquals(1, intList.size());
    IntTag tag = assertInstanceOf(IntTag.class, intList.get(0));
    assertEquals(tag.intValue(), 1000);

    var stringList = stringPath.get(testTag);
    assertEquals(stringList.size(), 1);
    StringTag stringTag = assertInstanceOf(StringTag.class, stringList.get(0));
    assertEquals(stringTag.value(), HELLO_WORLD);

    TagPath noReturnPath = TagPath.parse("obj.not_real");
    var list = noReturnPath.get(testTag);
    assertTrue(list.isEmpty());
  }

  @Test
  void get_objectFiltering() {
    var tag = createTestTag();

    TagPath success = TagPath.parse("{an_int:1000}obj.a_long");
    TagPath failure = TagPath.parse("{an_int:0999}obj.a_long");

    var successList = success.get(tag);
    var failureList = failure.get(tag);

    assertEquals(successList.size(), 1);
    assertTrue(failureList.isEmpty());
  }

  @Test
  void get_indexBased() {
    var tag = createTestTag();

    TagPath success = TagPath.parse("string_list[0]");
    TagPath failure = TagPath.parse("string_list[40]");

    var successList = success.get(tag);
    var failureList = failure.get(tag);

    assertEquals(successList.size(), 1);
    assertTrue(failureList.isEmpty());
  }

  @Test
  void get_matchAll() {
    var tag = createTestTag();

    TagPath success = TagPath.parse("string_list[]");
    TagPath failure = TagPath.parse("obj.float[]");

    var successList = success.get(tag);
    var failureList = failure.get(tag);

    assertEquals(successList.size(), 3);
    assertTrue(failureList.isEmpty());
  }

  @Test
  void remove() {
    var tag = createTestTag();

    TagPath success = TagPath.parse("obj");
    TagPath failure = TagPath.parse("floats");

    int successCount = success.remove(tag);
    int failureCount = failure.remove(tag);

    assertEquals(successCount, 1);
    assertEquals(failureCount, 0);
  }

  @Test
  void remove_objectFiltering() {
    var tag = createTestTag();

    TagPath success = TagPath.parse("{an_int:1000}obj.a_long");
    TagPath failure = TagPath.parse("{an_int:0999}obj.a_long");

    var successCount = success.remove(tag);
    var failureCount = failure.remove(tag);

    assertTrue(successCount >= 1);
    assertEquals(0, failureCount);
  }

  @Test
  void remove_indexBased() {
    var tag = createTestTag();

    TagPath success = TagPath.parse("string_list[0]");
    TagPath failure = TagPath.parse("string_list[40]");

    var successCount = success.remove(tag);
    var failureCount = failure.remove(tag);

    assertTrue(successCount >= 1);
    assertEquals(0, failureCount);
  }

  @Test
  void remove_matchAll() {
    var tag = createTestTag();

    TagPath success = TagPath.parse("string_list[]");
    TagPath failure = TagPath.parse("obj.float[]");

    var successCount = success.remove(tag);
    var failureCount = failure.remove(tag);

    assertTrue(successCount >= 1);
    assertEquals(0, failureCount);
  }

  @Test
  void set() {
    var tag = createTestTag();

    TagPath success = TagPath.parse("obj");
    int successCount = success.set(tag, SET_VALUE);
    assertEquals(successCount, 1);
  }

  @Test
  void set_objectFiltering() {
    var tag = createTestTag();

    TagPath success = TagPath.parse("{an_int:1000}obj.a_long");
    TagPath failure = TagPath.parse("{an_int:0999}obj.a_long");

    var successCount = success.set(tag, SET_VALUE);
    var failureCount = failure.set(tag, SET_VALUE);

    assertTrue(successCount >= 1);
    assertEquals(0, failureCount);
  }

}