# NBT
NamedBinaryTag library for Java
  
## Writing and reading
Reading binary data:
```java
InputStream input = // Get an input stream
CompoundTag tag = BinaryTags.readCompressed(input);
int intValue = tag.getInt("key");
String string = tag.getString("other_key");
```
Writing binary data:
```java
CompoundTag tag = // Get a tag
OutputStream stream = // Create a stream
BinaryTags.writeCompressed(stream, tag);
```
SNBT parsing:
```java
import net.forthecrown.nbt.string.Snbt;

String input = // Get an SNBT input
BinaryTag tag = Snbt.parse(input);
```
SNBT writing:
```java
BinaryTag tag = // Get a tag
String output = Snbt.toString(tag);
```
## Tag Paths
Minecraft wiki: https://minecraft.fandom.com/wiki/NBT_path_format  
NBT paths can be created and used like so:
```java
import net.forthecrown.nbt.path.TagPath

TagPath path = TagPath.parse("foo.bar[]");
BinaryTag tag = // Get a tag

List<BinaryTag> results = path.get(tag);
int removed = path.remove(tag);
int changed = path.set(tag, BinaryTags.stringTag("Hello, world!"));
```
Paths can also be created programmatically with `TagPath.builder()`, the above shown example would look like so:
```java
TagPath path = TagPath.builder()
	.addObjectNode("foo")
	.addObjectNode("bar")
	.addMatchAll()
	.build();
```
