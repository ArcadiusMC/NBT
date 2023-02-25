package net.forthecrown.nbt.path;

public class PathParseException extends RuntimeException {

  private String context;
  private int position;

  public PathParseException(String message) {
    super(message);
  }

  public PathParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public PathParseException(Throwable cause) {
    super(cause);
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public int getPosition() {
    return position;
  }

  public String getContext() {
    return context;
  }

  @Override
  public String getMessage() {
    return super.getMessage()
        + (context != null ? ", at (" + position + "): '" + context + "'<-" : "");
  }
}