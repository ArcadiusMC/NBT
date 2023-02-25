package net.forthecrown.nbt.path;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PathParserTest {

  @Test
  void parse() {
    String input = "{foo:1b}node1.node2.node3[].node4[1].node5{foo:'bar'}";
    assertDoesNotThrow(() -> TagPath.parse(input));

    String failingString = "node1..node2";
    assertThrows(PathParseException.class, () -> {
      TagPath.parse(failingString);
    });
  }
}