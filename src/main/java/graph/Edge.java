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
   */
  public Edge(TripElement first, TripElement second, Map<String, Node> V) {
    to = V.get(second.stop().id());
    dep = first.departure_time();
    dur = second.departure_time() - dep;

    if (to == null) {
      throw new IllegalArgumentException("Can't build an edge with an unknown target");
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
  public Edge(Node target, int distance) {
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
    return "Edge(departure at " + beautiful_time(dep) + ", to " + to + ")";
  }

  // #### Getters ####

  /**
   * Get the target node of the connection
   *
   * @return The target node
   */
  public Node to() {
    return to;
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

  // #### Attributes ####

  Node to;
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
        "%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60);
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
