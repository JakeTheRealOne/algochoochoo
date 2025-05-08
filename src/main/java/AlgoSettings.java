/**
 * Store settings for algorithm
 *
 * @author Bilal Vandenberge
 */
public class AlgoSettings {
  // #### Attributes ####

  /** If the name of the s and t stops are strictly given */
  boolean strict_search = true;

  /** The maximum walking distance for each transfer (meters) */
  int footpath_radius = 3000;

  /**
   * The minimum waiting time at a stop for the trips. This can be used to
   * handle and prevent delayed transport
   */
  int min_waiting_time = 0;

  /**
   * The cost function (that we are trying to minimize)
   * 
   * @param previous The previous edge in the path
   * @param edge The edge which we try to determine the cost An edge
   * @return The cost of the edge
   */
  long cost_function(Edge previous, Edge e) {
    int dur = e.duration();
    if (e.is_transfer()) {
      return (long) (dur * 3);
    }
    RouteType type = e.trip().route().type();
    switch (type) {
      case RouteType.BUS:
        return (long) (dur * 1.5);
      case RouteType.TRAIN:
        return (long) (dur * 1);
      case RouteType.METRO:
        return (long) (dur * 1.2);
      case RouteType.TRAM:
        return (long) (dur * 1.3);
      default:
        return (long) dur;
    }
  }

  boolean train_is_banned = false; // We are going to change that
}
