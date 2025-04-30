import java.util.ArrayList;

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
          "Wrong stop entry size (Input: " + data.length + " != Expected: 4)");
    }
    id = data[0];
    name = data[1];
    pos = new EarthPos(data[2], data[3]);
    neighbors = new ArrayList<Neighbor>();
  }

  /** Convert a Stop object to string */
  @Override
  public String toString() {
    return "Stop(" + id + ", '" + name + "', " + pos + ")";
  }

  /**
   * Set the list of neighbors
   *
   * @param n The new list of neighbors
   */
  public void setNeighbors(ArrayList<Neighbor> n) {
    neighbors = n;
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
   * Get the position of the stop
   *
   * @return An earth position
   */
  public EarthPos pos() {
    return pos;
  }

  /**
   * Get the latitude of the stop
   *
   * @return The distance north or south of the equator
   */
  public double latitude() {
    return pos.latitude();
  }

  /**
   * Get the longitude of the stop
   *
   * @return The distance east or west of the Prime Meridian
   */
  public double longitude() {
    return pos.longitude();
  }

  /**
   * Get the close neighbors of the stop
   *
   * @return The list of neighbors
   */
  public ArrayList<Neighbor> neighbors() {
    return neighbors;
  }

  // #### Attributes ####

  private String id;
  private String name;
  private EarthPos pos;

  private ArrayList<Neighbor> neighbors;
  private int earliest = Integer.MAX_VALUE;
  private Connection predecessor;
}
