import java.time.Duration;

/**
 * (Literally) A stop schedule
 *
 * @author Bilal Vandenberge
 */
public class StopTime {
  // #### Public methods ####

  // TODO stop_times: (trip_id,departure_time,stop_id,stop_sequence)

  /**
   * Construct a new StopTime object
   *
   * @exception IllegalArgumentException If the argument is invalid
   * @param data A CSV file row
   */
  public StopTime(String[] data) {
    if (data.length != 4) {
      throw new IllegalArgumentException(
          "Wrong stop_time entry size (Input: " + data.length + " != Expected: 4)");
    }
    trip_id = data[0];
    dep_time = read_time(data[1]);
    stop_id = data[2];
    stop_idx = Integer.parseInt(data[3]);
  }

  /** Convert a StopTime object to string */
  @Override
  public String toString() {
    return "StopTime(" + trip_id + ", " + dep_time + ", " + stop_id + ", " + stop_idx + ")";
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
   * Get the departure time
   *
   * @return The departure time
   */
  public long departure_time() {
    return dep_time;
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
  public int stop_idx() {
    return stop_idx;
  }

  // #### Attributes ####

  private String trip_id;
  private long dep_time;
  private String stop_id;
  private int stop_idx;

  // #### Private helpers ####

  /**
   * Convert a string representing a departure time to a numeric value
   *
   * @param dep_str Time (HH:mm:ss)
   */
  private static long read_time(String dep_str) {
    String[] dep_parts = dep_str.split(":");
    int hours = Integer.parseInt(dep_parts[0]);
    int minutes = Integer.parseInt(dep_parts[1]);
    int seconds = Integer.parseInt(dep_parts[2]);
    Duration duration = Duration.parse("PT" + hours + "H" + minutes + "M" + seconds + "S");
    return duration.getSeconds();
  }
}
