package algochoochoo;

import algochoochoo.graph.*;

/**
 * Store settings for algorithm
 *
 * @author Bilal Vandenberge
 */
public class AlgoSettings {
  // #### Attributes ####

  /** If the name of the s and t stops are strictly given */
  public boolean strict_search = true;

  /** The maximum walking distance for each transfer (meters) */
  public int footpath_radius = 500;

  /**
   * The cost function (that we are trying to minimize)
   *
   * @param previous The previous edge in the path
   * @param edge The edge which we try to determine the cost An edge
   * @return The cost of the edge
   */
  long cost_function(Node v, Edge e) {
    long transfers = (v.best_edge() == null || v.best_edge().trip() != e.trip()
                         || e.is_transfer())
        ? 1
        : 0;
    transfers <<= 32;

    int waiting_time =
        e.is_transfer() ? 0 : e.departure_time() - v.best_time();
    while (waiting_time < 0) {
      waiting_time += 24 * 3600; // Handle trips over several days
    }

    long dur = e.duration();
    RouteType type = e.type();
    switch (type) {
      case RouteType.BUS:
        dur = (long) (dur * 1.5);
      case RouteType.TRAIN:
        dur = (long) (dur * 1);
      case RouteType.METRO:
        dur = (long) (dur * 1.2);
      case RouteType.TRAM:
        dur = (long) (dur * 1.3);
      case RouteType.FOOT:
        dur = (long) (dur * 3);
    }

    return v.best_cost() + dur + waiting_time + transfers;
  }
}
