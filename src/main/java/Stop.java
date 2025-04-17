/**
 * (Literally) A stop
 *
 * @author Bilal Vandenberge
 */
public class Stop {
  // #### Public methods ####

  /**
   * Construct a new Stop object
   *
   * @exception IllegalArgumentException If the argument is invalid
   * @param data A CSV file row
   */
  public Stop(String[] data) {
    if (data.length != 4) {
      throw new IllegalArgumentException(
          "Wrong stop entry size (Input: " + data.length + " != Expected: 4), " + data[0] + "");
    }
    id = data[0];
    name = data[1];
    latitude = Double.parseDouble(data[2]);
    longitude = Double.parseDouble(data[3]);
  }

  /** Convert a Stop object to string */
  @Override
  public String toString() {
    return "Stop(" + id + ", '" + name + "', " + latitude + ", " + longitude + ")";
  }

  // #### Getters ####

  /**
   * Get the id of the stop
   *
   * @return The id
   */
  public String id() {
    return id;
  }

  /**
   * Get the name of the stop
   *
   * @return The name
   */
  public String name() {
    return name;
  }

  /**
   * Get the latitude of the stop
   *
   * @return The distance north or south of the equator
   */
  public double latitude() {
    return latitude;
  }

  /**
   * Get the longitude of the stop
   *
   * @return The distance east or west of the Prime Meridian
   */
  public double longitude() {
    return longitude;
  }

  // #### Attributes ####

  private String id;
  private String name;
  private double latitude;
  private double longitude;
}
