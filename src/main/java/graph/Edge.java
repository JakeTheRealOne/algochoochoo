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

    if (to == null) {
      throw new IllegalArgumentException(
          "Can't build an edge with an unknown target");
    } else if (dur < 0) {
      throw new IllegalArgumentException("Can't build a negative edge");
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
      throw new IllegalArgumentException("Can't build a negative edge");
    }
  }

  /** Convert a Edge object to string */
  @Override
  public String toString() {
    return "Edge(from " + from.stop().name() + " " + beautiful_time(dep)
        + ", to " + to.stop().name() + ")";
  }

  /**
   * Print the source node on STDOUT
   */
  public void print_from() {
    System.out.print(" from " + from.stop().name() + " "
        + beautiful_time(from.best_time()));
  }

  /**
   * Print the target node on STDOUT
   */
  public void print_to() {
    System.out.print(
        " to " + to.stop().name() + " " + beautiful_time(to.best_time()));
  }

  /**
   * Print the english directive related to the edge on STDOUT
   */
  public void print_directive() {
    String directive =
        is_transfer() ? "Walking" : ("Taking " + trip.route().clean_str());
    System.out.print(directive);
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

  public int cost() {
    return dur; // TEST
    // if (is_transfer()) {
    //   return (int)(dur * 3);
    // }

    // RouteType type = trip.route().type();
    // switch (type) {
    //   case RouteType.BUS:
    //     return (int)(dur * 1.5);
    //   case RouteType.TRAIN:
    //     return (int)(dur * 1);
    //   case RouteType.METRO:
    //     return (int)(dur * 1.2);
    //   case RouteType.TRAM:
    //     return (int)(dur * 1.3);
    //   default:
    //     return dur;
    // }
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

  /**
   * Compute the estimated walk distance from a distance
   *
   * @param dist The distance (in meters)
   * @return The estimated walk distance (in meters)
   */
  private static int compute_walk_dist(int dist) {
    final double coef = Math.sqrt(2);
    return (int) Math.abs(dist * coef);
  }
}
