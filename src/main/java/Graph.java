import java.util.ArrayList;
import java.util.Comparator;
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
   * @param E The edges list
   * @param V The vertices map
   */
  public Graph(List<Connection> E, Map<String, Stop> V) {
    edges = E;
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
    edges = from_trips(trips);
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
   * Iterate over the edges of the graph
   *
   * @return An iterable
   */
  public Iterable<Connection> edges() {
    return edges;
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
   * Convert a list of trips to a list of connections
   *
   * @param trips The list of trips
   * @return The list of connections
   */
  private ArrayList<Connection> from_trips(ArrayList<Trip> trips) {
    ArrayList<Connection> output = new ArrayList<>();
    for (Trip trip : trips) {
      ArrayList<TripElement> seq = trip.content();
      int n = seq.size();
      for (int i = 0; i < n - 1; ++i) {
        Connection conn = new Connection(seq.get(i), seq.get(i + 1), trip);
        output.add(conn);
      }
    }
    output.sort(Comparator.comparingInt(Connection::departure_time));
    return output;
  }

  // #### Attributes ####

  private AlgoSettings settings;
  private BallTree transfers;
  private List<Connection> edges;
  private Map<String, Stop> vertices;
}
