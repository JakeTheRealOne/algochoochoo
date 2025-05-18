package algochoochoo.query;

import algochoochoo.graph.*;
import algochoochoo.parsing.RouteType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Store settings for algorithm
 *
 * @author Bilal Vandenberge
 */
public class AlgoSettings implements Cloneable {
  // #### Public methods ####

  /**
   * Construct a new default AlgoSettings object
   */
  public AlgoSettings() {
    weights.put(RouteType.FOOT, 1.0);
    weights.put(RouteType.TRAM, 1.0);
    weights.put(RouteType.TRAIN, 1.0);
    weights.put(RouteType.METRO, 1.0);
    weights.put(RouteType.BUS, 1.0);
  }

  /**
   * Construct a new AlgoSettings from exec arguments
   *
   * @param args The arguments of the executable
   */
  public AlgoSettings(String[] args) {
    this();
    int n = args.length;
    for (int i = 3; i < n; ++i) {
      String arg = args[i];
      if (arg.startsWith(PRIORITY_ARG)) {
        parsePriority(arg);
      } else if (arg.startsWith(FOOTW_ARG)) {
        parseWeight(arg, RouteType.FOOT);
      } else if (arg.startsWith(TRAMW_ARG)) {
        parseWeight(arg, RouteType.TRAM);
      } else if (arg.startsWith(BUSW_ARG)) {
        parseWeight(arg, RouteType.BUS);
      } else if (arg.startsWith(METROW_ARG)) {
        parseWeight(arg, RouteType.METRO);
      } else if (arg.startsWith(TRAINW_ARG)) {
        parseWeight(arg, RouteType.TRAIN);
      }
    }
  }

  /**
   * Copy the settings
   */
  public AlgoSettings clone() {
    AlgoSettings copy = new AlgoSettings();
    copy.strict_search = strict_search;
    copy.weights = new LinkedHashMap<>(weights);
    copy.priority = priority;
    return copy;
  }

  // #### Constants ####

  final private double MAX_WEIGHT = 10;

  final static private String PRIORITY_ARG = "--priority=";
  final static private String FOOTW_ARG = "--foot-weight=";
  final static private String METROW_ARG = "--metro-weight=";
  final static private String TRAINW_ARG = "--train-weight=";
  final static private String TRAMW_ARG = "--tram-weight=";
  final static private String BUSW_ARG = "--bus-weight=";

  final static private String tot_duration_priority = "time";
  final static private String trip_changes_priority = "trips";

  // #### Attributes ####

  /** If the name of the s and t stops are strictly given */
  public boolean strict_search = true;

  /** The user preferences for transport mode */
  public Map<RouteType, Double> weights = new LinkedHashMap<>();

  /** The priority of the algorithm (which criterion we will minimize first) */
  public AlgoPriority priority = AlgoPriority.TOTAL_DURATION;

  /**
   * The cost function (that the algorithm is minimizing)
   *
   * @param v The current node
   * @param e The edge from v which we try to determine the cost
   * @return cost(v) + cost(e)
   */
  public long cost_function(Node v, Edge e) {
    long transfers = (v.best_edge() == null ||
                      v.best_edge().trip() != e.trip() || e.is_transfer())
                         ? 1
                         : 0;

    int waiting_time = e.is_transfer() ? 0 : e.departure_time() - v.best_time();
    waiting_time = Math.floorMod(waiting_time,
                                 24 * 3600); // Handle trips over several days
    int dur = (int)(e.duration() * weights.getOrDefault(e.type(), 1.0));
    long time = dur + waiting_time;

    if (priority.equals(AlgoPriority.TOTAL_DURATION)) {
      time <<= 32;
    } else {
      transfers <<= 32;
    }

    return v.best_cost() + time + transfers;
  }

  /**
   * Print on STDOUT the settings
   */
  public void print() {
    System.out.println("Priority: " + priority);
    System.out.println("Foot weight: " +
                       weights.getOrDefault(RouteType.FOOT, 1.0));
    System.out.println("Metro weight: " +
                       weights.getOrDefault(RouteType.METRO, 1.0));
    System.out.println("Bus weight: " +
                       weights.getOrDefault(RouteType.BUS, 1.0));
    System.out.println("Tram weight: " +
                       weights.getOrDefault(RouteType.TRAM, 1.0));
    System.out.println("Train weight: " +
                       weights.getOrDefault(RouteType.TRAIN, 1.0));
  }

  // #### Helpers ####

  /**
   * Update the priority from exec args
   *
   * @param arg One exec argument
   */
  void parsePriority(String arg) {
    int i = PRIORITY_ARG.length();
    String raw = arg.substring(i);
    switch (raw) {
    case tot_duration_priority:
      priority = AlgoPriority.TOTAL_DURATION;
      break;
    case trip_changes_priority:
      priority = AlgoPriority.TRIP_CHANGES;
      break;
    default:
      throw new IllegalArgumentException("Unknown priority: " + raw);
    }
  }

  /**
   * Reevaluate the weight of a route type from exec arg
   *
   * @param arg  One exec argument
   * @param type The target type
   */
  void parseWeight(String arg, RouteType type) {
    int i = (type == RouteType.METRO   ? METROW_ARG
             : type == RouteType.TRAIN ? TRAINW_ARG
             : type == RouteType.TRAM  ? TRAMW_ARG
             : type == RouteType.BUS   ? BUSW_ARG
                                       : FOOTW_ARG)
                .length();
    double weight = 0;
    try {
      weight = Double.parseDouble(arg.substring(i));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid weight: " + arg.substring(i));
    }
    if (weight <= 0) {
      throw new IllegalArgumentException("Weight can't be negative or null: " +
                                         weight);
    } else if (weight > MAX_WEIGHT) {
      throw new IllegalArgumentException("Weight can't exceed " + MAX_WEIGHT +
                                         ": " + weight);
    }
    weights.put(type, weight);
  }
}
