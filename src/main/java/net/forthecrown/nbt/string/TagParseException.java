package net.forthecrown.nbt.string;

public class TagParseException extends RuntimeException {
  private String context;
  private int parseOffset;

  public TagParseException(String message) {
    super(message);
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public int getParseOffset() {
    return parseOffset;
  }

  public void setParseOffset(int parseOffset) {
    this.parseOffset = parseOffset;
  }

  @Override
  public String getMessage() {
    var superMessage = super.getMessage();

    if (context != null) {
      return superMessage + ", at " + parseOffset + "\n" + context + "<- here";
    }

    return superMessage;
  }
}