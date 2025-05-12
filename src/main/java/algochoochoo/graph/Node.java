package algochoochoo.graph;

import algochoochoo.*;

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
  public Node(Stop s) {
    stop = s;
    transfers = new ArrayList<Edge>();
    connections = new ArrayList<Edge>();
    init();
  }

  /** Convert a Node object to string */
  @Override
  public String toString() {
    return "Node(" + stop + ", " + transfers.size() + " transfers & "
        + connections.size() + " connections)";
  }

  /** Initialize the node */
  public void init() {
    source_flag = false;
    target_flag = false;
    best_edge = null;
    best_cost = Long.MAX_VALUE;
    best_time = Integer.MAX_VALUE;
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
  public long best_cost() {
    return best_cost;
  }

  /**
   * Get the earliest time arriving at the node in the Dijkstra algorithm
   *
   * @return The best time
   */
  public int best_time() {
    return best_time;
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

  /**
   * Get the index of the node in the list (for IndexFibonacciMinPQ)
   *
   * @return The index
   */
  public int index() {
    return index;
  }

  /**
   * Return if this node is the source of a path finding problem
   *
   * @return The source flag
   */
  public boolean is_source() {
    return source_flag;
  }

  /**
   * Return if this node is the target of a path finding problem
   *
   * @return The target flag
   */
  public boolean is_target() {
    return target_flag;
  }

  // #### Setters ####

  /**
   * Set a new best path to get to the stop
   *
   * @param cost The cost of the path
   * @param time The time at the end of the path
   * @param edge The last edge in the path
   */
  public void set_best(long cost, int time, Edge edge) {
    best_cost = cost;
    best_time = time;
    best_edge = edge;
  }

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

  /**
   * Set the index of the node in the list (for IndexFibonacciMinPQ)
   *
   * @param i The index
   */
  public void set_index(int i) {
    index = i;
  }

  /**
   * Declare this node as the source of a path finding problem
   */
  public void declare_source() {
    source_flag = true;
  }

  /**
   * Declare this node as the target of a path finding problem
   */
  public void declare_target() {
    target_flag = true;
  }

  // #### Attributes ####

  boolean source_flag;
  boolean target_flag;
  Stop stop;
  Edge best_edge;
  int index = -1;
  long best_cost;
  int best_time;

  long hahaTODO;

  List<Edge> connections;
  List<Edge> transfers;
}
