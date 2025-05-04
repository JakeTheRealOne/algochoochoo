import java.util.ArrayList;
import java.util.List;
// TEST: TODO Remove
import java.util.random.*;

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
    run_test_1();
  }

  public static void run_test_1() {
    AlgoSettings set = new AlgoSettings();
    Graph graph = new Graph("src/main/resources/GTFS", set);
    Algorithm algo = new Algorithm(graph);
    List<Edge> path1 = algo.dijkstra("AUMALE", "HERRMANN-DEBROUX", 7 * 3600 + 0 * 60);
    List<Edge> path2 =
        algo.dijkstra(
            "Antwerpen Centraal Station", "CHIMAY Petit Virelles", 14 * 3600 + 14 * 60 + 14);
    List<Edge> path3 = algo.dijkstra("Alveringem Nieuwe Herberg", "Aubange", 10 * 3600 + 30 * 60);
    View.print(path1);
    System.out.println();
    View.print(path2);
    System.out.println();
    View.print(path3);
  }

  /**
   * Construct a new Algorithm object
   *
   * @param G The graph of the algorithm
   * @param set The algorithm settings
   */
  public Algorithm(Graph G) {
    graph = G;
    settings = G.settings();
    sources = new ArrayList<>();
    targets = new ArrayList<>();
  }

  // public static void run_test_1() {
  //   AlgoSettings set = new AlgoSettings();
  //   Graph graph = new Graph("src/main/resources/GTFS", set);
  //   Algorithm algo = new Algorithm(graph, set);
  //   List<Connection> path1 = algo.CSA("AUMALE", "HERRMANN-DEBROUX", 7 * 3600 + 0 * 60);
  //   // List<Connection> path2 = algo.CSA("Antwerpen Centraal Station", "CHIMAY Petit Virelles",
  // 14 *
  //   // 3600 + 14 * 60 + 14);
  //   List<Connection> path3 = algo.CSA("Alveringem Nieuwe Herberg", "Aubange", 10 * 3600 + 30 *
  // 60);
  //   View.print(path1);
  //   System.out.println();
  //   // View.print(path2);
  //   // System.out.println();
  //   View.print(path3);
  //   // System.out.println();
  // }

  // public static void run_test_2() {
  //   AlgoSettings set = new AlgoSettings();
  //   Graph graph = new Graph("src/main/resources/GTFS", new AlgoSettings());
  //   Graph graph2 = new Graph("src/main/resources/GTFS", set);
  //   Algorithm algo = new Algorithm(graph, new AlgoSettings());
  //   Algorithm algo2 = new Algorithm(graph2, set);
  //   Random rand = new Random();
  //   int min = 3 * 3600;
  //   int max = 18 * 3600;
  //   List<Stop> stop_set = new ArrayList<>(Parser.stops("src/main/resources/GTFS").values());
  //   while (true) {
  //     int i = rand.nextInt(stop_set.size());
  //     int j = rand.nextInt(stop_set.size());
  //     String s = stop_set.get(i).name();
  //     String t = stop_set.get(j).name();
  //     int h = rand.nextInt(max - min + 1) + min;
  //     List<Connection> path_safe = algo.CSA(s, t, h);
  //     List<Connection> path_unsafe = algo2.CSA(s, t, h);
  //     if (path_safe.getLast().arrival_time() != path_unsafe.getLast().arrival_time()) {
  //       System.out.println("ITS OVER " + path_safe.size() + " " + path_unsafe.size());
  //       // for (int e = 0; e < path_safe.size(); e++) {
  //       //   if (!Objects.equals(path_safe.get(e), path_unsafe.get(e))) {
  //       //     System.out.println("Difference at index " + e + ": " + path_safe.get(e) + " vs " +
  //       // path_unsafe.get(e));
  //       //     break;
  //       //   }
  //       // }
  //       View.print(path_safe);
  //       System.out.println();
  //       View.print(path_unsafe);
  //     } else {
  //       System.out.println("v");
  //     }
  //     System.out.println();
  //     System.out.println();
  //   }
  // }

  // /**
  //  * Execute the Connexion scanning algorithm
  //  *
  //  * @param s The source node
  //  * @param t The target node
  //  * @param h The source time
  //  */
  // public List<Connection> CSA(String s, String t, int h) {
  //   init(s, t, h);

  //   for (Stop stop : graph.vertices()) {
  //     stop.init_for_algo();
  //   }

  //   for (String source : sources) {
  //     Stop stop = graph.get_stop(source);
  //     stop.cost().set_duration(h);
  //     stop.relaxe_transfers();
  //   }

  //   for (Connection conn : graph.edges()) {
  //     conn.to().evaluate(conn);
  //   }

  //   return build_solution();
  // }

  public List<Edge> dijkstra(String s, String t, int h) {
    init(s, t, h);
    List<Edge> output = new ArrayList<Edge>();
    return output;
  }

  // #### Private helpers ####

  /**
   * Initialize the attributes to execute an algorithm
   *
   * @param s_ The name of the source stop
   * @param t The name of the target stop
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
          "At least one source and one target is required (number of sources: "
              + sources.size()
              + ", number of targets: "
              + targets.size()
              + ")");
    }
  }

  // /**
  //  * Build the solution (path) after a CSA algorithm
  //  *
  //  * @return The best path considering current algorithm settings
  //  */
  // private List<Connection> build_solution() {
  //   List<Connection> output = new ArrayList<>();
  //   Stop best_target = get_best_target();
  //   if (best_target == null) return output;

  //   Connection conn = best_target.predecessor();
  //   while (conn != null) {
  //     output.add(conn);
  //     if (equal(s, conn.from().name())) {
  //       break;
  //     }
  //     conn = conn.from().predecessor();
  //   }

  //   Collections.reverse(output);
  //   return output;
  // }

  // /**
  //  * Get the target accessed the earliest after a CSA algorithm
  //  *
  //  * @return The best target
  //  */
  // private Stop get_best_target() {
  //   int earliest = Integer.MAX_VALUE;
  //   Stop output = null;
  //   for (String target : targets) {
  //     Stop stop = graph.get_stop(target);
  //     int current = stop.cost().duration();
  //     if (current < earliest) {
  //       earliest = current;
  //       output = stop;
  //     }
  //   }
  //   return output;
  // }

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
