import java.util.Map;
import raw.RStopTime;

/**
 * (Literally) A trip element
 *
 * @author Bilal Vandenberge
 */
public class TripElement {
  // #### Public methods ####

  /**
   * Construct a new TripElement object
   *
   * @param stop_time The raw stop time coming from stop_times.csv
   * @param stops The map of {id, stop} from the GTFS data
   */
  public TripElement(RStopTime stop_time, Map<String, Stop> stops) {
    departure_time = stop_time.departure_time();
    index = stop_time.stop_index();
    stop = stops.get(stop_time.stop_id());
  }

  /** Convert a TripElement object to string */
  @Override
  public String toString() {
    return "TripElement(" + stop + ")";
  }

  // #### Getters ####

  /**
   * Get the stop of the trip element
   *
   * @return The stop
   */
  public Stop stop() {
    return stop;
  }

  /**
   * Get the departure time of the trip element
   *
   * @return The departure time (in seconds from midnight)
   */
  public int departure_time() {
    return departure_time;
  }

  /**
   * Get the index of the trip element
   *
   * @return The index in the sequence
   */
  public int index() {
    return index;
  }

  // #### Attributes ####

  private Stop stop;
  private int departure_time;
  private int index;
}
