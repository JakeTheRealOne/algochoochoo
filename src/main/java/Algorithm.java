import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Execute algorithms on a graph
 * 
 * @author Bilal Vandenberge
 */
public class Algorithm {
  // #### Public methods ####

  /**
   * Run unit tests
   * 
   * @param args Irrelevant here
   */
  public static void main(String[] args) {
    Graph graph = new Graph("src/main/resources/GTFS");
    Algorithm algo = new Algorithm(graph, new AlgoSettings());
    List<Connection> path1 = algo.CSA("AUMALE", "FRAITEUR", 8 * 3600 + 0 * 60);
    List<Connection> path2 = algo.CSA("Antwerpen Centraal Station", "CHIMAY Petit Virelles", 14 * 3600 + 14 * 60 + 14);
    List<Connection> path3 = algo.CSA("Alveringem Nieuwe Herberg", "Aubange", 10 * 3600 + 30 * 60);
    View.show(path1);
    System.out.println();
    View.show(path2);
    System.out.println();
    View.show(path3);
    System.out.println();
  }

  /**
   * Construct a new Algorithm object
   * 
   * @param G   The graph of the algorithm
   * @param set The algorithm settings
   */
  public Algorithm(Graph G, AlgoSettings set) {
    graph = G;
    settings = set;
    sources = new ArrayList<>();
    targets = new ArrayList<>();
  }

  /**
   * Execute the Connexion scanning algorithm
   * 
   * @param s The source node
   * @param t The target node
   * @param h The source time
   */
  public List<Connection> CSA(String s, String t, int h) {
    init(s, t, h);

    Map<String, Connection> predecessor = new LinkedHashMap<>();

    for (Stop stop : graph.vertices()) {
      stop.init_for_algo();
    }

    for (String source : sources) {
      Stop stop = graph.get_stop(source);
      stop.cost().set_duration(h);
      for (Transfer transfer : stop.transfers()) {
        Stop near = transfer.stop();
        int candidate = h + transfer.duration();
        if (near.cost().duration() > candidate) {
          near.cost().set_duration(candidate);
          predecessor.put(
              transfer.stop().id(),
              new Connection(graph.get_stop(source), transfer.stop(), h, transfer.duration()));
        }
      }
    }

    for (Connection conn : graph.edges()) {
      Stop from = conn.from();
      Stop to = conn.to();
      int ahead = 0;
      Connection p = predecessor.getOrDefault(conn.from().id(), null);
      if (p != null) {
        if (p.trip() != conn.trip()) {
          ahead = settings.min_ahead_time;
        }
      }
      if (!(conn.departure_time() - ahead < from.cost().duration())) {
        if (conn.arrival_time() < to.cost().duration()) {
          to.cost().set_duration(conn.arrival_time());
          predecessor.put(conn.to().id(), conn);

          for (Transfer foot : conn.to().transfers()) {
            Stop near = foot.stop();
            int candidate = conn.arrival_time() + foot.duration();
            if (candidate < near.cost().duration()) {
              near.cost().set_duration(candidate);
              predecessor.put(
                  foot.stop().id(),
                  new Connection(conn.to(), foot.stop(), conn.arrival_time(), foot.duration()));
            }
          }
        }
      }
    }

    List<Connection> output = new ArrayList<>();
    int best_result = Integer.MAX_VALUE;
    Stop best_target = null;
    for (String target : targets) {
      Stop stop = graph.get_stop(target);
      int res = stop.cost().duration();
      if (res < best_result) {
        best_result = res;
        best_target = stop;
      }
    }
    if (best_target == null) {
      return output;
    }

    Connection conn = predecessor.get(best_target.id());
    while (conn != null) {
      output.add(conn);
      if (equal(s, conn.from().name())) {
        break;
      }
      conn = predecessor.get(conn.from().id());
    }
    Collections.reverse(output);
    return output;
  }

  // #### Private helpers ####

  /**
   * Initialize the attributes to execute an algorithm
   * 
   * @param s_ The name of the source stop
   * @param t  The name of the target stop
   * @param h_ The departure time at the source stop
   */
  private void init(String s_, String t, int h_) {
    h = h_;
    s = s_;
    sources.clear();
    targets.clear();
    for (Stop stop : graph.vertices()) {
      String name = stop.name();
      if (equal(s, name)) {
        sources.add(stop.id());
      }
      if (equal(t, name)) {
        targets.add(stop.id());
      }
    }
    if (sources.isEmpty() || targets.isEmpty()) {
      throw new IllegalArgumentException(
          "At least one source and one target is required (s: "
              + sources.size()
              + ", t: "
              + targets.size()
              + ")");
    }
  }

  private boolean equal(String candidate, String sample) {
    if (settings.strict_search) {
      return candidate.equals(sample);
    } else {
      return sample.contains(candidate);
    }
  }

  // #### Attributes ####

  private Graph graph;
  private AlgoSettings settings;
  private List<String> sources;
  private List<String> targets;
  private String s;
  private int h;
}
