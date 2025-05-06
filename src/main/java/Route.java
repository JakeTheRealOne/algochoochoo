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
      throw new IllegalArgumentException("Wrong route entry size (Input: "
          + data.length + " != Expected: 4)");
    }
    id = data[0];
    short_name = data[1];
    long_name = data[2];
    type = RouteType.valueOf(data[3]);
    agency = "None";
  }

  /** Convert a Route object to string */
  @Override
  public String toString() {
    return "Route(" + id + ", '" + short_name + "', '" + long_name + "', "
        + type + ")";
  }

  /**
   * Convert a route object into a brief, clean string
   * 
   * @return route_type route_short_name
   */
  public String clean_str() {
    return type + " " + short_name;
  }

  /**
   * Set the agency of the route
   *
   * @param ag The new agency name
   */
  public void setAgency(String ag) {
    agency = ag;
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

  /**
   * Get the agency owning the route (STIB, ...)
   *
   * @return The route agency
   */
  public String agency() {
    return agency;
  }

  // #### Attributes ####

  private String id;
  private String short_name;
  private String long_name;
  private String agency;
  private RouteType type;
}
