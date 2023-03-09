package net.forthecrown.nbt.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.forthecrown.nbt.BinaryTag;
import org.jetbrains.annotations.NotNull;

class TagPathImpl implements TagPath, Iterable<Node> {

  private final Node[] nodes;
  private String input;

  TagPathImpl(Node[] nodes) {
    this.nodes = nodes;
  }

  @NotNull
  @Override
  public Iter iterator() {
    return new Iter();
  }

  @Override
  public @NotNull List<BinaryTag> get(@NotNull BinaryTag tag) {
    Objects.requireNonNull(tag);

    List<BinaryTag> tags = new ArrayList<>();
    tags.add(tag);

    for (Node node: this) {
      tags = node.get(tags, null);

      if (tags.isEmpty()) {
        return Collections.emptyList();
      }
    }

    return tags;
  }

  @Override
  public int remove(@NotNull BinaryTag tag) {
    Objects.requireNonNull(tag);

    List<BinaryTag> tags = new ArrayList<>();
    tags.add(tag);

    var it = iterator();

    while (it.hasNext()) {
      var n = it.next();

      if (it.hasNext()) {
        tags = n.get(tags, null);
      } else {
        return n.remove(tags);
      }
    }

    return 0;
  }

  @Override
  public int set(@NotNull BinaryTag tag, @NotNull Supplier<BinaryTag> supplier) {
    Objects.requireNonNull(tag);
    Objects.requireNonNull(supplier);

    List<BinaryTag> tags = new ArrayList<>();
    tags.add(tag);

    var it = iterator();

    while (it.hasNext()) {
      var n = it.next();

      if (it.hasNext()) {
        var next = it.next();
        it.index--;

        tags = n.get(tags, next::createParent);
      } else {
        return n.set(tags, supplier);
      }
    }

    return 0;
  }

  @Override
  public @NotNull String getInput() {
    if (input != null) {
      return input;
    }

    StringBuilder builder = new StringBuilder();
    boolean first = true;

    for (Node n : this) {
      n.appendInput(first, builder);

      if (!(n instanceof RootNode)) {
        first = false;
      }
    }

    return input = builder.toString();
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("/");

    for (var n: this) {
      joiner.add(n.toString());
    }

    return joiner.toString();
  }

  class Iter implements Iterator<Node> {
    int index = 0;

    @Override
    public boolean hasNext() {
      return index < nodes.length;
    }

    @Override
    public Node next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      return nodes[index++];
    }
  }

  static class BuilderImpl implements Builder {

    private Predicate<BinaryTag> rootFilter;
    private final List<Node> nodes = new ArrayList<>();

    @Override
    public Builder setRootFilter(Predicate<BinaryTag> filter) {
      rootFilter = filter;
      return this;
    }

    @Override
    public Builder addIndexNode(int index) {
      return addNode(new IndexNode(index));
    }

    @Override
    public Builder addMatchAll(Predicate<BinaryTag> filter) {
      return addNode(new MatchAllNode(filter));
    }

    @Override
    public Builder addObjectNode(String name) {
      return addNode(new ObjectNode(name, null));
    }

    @Override
    public Builder addObjectNode(String name, Predicate<BinaryTag> filter) {
      return addNode(new ObjectNode(name, filter));
    }

    @Override
    public Builder addMatchAll() {
      return addNode(new MatchAllNode(null));
    }

    public Builder addNode(Node node) {
      nodes.add(node);
      return this;
    }

    @Override
    public TagPath build() {
      int size = this.nodes.size() + 1;
      Node[] nodes = new Node[size];
      nodes[0] = new RootNode(rootFilter);

      for (int i = 1; i < size; i++) {
        nodes[i] = this.nodes.get(i-1);
      }

      return new TagPathImpl(nodes);
    }
  }
}