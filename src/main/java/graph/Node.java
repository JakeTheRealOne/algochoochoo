import java.util.ArrayList;
import java.util.List;

/** (Literally) A graph node */
public class Node {
  // #### Public methods ####

  /**
   * Construct a new Node object
   *
   * @param s The stop of the node
   */
  Node(Stop s) {
    stop = s;
    init();
  }

  /** Initialize the node */
  public void init() {
    best_edge = null;
    best_cost = Integer.MAX_VALUE;
    transfers = new ArrayList<Edge>();
    connections = new ArrayList<Edge>();
  }

  // #### Getters ####

  /**
   * Get the stop
   *
   * @return The stop
   */
  public Stop stop() {
    return stop;
  }

  /**
   * Get the edge leading to the invovled stop that is in the best path
   *
   * @return The best edge leading to the stop
   */
  public Edge best_edge() {
    return best_edge;
  }

  /**
   * Get the minimal cost of the best path leading to the stop
   *
   * @return The best cost
   */
  public int best_cost() {
    return best_cost;
  }

  /**
   * Get the transfers from the stop to a near stop
   *
   * @return The list of outgoing edges defined as transfers
   */
  public List<Edge> transfers() {
    return transfers;
  }

  /**
   * Get the connections from the stop to another stop
   *
   * @return The list of outgoing edges defined as connections
   */
  public List<Edge> connections() {
    return connections;
  }

  // #### Setters ####

  // public void new_best() {
  // TODO
  // }

  /**
   * Set the outgoing transfers of the node
   *
   * @param t The new transfers
   */
  public void set_transfers(List<Edge> t) {
    transfers = t;
  }

  /**
   * Add a connection to the current list of connections of the stop
   *
   * @param c The new connection
   */
  public void add_connection(Edge c) {
    connections.add(c);
  }

  // #### Attributes ####

  Stop stop;
  Edge best_edge;
  int best_cost;
  List<Edge> connections;
  List<Edge> transfers;
}
