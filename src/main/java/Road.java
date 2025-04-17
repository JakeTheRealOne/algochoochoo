/**
 * (Literally) A road
 *
 * @author Bilal Vandenberge
 */
public class Road {
  // #### Public methods ####

  /**
   * Construct a new Road object
   *
   * @exception IllegalArgumentException If the argument is invalid
   * @param data A CSV file row
   */
  public Road(String[] data) {
    if (data.length != 4) {
      throw new IllegalArgumentException(
          "Wrong road entry size (Input: " + data.length + " != Expected: 4)");
    }
    id = data[0];
    short_name = data[1];
    long_name = data[2];
    type = RoadType.valueOf(data[3]);
  }

  /** Convert a Road object to string */
  @Override
  public String toString() {
    return "Road(" + id + ", " + short_name + ", " + long_name + ", " + type + ")";
  }

  // #### Getters ####

  /**
   * Get the id of the road
   *
   * @return The id
   */
  public String id() {
    return id;
  }

  /**
   * Get the short name of the road
   *
   * @return The short name
   */
  public String short_name() {
    return short_name;
  }

  /**
   * Get the long name of the road
   *
   * @return The long name
   */
  public String long_name() {
    return long_name;
  }

  /**
   * Get the type of the road
   *
   * @return The road type
   */
  public RoadType type() {
    return type;
  }

  // #### Attributes ####

  private String id;
  private String short_name;
  private String long_name;
  private RoadType type;
}
