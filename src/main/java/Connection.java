/**
 * (Literally) A connection between two stops
 *
 * @author Bilal Vandenberge
 */
public class Connection {
  // #### Public methods ####

  public Connection(Stop first, Stop second, long dep, long arr) {
    from = first;
    to = second;
    departure_time = dep;
    arrival_time = arr;
    if (arr < dep) {
      throw new IllegalArgumentException("Arrival time: " + beautiful_time(false) + " < Departure time: " + beautiful_time(true));
    }
  }

  /** Convert an Edge object to string */
  @Override
  public String toString() {
    return "Conn(from " + from + ", to " + to + ")";
  }


  // #### Getters ####

  public Stop from() {
    return from;
  }

  public Stop to() {
    return to;
  }

  public long departure_time() {
    return departure_time;
  }

  public long arrival_time() {
    return arrival_time;
  }

  // #### Attributes ####

  Stop from;
  Stop to;
  long departure_time;
  long arrival_time;

  // #### Private helpers ####

  private String beautiful_time(boolean departure) {
    long time = (departure ? departure_time : arrival_time);
    return String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60);
  }
}