package algochoochoo.query;

/**
 * The priority of a TDSP algorithm
 *
 * @author Bilal Vandenberge
 */
public enum AlgoPriority {
  /** The algorithm will minimize the path duration */
  TOTAL_DURATION,

  /** The algorithm will minimize the number of trip changes */
  TRIP_CHANGES,
}
