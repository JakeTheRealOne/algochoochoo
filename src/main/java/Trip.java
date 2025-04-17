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
   * @exception IllegalArgumentException If the argument is invalid
   * @param data A CSV file row
   */
  public Trip(String[] data) {
    if (data.length != 2) {
      throw new IllegalArgumentException(
          "Wrong trip entry size (Input: " + data.length + " != Expected: 2), " + data[0] + "");
    }
    id = data[0];
    route_id = data[1];
  }

  /** Convert a Trip object to string */
  @Override
  public String toString() {
    return "Trip(" + id + ", " + route_id + ")";
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
   * Get the id of the related route
   *
   * @return The route id
   */
  public String route_id() {
    return route_id;
  }

  // #### Attributes ####

  private String id;
  private String route_id;
}
