package net.forthecrown.nbt.string;

import net.forthecrown.nbt.TagTypes;

record TagToken(Type type, String value, Number number) {

  enum Type {
    ARRAY_OPEN      (Tokens.ARRAY_START),
    ARRAY_CLOSE     (Tokens.ARRAY_END),
    BYTE_ARRAY_OPEN ("[B;"),
    INT_ARRAY_OPEN  ("[I;"),
    LONG_ARRAY_OPEN ("[L;"),

    COMPOUND_OPEN   (Tokens.COMPOUND_START),
    COMPOUND_CLOSE  (Tokens.COMPOUND_END),

    BYTE            (TagTypes.byteType().getName()),
    SHORT           (TagTypes.shortType().getName()),
    INT             (TagTypes.intType().getName()),
    LONG            (TagTypes.longType().getName()),
    FLOAT           (TagTypes.floatType().getName()),
    DOUBLE          (TagTypes.doubleType().getName()),

    STRING          (TagTypes.stringType().getName()),

    COMMA           (Tokens.COMMA),
    ASSIGNMENT      (Tokens.ASSIGNMENT),

    EOF             ("<EOF>"),
    ;

    private final String name;

    Type(String name) {
      this.name = name;
    }

    Type(char c) {
      this(Character.toString(c));
    }

    boolean isNumber() {
      return this == BYTE
          || this == SHORT
          || this == INT
          || this == LONG
          || this == FLOAT
          || this == DOUBLE;
    }

    TagToken token(String s) {
      return new TagToken(this, s, null);
    }

    TagToken token(Number number) {
      return new TagToken(this, "", number);
    }

    TagToken token() {
      return token("");
    }

    @Override
    public String toString() {
      return name.length() == 1 ? "'" + name + "'" : name;
    }
  }
}