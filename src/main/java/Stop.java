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
    transfers = new ArrayList<Transfer>();
  }

  /** Convert a Stop object to string */
  @Override
  public String toString() {
    return "Stop(" + id + ", '" + name + "', " + pos + ")";
  }

  /**
   * Set the list of transfers
   *
   * @param n The new list of transfers
   */
  public void set_transfers(ArrayList<Transfer> t) {
    transfers = t;
  }

  /** Reinitialize the attributes for a new algorithm execution */
  public void init_for_algo() {
    predecessor = null;
    best = new PathCost(Integer.MAX_VALUE);
  }

  /** Relaxe near stops with transfers */
  public void relaxe_transfers() {
    int h = best.duration();
    for (Transfer transfer : transfers) {
      Stop near = transfer.stop();
      int candidate = h + transfer.duration();
      if (near.cost().duration() > candidate) {
        near.cost().set_duration(candidate);
        Connection conn = new Connection(this, transfer.stop(), h, transfer.duration());
        near.set_predecessor(conn);
      }
    }
  }

  /**
   * Check if a connection leading to this stop is earlier than current earliest connection
   *
   * @param conn The connection
   */
  public void evaluate(Connection conn) {
    if (conn.to() != this) return;

    Stop from = conn.from();
    Stop to = conn.to();
    boolean isLegal = conn.departure_time() >= from.cost().duration();
    boolean isBest = conn.arrival_time() < best.duration();
    if (isLegal && isBest) {
      best.set_duration(conn.arrival_time());
      set_predecessor(conn);
      relaxe_transfers();
    }
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
   * Get the transfers of the stop
   *
   * @return The list of transfers
   */
  public ArrayList<Transfer> transfers() {
    return transfers;
  }

  /**
   * Get the best cost to get to the stop
   *
   * @return The best cost
   */
  public PathCost cost() {
    return best;
  }

  /**
   * Get the predecessor of the stop
   *
   * @return The predecessor of the stop
   */
  public Connection predecessor() {
    return predecessor;
  }

  // #### Setters ####

  /**
   * Set the best cost to get to the stop
   *
   * @param cost The new cost
   */
  public void set_cost(PathCost cost) {
    best = cost;
  }

  /**
   * Set the predecessor of the stop
   *
   * @param pred The new predecessor
   */
  public void set_predecessor(Connection pred) {
    predecessor = pred;
  }

  // #### Attributes ####

  private String id;
  private String name;
  private EarthPos pos;

  private ArrayList<Transfer> transfers;
  private PathCost best;
  private Connection predecessor;
}
