package net.forthecrown.nbt.io;

import java.io.DataInput;
import java.io.IOException;

/**
 * A data input which tracks 2 extra variables.
 * <p>
 * The first being the amount of bytes read from the input. This is done to
 * prevent reading data outside the {@link #maxBytes()} range.
 * <p>
 * The second variable is the 'tag depth'. Tag depth is the measure of how many
 * layers deep inside nested lists/compounds the reader currently is. This depth
 * cannot surpass {@link #MAX_DEPTH}
 */
public interface ScopedDataInput extends DataInput {

  /**
   * Maximum tag depth, taken from minecraft's own limit
   */
  int MAX_DEPTH = 512;

  /**
   * Increases tag depth and performs a depth check
   * @throws IOException If the depth check fails
   */
  void enterScope() throws IOException;

  /**
   * Decreases tag depth
   */
  void endScope();

  /**
   * Gets the current tag depth
   * @return Current tag depth
   */
  int depth();

  /**
   * Amount of written bytes
   * @return Written bytes
   */
  long accountedBytes();

  /**
   * Gets the maximum bytes that can be read
   * @return Max readable bytes, 0 or less, if no limit
   */
  long maxBytes();
}