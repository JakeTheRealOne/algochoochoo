import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Execute algorithms on a graph
 */
public class Algorithm {
  public static void main(String[] args) {
    Graph graph = new Graph("src/main/resources/GTFS");
    Algorithm algo = new Algorithm(graph, new AlgoSettings());
    List<Connection> path = algo.CSA("AUMALE", "FRAITEUR", 8 * 3600 + 0 * 60);
    View.print(path);
  }

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

    Map<String, Integer> earliest = new LinkedHashMap<>();
    Map<String, Connection> predecessor = new LinkedHashMap<>();

    for (Connection conn : graph.edges()) {
      earliest.put(conn.from().id(), Integer.MAX_VALUE);
      earliest.put(conn.to().id(), Integer.MAX_VALUE);
    }
    for (String source : sources) {
      earliest.put(source, h);
      for (Neighbor neighbor : graph.get_stop(source).neighbors()) {
        if (earliest.getOrDefault(neighbor.stop().id(), Integer.MAX_VALUE) > h + neighbor.duration()) {
          earliest.put(neighbor.stop().id(), h + neighbor.duration());
          predecessor.put(
            neighbor.stop().id(),
            new Connection(graph.get_stop(source), neighbor.stop(), h, neighbor.duration()));
        }
      }
    }

    for (Connection conn : graph.edges()) {
      if (!earliest.containsKey(conn.from().id())) continue;
      int ahead = 0;
      Connection p = predecessor.getOrDefault(conn.from().id(), null);
      if (p != null) {
        if (p.trip() != conn.trip()) {
          ahead = settings.min_ahead_time;
        }
      }
      if (!(conn.departure_time() - ahead < earliest.get(conn.from().id()))) {
        if (conn.arrival_time() < earliest.get(conn.to().id())) {
          earliest.put(conn.to().id(), conn.arrival_time());
          predecessor.put(conn.to().id(), conn);

          for (Neighbor foot : conn.to().neighbors()) {
            int arrivalByFoot = conn.arrival_time() + foot.duration();
            if (arrivalByFoot < earliest.getOrDefault(foot.stop().id(), Integer.MAX_VALUE)) {
              earliest.put(foot.stop().id(), arrivalByFoot);
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
    String best_target = "None";
    for (String target : targets) {
      Integer res = earliest.get(target);
      if (res == null) {
        continue;
      } else if (res < best_result) {
        best_result = res;
        best_target = target;
      }
    }
    if (best_result == Integer.MAX_VALUE) {
      return output;
    }

    Connection conn = predecessor.get(best_target);
    // TODO: we actually don't need target_name
    while (conn != null) {
      output.add(conn);
      if (conn.from().name().toLowerCase().equals(s)) {
        break;
      }
      conn = predecessor.get(conn.from().id());
    }
    Collections.reverse(output);
    return output;
  }

  // #### Private helpers ####

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
