import edu.princeton.cs.algs4.IndexMinPQ;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    runtime_test();
  }

  public static void runtime_test() {
    long start = System.nanoTime();
    AlgoSettings set = new AlgoSettings();
    Graph graph = new Graph("src/main/resources/GTFS", set);
    Algorithm algo = new Algorithm(graph);
    long end = System.nanoTime();
    long duration = end - start;
    System.out.println("[Dijkstra] : Construction en " + duration / 1000000000f
        + " secondes");

    start = System.nanoTime();
    List<Edge> path1 = algo.dijkstra("AUMALE", "FRAITEUR", 7 * 3600 + 0 * 60);
    end = System.nanoTime();
    duration = end - start;
    System.out.println("[Dijkstra] : Requete #1   en " + duration / 1000000000f
        + " secondes");
    start = System.nanoTime();
    List<Edge> path2 = algo.dijkstra("Antwerpen Centraal Station",
        "CHIMAY Petit Virelles", 14 * 3600 + 14 * 60 + 14);
    end = System.nanoTime();
    System.out.println("[Dijkstra] : Requete #2   en " + duration / 1000000000f
        + " secondes");
    duration = end - start;
    start = System.nanoTime();
    List<Edge> path3 = algo.dijkstra(
        "Alveringem Nieuwe Herberg", "Aubange", 10 * 3600 + 30 * 60);
    end = System.nanoTime();
    duration = end - start;
    System.out.println("[Dijkstra] : Requete #3   en " + duration / 1000000000f
        + " secondes");
  }

  public static void run_test_3() {
    AlgoSettings set = new AlgoSettings();
    Graph graph = new Graph("src/test/resources", set);
    Algorithm algo = new Algorithm(graph);
    System.out.println(graph);
  }

  public static void print_test() {
    AlgoSettings set = new AlgoSettings();
    Graph graph = new Graph("src/main/resources/GTFS", set);
    Algorithm algo = new Algorithm(graph);
    List<Edge> path1 = algo.dijkstra("AUMALE", "DELTA", 8 * 3600 + 0 * 60);
    // algo.dijkstra("SCHUMAN", "ETTERBEEK GARE", 10 * 3600 + 30 * 60);
    List<Edge> path2 = algo.dijkstra("Antwerpen Centraal Station",
        "CHIMAY Petit Virelles", 14 * 3600 + 14 * 60 + 14);
    List<Edge> path3 = algo.dijkstra("Alveringem Nieuwe Herberg",
        // List<Edge> path3 = algo.dijkstra("Knokke",

        "Aubange", 10 * 3600 + 30 * 60);
    System.out.println();
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
  }

  // #### Private helpers ####

  /**
   * Execute dijkstra algorithm to find the best path in the graph between two
   * stops
   *
   * @param s The source node
   * @param t The target node
   * @param h The departure time (in seconds)
   */
  private List<Edge> dijkstra(String s, String t, int h) {
    init(s, t, h);

    int treated_nodes = 0; // For debug

    IndexMinPQ<Long> heap = new IndexMinPQ<>(graph.V_card());
    for (Node node : graph.vertices()) {
      heap.insert(node.index(), node.best_cost());
    }

    while (heap.size() > 0) {
      treated_nodes++;
      int index = heap.delMin();
      Node node = graph.vertices.get(index);

      if (node.is_target()) {
        System.out.println("Total path cost: " + node.best_cost());
        return build_solution(node);
      } else if (node.best_cost() == Long.MAX_VALUE) {
        return new ArrayList<Edge>();
      }
      relaxe(node, heap);
    }

    return new ArrayList<Edge>();
  }

  private void relaxe(Node node, IndexMinPQ heap) {
    for (Edge e : node.connections()) {
      boolean is_illegal = check_legality(node, e);
      if (is_illegal)
        continue;
      Node target = e.to();
      // TODO here allow user to set settings of MIN_WAIT_TIME and
      // MAX_WAIT_TIME (and check if a wait time is legal)
      long candidate = node.best_cost()
          + (e.departure_time() - node.best_time()) + e.cost();
      boolean is_best = candidate < target.best_cost();
      if (is_best) {
        target.set_best(candidate, e.departure_time() + e.duration(), e);
        heap.decreaseKey(target.index(), candidate);
      }
    }

    for (Edge e : node.transfers()) {
      Node target = e.to();
      long candidate = node.best_cost() + e.cost();
      boolean is_best = candidate < target.best_cost();
      if (is_best) {
        target.set_best(candidate, node.best_time() + e.duration(), e);
        heap.decreaseKey(target.index(), candidate);
      }
    }
  }

  /**
   * Check if we are allowed to
   *
   * @return If we cannot take the edge because it is illegal
   */
  private boolean check_legality(Node node, Edge edge) {
    return edge.departure_time() < node.best_time();
  }

  /**
   * From a target node, build the list of edge such as the first one is a
   * source and the last one is a target
   *
   * @param target The target reached during the Dijkstra algorithm
   * @return The list of edges constituting the best path
   */
  private static List<Edge> build_solution(Node target) {
    List<Edge> output = new ArrayList<Edge>();

    Edge current = target.best_edge();
    while (!current.from().is_source()) {
      Edge e = current;
      output.add(current);
      current = current.from().best_edge();
    }
    output.add(current);
    Collections.reverse(output);

    return output;
  }

  /**
   * Initialize the attributes to execute an algorithm
   *
   * @param s The name of the source stop
   * @param t The name of the target stop
   * @param h The departure time at the source stop
   */
  private void init(String s, String t, int h) {
    boolean source_found = false;
    boolean target_found = false;
    for (Node node : graph.vertices()) {
      node.init();
      Stop stop = node.stop();
      String name = stop.name();
      if (equal(s, name)) {
        source_found = true;
        node.declare_source();
        node.set_best(0, h, null);
      }
      if (equal(t, name)) {
        target_found = true;
        node.declare_target();
      }
    }
    if (!target_found || !source_found) {
      throw new IllegalArgumentException(
          "At least one source and one target is required");
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
}
