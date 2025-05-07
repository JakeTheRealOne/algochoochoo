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
   * The safety margin to avoid the transport leaving without us (in seconds)
   */
  int min_ahead_time = 0 * 60;
}
