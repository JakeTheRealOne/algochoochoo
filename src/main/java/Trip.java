import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * (Literally) A trip
 *
 * @author Bilal Vandenberge
 */
public class Trip {
  // #### Public methods ####

  // TODO: for each map.get, check if null

  /**
   * Construct a new Trip object
   *
   * @exception IllegalArgumentException If the route id doesn't match
   * @param raw_trip The raw trip output from the trips.csv file
   * @param routes The routes of the GTFS datas
   */
  public Trip(RTrip raw_trip, Map<String, Route> routes) {
    id = raw_trip.trip_id();
    route = routes.get(raw_trip.route_id());
    if (route == null) {
      throw new IllegalArgumentException("Unknown route: '" + raw_trip.route_id() + "'");
    }
    content = new ArrayList<>();
  }

  /** Convert a Trip object to string */
  @Override
  public String toString() {
    return "Trip(" + id + " with " + content.size() + " stops)";
  }

  /**
   * Add an element to the trip
   *
   * @param element The element to add
   */
  public void add(TripElement element) {
    content.add(element);
  }

  /**
   * Sort and return the content of the trip (based on the index of each element)
   *
   * @return The sorted content
   */
  public ArrayList<TripElement> sort() {
    content.sort(Comparator.comparingInt(TripElement::index));
    return content;
  }

  // #### Getters ####

  /**
   * Get the id of the trip
   *
   * @return The id
   */
  public String id() {
    return id;
  }

  /**
   * Get the route followed by the trip
   *
   * @return The route
   */
  public Route route() {
    return route;
  }

  /**
   * Get the content of the trip
   *
   * @return The content
   */
  public ArrayList<TripElement> content() {
    return content;
  }

  // #### Attributes ####

  private String id;
  private Route route;
  private ArrayList<TripElement> content;
}
