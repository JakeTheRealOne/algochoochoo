package algochoochoo.graph;

import algochoochoo.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.index.strtree.STRtree;

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
    build_nodes(stops, trips);
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
      Map<String, Stop> stops, List<Trip> trips) {
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
    populate_transfers();
    System.err.println(this);
  }

  public void populate_transfers() {
    double radius = settings.footpath_radius;

    GeometryFactory geometryFactory = new GeometryFactory();
    STRtree index = new STRtree();
    for (Node node : vertices) {
      double lat = node.stop().latitude();
      double lon = node.stop().longitude();
      Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
      index.insert(point.getEnvelopeInternal(), node);
    }

    for (Node node : vertices) {
      double lat = node.stop().latitude();
      double lon = node.stop().longitude();
      Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
      Envelope searchEnv = point.getEnvelopeInternal();
      double deltaLat = radius / 111320.0;
      double deltaLon = radius / (111320.0 * Math.cos(Math.toRadians(lat)));
      searchEnv.expandBy(deltaLat, deltaLon);

      List<?> candidates = index.query(searchEnv);
      List<Edge> neighbors = new ArrayList<>();

      for (Object obj : candidates) {
        Node candidate = (Node) obj;
        if (!candidate.equals(node)) {
          double candidateLat = candidate.stop().latitude();
          double candidateLon = candidate.stop().longitude();
          double distance = haversine(lat, lon, candidateLat, candidateLon);
          if (distance <= radius) {
            neighbors.add(new Edge(node, candidate, (int)distance));
          }
        }
      }
      edge_count += neighbors.size();
      node.set_transfers(neighbors);
    }
  }

  public static double haversine(
      double lat1, double lon1, double lat2, double lon2) {
    final int R = 6371000; // Rayon de la Terre en mètres
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  }

  // #### Attributes ####

  private int edge_count = 0;
  private AlgoSettings settings;
  private List<Node> vertices;
}
