package raw;

/**
 * A raw stop time from GTFS stop_times.csv file
 *
 * @author Bilal Vandenberge
 */
public class RStopTime {
  // #### Public methods ####

  /**
   * Construct a new RStoptime object
   *
   * @exception IllegalArgumentException If the argument is invalid
   * @param data A CSV file row
   */
  public RStopTime(String[] data) {
    if (data.length != 4) {
      throw new IllegalArgumentException(
          "Wrong stop time entry size (Input: " + data.length + " != Expected: 4)");
    }
    trip_id = data[0];
    departure_time = read_time(data[1]);
    stop_id = data[2];
    stop_sequence = Integer.parseInt(data[3]);
  }

  /** Convert a RStopTime object to string */
  @Override
  public String toString() {
    return "RStopTime(index " + stop_sequence + " in trip " + trip_id + ")";
  }

  // #### Getters ####

  /**
   * Get the id of the trip
   *
   * @return The trip id
   */
  public String trip_id() {
    return trip_id;
  }

  /**
   * Get the departure time of the transport to the next stop in the trip
   *
   * @return The departure time
   */
  public int departure_time() {
    return departure_time;
  }

  /**
   * Get the id of the stop
   *
   * @return The stop id
   */
  public String stop_id() {
    return stop_id;
  }

  /**
   * Get the index of the stop in the trip sequence
   *
   * @return The stop index
   */
  public int stop_index() {
    return stop_sequence;
  }

  // #### Private helpers ####

  /**
   * Convert a string representing a departure time to a numeric value
   *
   * @param dep_str Time (HH:mm:ss)
   */
  private static int read_time(String dep_str) {
    int h = 0, m = 0, s = 0;
    int i = 0, j = 0;

    while (dep_str.charAt(j) != ':') j++;
    h = Integer.parseInt(dep_str.substring(i, j));
    i = ++j;

    while (dep_str.charAt(j) != ':') j++;
    m = Integer.parseInt(dep_str.substring(i, j));
    i = ++j;

    s = Integer.parseInt(dep_str.substring(i));

    return h * 3600 + m * 60 + s;
  }

  // #### Attributes ####

  String trip_id;
  int departure_time;
  String stop_id;
  int stop_sequence;
}
