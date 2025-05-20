/*
 * This file is part of Algochoochoo.
 *
 * Licensed under the GNU General Public License v3.
 * See <https://www.gnu.org/licenses/gpl-3.0.html> for details.
 * 
 * 2025
 * Bilal Vandenberge
 */

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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.index.strtree.ItemBoundable;
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
  public Graph(GraphSettings set) { reload(set); }

  /** Convert a Graph object to string */
  @Override
  public String toString() {
    return "Graph(#V: " + V_card() + ", #A: " + A_card() + ", #T: " + T_card() +
        ")";
  }

  /**
   * (Lazy) Reload the graph with new settings
   *
   * @param new_settings Graph building settings
   */
  public void reload(GraphSettings new_settings) {
    if (new_settings.foot_radius < 0) {
      throw new IllegalArgumentException(
          "The footpath distance has to be positive: " +
          new_settings.foot_radius);
    }

    long start_time = System.nanoTime();

    GraphSettings old_settings = settings == null ? null : settings.clone();
    settings = new_settings.clone();

    if (old_settings == null ||
        !old_settings.GTFS_path.equals(settings.GTFS_path)) {
      // New settings or new GTFS path (reevaluate everything)
      Map<String, Stop> stops = Parser.stops(settings.GTFS_path);
      List<Trip> trips = Parser.trips(settings.GTFS_path, stops);
      build_nodes(stops, trips);
    } else if (old_settings.foot_radius != settings.foot_radius) {
      populate_transfers(); // Reset and reevaluate all transfers
    }

    long end_time = System.nanoTime();
    runtime = ((double)end_time - start_time) / 1_000_000_000f;
  }

  // #### Getters ####

  /**
   * Get the cardinality of the edge set
   *
   * @return The number of edges
   */
  public int E_card() { return transfer_count + connection_count; }

  /**
   * Get the cardinality of the connection set
   *
   * @return The number of connections
   */
  public int A_card() { return connection_count; }

  /**
   * Get the cardinality of the transfer set
   *
   * @return The number of transfers
   */
  public int T_card() { return transfer_count; }

  /**
   * Get the cardinality of the node set
   *
   * @return The number of nodes
   */
  public int V_card() { return vertices.size(); }

  /**
   * Get the list of the vertices of the graph
   *
   * @return The graph nodes
   */
  public List<Node> vertices() { return vertices; }

  /**
   * Get the settings used to build the graph
   *
   * @return The graph settings
   */
  public GraphSettings settings() { return settings; }

  /**
   * Get the runtime of the last Graph.reload operation
   *
   * @return The reload runtime (in seconds)
   */
  public double reload_runtime() { return runtime; }

  // #### Private helpers ####

  /**
   * From generic datas, construct the nodes of the graph
   *
   * @param stops The map {stop_id,stop_object}
   * @param trips The map {trip_id,trip_object}
   * @param tree  The balltree containing the stops
   */
  private void build_nodes(Map<String, Stop> stops, List<Trip> trips) {
    connection_count = 0;

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
      connection_count += n - 1;
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
    transfer_count = 0;

    double radius = settings.foot_radius;
    STRtree tree = compute_tree();
    GeometryFactory factory = new GeometryFactory();
    for (Node node : vertices) {
      Envelope zone = compute_search_zone(node.stop(), factory);
      List<Object> candidates = tree.query(zone);
      List<Edge> transfers = new ArrayList<>();

      for (Object candidate : candidates) {
        Node neighbor = (Node)candidate;
        if (neighbor == node)
          continue;
        double distance = haversine(node.stop().pos(), neighbor.stop().pos());
        if (distance <= radius) {
          Edge transfer = new Edge(node, neighbor, (int)distance);
          transfers.add(transfer);
        }
      }
      transfer_count += transfers.size();
      node.set_transfers(transfers);
    }
  }

  /**
   * Construct a STR tree containing all vertices
   *
   * @return A STR tree
   */
  private STRtree compute_tree() {
    final int node_capacity = 10;
    ArrayList<ItemBoundable> stop_bounds = new ArrayList<>(V_card());
    for (Node node : vertices) {
      double latitude = node.stop().latitude();
      double longitude = node.stop().longitude();
      Envelope envelope =
          new Envelope(longitude, longitude, latitude, latitude);
      ItemBoundable bound = new ItemBoundable(envelope, node);
      stop_bounds.add(bound);
    }

    return new STRtree(node_capacity, stop_bounds);
  }

  /**
   * Compute the envelope of a stop used in near stops search
   *
   * @param stop The central stop
   * @param factory The geometry factory
   */
  private Envelope compute_search_zone(Stop stop, GeometryFactory factory) {
    final double one_lat = 111320.0; // 1° latitude
    double radius = settings.foot_radius;
    double latitude = stop.latitude();
    double longitude = stop.longitude();
    double lat_diff = radius / one_lat;
    double lon_diff = radius / (one_lat * Math.cos(Math.toRadians(latitude)));
    Point point = factory.createPoint(new Coordinate(longitude, latitude));
    Envelope envelope = point.getEnvelopeInternal();
    envelope.expandBy(lat_diff, lon_diff);
    return envelope;
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
    double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
               Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2) *
                   Math.sin(deltaLambda / 2);
    double distance = R * Math.sqrt(a * (2 - a));

    return distance;
  }

  // #### Attributes ####

  private int connection_count = 0;
  private int transfer_count = 0;
  private GraphSettings settings;
  private List<Node> vertices;

  // Debug:
  private double runtime = 0;
}
