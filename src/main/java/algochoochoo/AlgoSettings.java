package algochoochoo;

import algochoochoo.graph.*;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Store settings for algorithm
 *
 * @author Bilal Vandenberge
 */
public class AlgoSettings {
  // #### Public methods ####

  public AlgoSettings() {
    weights.put(RouteType.FOOT, 3.0);
    weights.put(RouteType.TRAM, 1.3);
    weights.put(RouteType.TRAIN, 1.0);
    weights.put(RouteType.METRO, 1.2);
    weights.put(RouteType.BUS, 1.5);
  }

  // #### Attributes ####

  /** If the name of the s and t stops are strictly given */
  public boolean strict_search = true;

  /** The maximum walking distance for each transfer (meters) */
  public int footpath_radius = 500;

  /** The user preferences for transport mode */
  public Map<RouteType, Double> weights = new LinkedHashMap<>();

  public AlgoPriority priority = AlgoPriority.TOTAL_DURATION;

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

    int waiting_time =
        e.is_transfer() ? 0 : e.departure_time() - v.best_time();
    while (waiting_time < 0) {
      waiting_time += 24 * 3600; // Handle trips over several days
    }
    int dur = (int)(e.duration() * weights.getOrDefault(e.type(), 1.0));
    long time = dur + waiting_time;

    if (priority.equals(AlgoPriority.TOTAL_DURATION)) {
      time <<= 32;
    } else {
      transfers <<= 32;
    }

    return v.best_cost() + time + transfers;
  }
}
