package algochoochoo.graph;

import algochoochoo.parsing.EarthPos;
import algochoochoo.parsing.Parser;
import algochoochoo.parsing.Stop;
import algochoochoo.parsing.Trip;
import algochoochoo.parsing.TripElement;
import java.util.ArrayList;
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
   * @param set The settings for graph construction
   */
  public Graph(GraphSettings set) {
    reload(set);
  }

  /** Convert a Graph object to string */
  @Override
  public String toString() {
    return "Graph(#V: " + V_card() + ", #E: " + E_card() + ")";
  }

  /**
   * (Lazy) Reload the graph with new settings
   *
   * @param new_settings Graph building settings
   */
  public void reload(GraphSettings new_settings) {
    if (new_settings.foot_radius < 0) {
      throw new IllegalArgumentException(
          "The footpath distance has to be positive: "
          + new_settings.foot_radius);
    }

    GraphSettings old_settings = settings;
    settings = new_settings;
    edge_count = 0;

    if (old_settings == null
        || !old_settings.GTFS_path.equals(settings.GTFS_path)) {
      // New settings or new GTFS path (reevaluate everything)
      Map<String, Stop> stops = Parser.stops(settings.GTFS_path);
      List<Trip> trips = Parser.trips(settings.GTFS_path, stops);
      build_nodes(stops, trips);
    } else if (old_settings.foot_radius != settings.foot_radius) {
      populate_transfers(); // Reset and reevaluate all transfers
    }
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

  /**
   * Get the list of the vertices of the graph
   *
   * @return The graph nodes
   */
  public List<Node> vertices() {
    return vertices;
  }

  /**
   * Get the settings used to build the graph
   *
   * @return The graph settings
   */
  public GraphSettings settings() {
    return settings;
  }

  // #### Private helpers ####

  /**
   * From generic datas, construct the nodes of the graph
   *
   * @param stops The map {stop_id,stop_object}
   * @param trips The map {trip_id,trip_object}
   * @param tree  The balltree containing the stops
   */
  private void build_nodes(Map<String, Stop> stops, List<Trip> trips) {
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

    populate_transfers();
  }

  /**
   * Evaluate all transfers between near stops
   */
  private void populate_transfers() {
    double radius = settings.foot_radius;

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
          double distance =
              haversine(node.stop().pos(), candidate.stop().pos());
          if (distance <= radius) {
            neighbors.add(new Edge(node, candidate, (int) distance));
          }
        }
      }
      edge_count += neighbors.size();
      node.set_transfers(neighbors);
    }
  }

  /**
   * Compute the distance between two position with the haversine formula
   *
   * @param pos1 The origin position
   * @param pos2 The other position
   * @return The distance (in meters) between pos1 and pos2
   */
  private static double haversine(EarthPos pos1, EarthPos pos2) {
    final double R = 6371000.0;
    double phi1 = Math.toRadians(pos1.latitude());
    double phi2 = Math.toRadians(pos2.latitude());
    double deltaPhi = Math.toRadians(pos2.latitude() - pos1.latitude());
    double deltaLambda = Math.toRadians(pos2.longitude() - pos1.longitude());
    double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
        + Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2)
            * Math.sin(deltaLambda / 2);
    double distance = R * Math.sqrt(a * (2 - a));

    return distance;
  }

  // #### Attributes ####

  private int edge_count = 0;
  private GraphSettings settings;
  private List<Node> vertices;
}
