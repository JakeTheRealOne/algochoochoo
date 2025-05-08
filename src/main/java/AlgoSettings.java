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
  int min_waiting_time = 0 * 60;

  /**
   * The maximum waiting time at a stop for the next trips.
   */
  int max_waiting_time = Integer.MAX_VALUE;

  boolean train_is_banned = true; // We are going to change that
}
