import java.util.ArrayList;
import java.util.Comparator;

/**
 * (Literally) A trip
 *
 * @author Bilal Vandenberge
 */
public class Trip {
  // #### Public methods ####

  /**
   * Construct a new Trip object
   *
   * @param raw_trip The raw trip output from the trips.csv file
   */
  public Trip(RTrip raw_trip) {
    id = raw_trip.trip_id();
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
   * Get the content of the trip
   *
   * @return The content
   */
  public ArrayList<TripElement> content() {
    return content;
  }

  // #### Attributes ####

  private String id;
  private ArrayList<TripElement> content;
}
