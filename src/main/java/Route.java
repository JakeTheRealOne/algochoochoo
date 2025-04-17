/**
 * (Literally) A route
 *
 * @author Bilal Vandenberge
 */
public class Route {
  // #### Public methods ####

  /**
   * Construct a new Route object
   *
   * @exception IllegalArgumentException If the argument is invalid
   * @param data A CSV file row
   */
  public Route(String[] data) {
    if (data.length != 4) {
      throw new IllegalArgumentException(
          "Wrong route entry size (Input: " + data.length + " != Expected: 4)");
    }
    id = data[0];
    short_name = data[1];
    long_name = data[2];
    type = RouteType.valueOf(data[3]);
  }

  /** Convert a Route object to string */
  @Override
  public String toString() {
    return "Route(" + id + ", '" + short_name + "', '" + long_name + "', " + type + ")";
  }

  // #### Getters ####

  /**
   * Get the id of the route
   *
   * @return The id
   */
  public String id() {
    return id;
  }

  /**
   * Get the short name of the route
   *
   * @return The short name
   */
  public String short_name() {
    return short_name;
  }

  /**
   * Get the long name of the route
   *
   * @return The long name
   */
  public String long_name() {
    return long_name;
  }

  /**
   * Get the type of the route
   *
   * @return The route type
   */
  public RouteType type() {
    return type;
  }

  // #### Attributes ####

  private String id;
  private String short_name;
  private String long_name;
  private RouteType type;
}
