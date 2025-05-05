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
   */
  public Edge(TripElement first, TripElement second) {
    to = second.stop();
    dep = first.departure_time();
    dur = second.departure_time() - dep;

    if (dur < 0) {
      throw new IllegalArgumentException("Can't build a negative edge");
    }
  }

  public Edge(Stop target, int duration) {
    to = target;
    dep = -1;
    dur = duration;
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
   * Get the target stop of the connection
   *
   * @return The target stop
   */
  public Stop to() {
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

  Stop to;
  int dep;
  int dur;

  // #### Private helpers ####

  /**
   * Convert a time into beautiful string
   *
   * @param time The time (in seconds)
   */
  private String beautiful_time(int time) {
    return String.format(
        "%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60);
  }
}
