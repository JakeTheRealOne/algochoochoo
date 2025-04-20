/**
 * (Literally) A (directed) graph edge
 *
 * @author Bilal Vandenberge
 */
public class Edge {
  // #### Public methods ####

  public Edge(Node dest, long dep, long arr) {
    destination = dest;
    departure_time = dep;
    arrival_time = arr;
    if (arr < dep) {
      throw new IllegalArgumentException("Arrival time: " + beautiful_time(false) + " < Departure time: " + beautiful_time(true));
    }
  }

  /** Convert an Edge object to string */
  @Override
  public String toString() {
    return "Edge(to " + destination + ")";
  }


  // #### Getters ####

  // TODO: getters -_-

  // #### Attributes ####

  Node destination;
  long departure_time;
  long arrival_time;

  // #### Private helpers ####

  private String beautiful_time(boolean departure) {
    long time = (departure ? departure_time : arrival_time);
    return String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60);
  }
}