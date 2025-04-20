import java.util.ArrayList;

/**
 * (Literally) A (directed) graph node
 *
 * @author Bilal Vandenberge
 */
public class Node {
  // #### Public methods ####

  public Node(Stop stop) {
    stop = stop;
    edges = new ArrayList<>();
  }

  /** Convert a Node object to string */
  @Override
  public String toString() {
    return "Node(" + stop.id() + ", " + edges.size() + " edges)";
  }

  public void add(Edge edge) {
    edges.add(edge);
  }

  // #### Getters ####

  public Stop stop() {
    return stop;
  }

  public ArrayList<Edge> edges() {
    return edges;
  }

  // #### Attributes ####

  Stop stop;
  ArrayList<Edge> edges;
}