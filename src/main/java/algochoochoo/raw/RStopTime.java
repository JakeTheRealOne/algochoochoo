package algochoochoo.raw;

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
      throw new IllegalArgumentException("Wrong stop time entry size (Input: "
          + data.length + " != Expected: 4)");
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
   * @exception IllegalArgumentException if the string isn't formatted properly
   * @param dep_str Time (HH:MM:SS)
   */
  public static int read_time(String dep_str) {
    int h = 0;
    int m = 0;
    int s = 0;
    if (dep_str.length() != 8 || dep_str.charAt(2) != ':'
        || dep_str.charAt(5) != ':') {
      String msg = "Invalid time format (HH:MM:SS) : [" + dep_str + "]";
      throw new IllegalArgumentException(msg);
    }
    try {
      h = Integer.parseInt(dep_str.substring(0, 2));
      m = Integer.parseInt(dep_str.substring(3, 5));
      s = Integer.parseInt(dep_str.substring(6, 8));
    } catch (NumberFormatException e) {
      String msg = "Invalid time format (HH:MM:SS) : [" + dep_str + "]";
      throw new IllegalArgumentException(msg);
    }

    // Keep the time under 24h
    final int max_time = 24 * 3600;
    int time = h * 3600 + m * 60 + s;
    return time % max_time;
  }

  // #### Attributes ####

  String trip_id;
  int departure_time;
  String stop_id;
  int stop_sequence;
}
