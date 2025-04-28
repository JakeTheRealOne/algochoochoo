/**
 * (Literally) A neighbor within walking distance
 *
 * @author Bilal Vandenberge
 */
public class Neighbor {
  // #### Public methods ####

  /**
   * Construct a new Neighbor object
   *
   * @param to The neighbor
   * @param distance The distance to the neighbor
   */
  public Neighbor(Stop to, int distance) {
    stop = to;
    dur = compute_walk_time(distance);
  }

  /** Convert a Neighbor object to string */
  @Override
  public String toString() {
    return "Neighbor(" + stop + "', in " + dur + "s)";
  }

  // #### Getters ####

  /**
   * Get the neighbor
   *
   * @return The neighbor
   */
  public Stop stop() {
    return stop;
  }

  /**
   * Get the duration (walk time) to the neighbor
   *
   * @return The duration (in seconds)
   */
  public int duration() {
    return dur;
  }

  // #### Private helpers ####

  /**
   * Estimate and return the walk time in seconds (optimized for large datasets)
   *
   * @param dist The distance (in meter)
   * @return The estimated walk time in seconds
   */
  private static int compute_walk_time(int dist) {
    final double speed = 0.72; // 5 km/h is base walk speed
    final int margin = 90; // 1min30 for transfer

    int time = (int) (compute_walk_dist(dist) * speed);
    return time + margin;
  }

  private static int compute_walk_dist(int dist) {
    final double coef = Math.sqrt(2);
    return (int) Math.abs(dist * coef);
  }


  // #### Attributes ####
  private Stop stop;
  private int dur;
}
