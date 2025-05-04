import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (Litteraly) A graph
 *
 * @author Bilal Vandenberge
 */
public class Graph {
  // #### Public methods ####

  /**
   * Construct a new Graph object
   *
   * @param V The vertices map
   */
  public Graph(Map<String, Stop> V) {
    vertices = V;
  }

  /**
   * Construct a new Graph object from GTFS datas
   *
   * @param path The path of the main GTFS directory
   * @param set The settings for graph construction
   */
  public Graph(String path, AlgoSettings set) {
    settings = set;
    vertices = Parser.stops(path);
    ArrayList<Trip> trips = Parser.trips(path, vertices);
    from_trips(trips);
    transfers = new BallTree(vertices.values());
    transfers.update_footpaths(vertices.values(), new AlgoSettings());
  }

  /**
   * Get a stop by its id
   *
   * @return A stop
   */
  public Stop get_stop(String id) {
    return vertices.get(id);
  }

  /**
   * Iterate over the vertices of the graph
   *
   * @return An iterable
   */
  public Iterable<Stop> vertices() {
    return vertices.values();
  }

  /**
   * Get the settings used to build the graph
   *
   * @return The AlgoSettings object associated to the graph
   */
  public AlgoSettings settings() {
    return settings;
  }

  // #### Private helpers ####

  /**
   * Update stops and add edges from GTFS trips
   *
   * @param trips The list of trips
   * @return The list of connections
   */
  private void from_trips(ArrayList<Trip> trips) {}

  // #### Attributes ####

  private AlgoSettings settings;
  private BallTree transfers;
  private Map<String, Stop> vertices;
}
