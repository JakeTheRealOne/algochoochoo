package algochoochoo.graph;

import algochoochoo.*;
import java.awt.Color;
import java.util.Map;

/**
 * (Literally) A connection between two stops
 *
 * @author Bilal Vandenberge
 */
public class Edge {
  // #### Public methods ####

  /**
   * Construct a new Edge object
   *
   * @param first The first element
   * @param second The second element
   * @param V The vertices of the graph
   * @param trip The trip
   */
  public Edge(
      TripElement first, TripElement second, Map<String, Node> V, Trip t) {
    from = V.get(first.stop().id());
    to = V.get(second.stop().id());
    trip = t;
    dep = first.departure_time();
    dur = second.departure_time() - dep;
    while (dur < 0) {
      dur += 24 * 3600;
    }

    if (to == null) {
      throw new IllegalArgumentException(
          "Can't build an edge with an unknown target");
    } else if (dur < 0) {
      throw new IllegalArgumentException(
          "Can't build a negative edge: " + dur);
    }
  }

  /**
   * Construct a new Edge object representing a transfer
   *
   * @param target The target node of the footpath
   * @param distance the distance between the two nodes (in meters)
   */
  public Edge(Node source, Node target, int distance) {
    from = source;
    to = target;
    dep = -1;
    dur = compute_walk_time(distance);
    if (dur < 0) {
      throw new IllegalArgumentException(
          "Can't build a negative edge: " + dur);
    }
  }

  /** Convert a Edge object to string */
  @Override
  public String toString() {
    return "Edge(from " + from.stop().name() + " " + beautiful_time(dep)
        + ", to " + to.stop().name() + " in " + dur / 60f + " minutes)";
  }

  /**
   * Get the english directive related to the edge on STDOUT
   *
   * @return The directive (as a string)
   */
  public String directive() {
    String directive =
        is_transfer() ? "Walking" : ("Taking " + trip.route().clean_str());
    return directive;
  }

  // #### Getters ####

  /**
   * Get the target node of the edge
   *
   * @return The target node
   */
  public Node to() {
    return to;
  }

  /**
   * Get the source node of the edge
   *
   * @return The source node
   */
  public Node from() {
    return from;
  }

  /** Return the trip of the edge or null if its a transfer */
  public Trip trip() {
    return trip;
  }

  /**
   * Return the route type of the edge
   */
  public RouteType type() {
    return is_transfer() ? RouteType.FOOT : trip.route().type();
  }

  /**
   * Get the departure time from the source stop
   *
   * @return The departure time
   */
  public int departure_time() {
    return dep;
  }

  /**
   * Get the duration of the edge
   *
   * @return The duration of the edge
   */
  public int duration() {
    return dur;
  }

  /**
   * Get if the edge is a transfer
   *
   * @return If the edge represents a footpath
   */
  public boolean is_transfer() {
    return dep < 0;
  }

  /**
   * Get if the edge is a connection
   *
   * @return If the edge represents a transport connection (e.g BUS, TRAIN)
   */
  public boolean is_connection() {
    return !is_transfer();
  }

  /**
   * Compute a vivid color based on the trip of the edge
   *
   * @return The color of the edge
   */
  public Color color() {
    if (is_transfer()) {
      return new Color(40, 40, 40); // Light grey
    }

    String name = trip().route().long_name();
    int hash = name.hashCode();

    int r = (hash & 0xFF0000) >> 16;
    int g = (hash & 0x00FF00) >> 8;
    int b = (hash & 0x0000FF);
    float[] hsb = Color.RGBtoHSB(r, g, b, null);
    float saturation = Math.min(1.0f, hsb[1] * 1.5f);
    float brightness = 0.8f;
    return Color.getHSBColor(hsb[0], saturation, brightness);
  }

  // #### Attributes ####

  Node from;
  Node to;
  Trip trip;
  int dep;
  int dur;

  // #### Private helpers ####

  /**
   * Convert a time into beautiful string
   *
   * @param time The time (in seconds)
   */
  private static String beautiful_time(int time) {
    return String.format(
        "(%02d:%02d:%02d)", time / 3600, (time % 3600) / 60, time % 60);
  }

  /**
   * Compute the estimated walk time from a distance
   *
   * @param dist The distance (in meters)
   * @return The estimated walk time (in seconds)
   */
  private static int compute_walk_time(int dist) {
    final double speed = 0.72; // 5 km/h is base walk speed
    final int margin = 90; // 1min30 for transfer padding

    int time = (int) (compute_walk_dist(dist) * speed);
    return time + margin;
  }

  // TODO Add cyclism

  /**
   * Compute the estimated walk distance from a distance
   *
   * @param dist The distance (in meters)
   * @return The estimated walk distance (in meters)
   */
  private static int compute_walk_dist(int dist) {
    final double coef = Math.PI / 2.0;
    return (int) Math.abs(dist * coef);
  }
}
