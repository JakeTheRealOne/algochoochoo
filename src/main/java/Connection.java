/**
 * (Literally) A connection between two stops
 *
 * @author Bilal Vandenberge
 */
public class Connection {
  // #### Public methods ####

  /**
   * Construct a new Connection object
   * 
   * @param first The first element
   * @param second The second element
   */
  public Connection(TripElement first, TripElement second, String trip) {
    from = first.stop();
    to = second.stop();
    trip_id = trip;
    departure_time = first.departure_time();
    arrival_time = second.departure_time();
    if (arrival_time < departure_time) {
      throw new IllegalArgumentException("Arrival time: " + beautiful_time(false) + " < Departure time: " + beautiful_time(true));
    }
  }

  /** Convert a Connection object to string */
  @Override
  public String toString() {
    return "Conn(from " + from + ", to " + to + ")";
  }


  // #### Getters ####

  /**
   * Get the source stop of the connection
   * 
   * @return The source stop
   */
  public Stop from() {
    return from;
  }

  /**
   * Get the target stop of the connection
   * 
   * @return The target stop
   */
  public Stop to() {
    return to;
  }

  /**
   * Get the trip id of the connection
   * 
   * @return The trip id
   */
  public String trip_id() {
    return trip_id;
  }

  /**
   * Get the departure time from the source stop
   * 
   * @return The departure time
   */
  public int departure_time() {
    return departure_time;
  }

  /**
   * Get the arrival time to the target stop
   * 
   * @return The arrival time
   */
  public int arrival_time() {
    return arrival_time;
  }

  // #### Attributes ####

  Stop from;
  Stop to;
  String trip_id;
  int departure_time;
  int arrival_time;

  // #### Private helpers ####

  private String beautiful_time(boolean departure) {
    int time = (departure ? departure_time : arrival_time);
    return String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60);
  }
}