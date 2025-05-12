package algochoochoo.graph;

import algochoochoo.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * (Literally) A graph representing transfer datas [GTFS]
 *
 * @author Bilal Vandenberge
 */
public class Graph {
  // #### Public method ####

  /**
   * Construct a new Graph object from GTFS datas
   *
   * @param path The path of the main GTFS directory
   * @param set The settings for graph construction
   */
  public Graph(String path, AlgoSettings set) {
    settings = set;
    Map<String, Stop> stops = Parser.stops(path);
    List<Trip> trips = Parser.trips(path, stops);
    BallTree tree = new BallTree(stops.values());
    build_nodes(stops, trips, tree);
  }

  /** Convert a Graph object to string */
  @Override
  public String toString() {
    return "Graph(#V: " + V_card() + ", #E: " + E_card() + ")";
  }

  // #### Getters ####

  /**
   * Get the cardinality of the edge set
   *
   * @return The number of edges
   */
  public int E_card() {
    return edge_count;
  }

  /**
   * Get the cardinality of the node set
   *
   * @return The number of nodes
   */
  public int V_card() {
    return vertices.size();
  }

  public List<Node> vertices() {
    return vertices;
  }

  public AlgoSettings settings() {
    return settings;
  }

  // #### Private helpers ####

  /**
   * From generic datas, construct the nodes of the graph
   *
   * @param stops The map {stop_id,stop_object}
   * @param trips The map {trip_id,trip_object}
   * @param tree The balltree containing the stops
   */
  private void build_nodes(
      Map<String, Stop> stops, List<Trip> trips, BallTree tree) {
    edge_count = 0;

    // 1. Create the nodes from the stops
    Map<String, Node> map = new LinkedHashMap<String, Node>(stops.size());
    vertices = new ArrayList<Node>(stops.size());
    for (Stop stop : stops.values()) {
      Node node = new Node(stop);
      node.set_index(vertices.size());
      vertices.add(node);
      map.put(stop.id(), node);
    }

    // 2. Populate connections from tips
    for (Trip trip : trips) {
      List<TripElement> list = trip.content();
      int n = list.size();
      if (n == 0)
        continue;
      edge_count += n - 1;
      TripElement current = list.get(0);
      for (int i = 1; i < n; ++i) {
        TripElement next = list.get(i);
        Edge connection = new Edge(current, next, map, trip);
        Node node = map.get(current.stop().id());
        node.add_connection(connection);
        current = next;
      }
    }

    // 3. Populate transfers
    int radius = settings.footpath_radius;
    for (Node node : vertices) {
      List<Edge> transfers = tree.radius_search(node.stop(), radius, map);
      edge_count += transfers.size();
      node.set_transfers(transfers);
    }
  }

  // #### Attributes ####

  private int edge_count = 0;
  private AlgoSettings settings;
  private List<Node> vertices;
}
