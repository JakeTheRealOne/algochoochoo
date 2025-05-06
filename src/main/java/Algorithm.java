import edu.princeton.cs.algs4.IndexMinPQ;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    print_test();
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
    List<Edge> path1 =
        algo.dijkstra("AUMALE", "HERRMANN-DEBROUX", 7 * 3600 + 0 * 60);
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
    List<Edge> path1 =
        algo.dijkstra("AUMALE", "HERRMANN-DEBROUX", 7 * 3600 + 0 * 60);
    List<Edge> path2 = algo.dijkstra("Antwerpen Centraal Station",
        "CHIMAY Petit Virelles", 14 * 3600 + 14 * 60 + 14);
    List<Edge> path3 = algo.dijkstra(
        "Alveringem Nieuwe Herberg", "Aubange", 10 * 3600 + 30 * 60);
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

    IndexMinPQ<Integer> heap = new IndexMinPQ<>(graph.V_card());
    List<Node> node_list = new ArrayList<>(graph.V_card());
    int i = 0;
    for (Node node : graph.vertices()) {
      heap.insert(i, node.best_cost());
      node.set_index(i);
      node_list.add(node);
      i += 1;
    }

    while (heap.size() > 0) {
      int index = heap.delMin();
      Node node = node_list.get(index);

      if (node.is_target()) {
        int time = node.best_cost();
        System.out.println("Best time found: "
            + String.format(
                "%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60));
        return build_solution(node);
      } else if (node.best_cost() == Integer.MAX_VALUE) {
        System.out.println("No path found");
        return new ArrayList<Edge>();
      }

      for (Edge e : node.connections()) {
        boolean is_illegal = e.departure_time() < node.best_cost();
        if (is_illegal)
          continue;
        Node target = e.to();
        int candidate = e.departure_time() + e.duration();
        boolean is_best = candidate < target.best_cost();
        if (is_best) {
          target.set_best(candidate, e);
          heap.decreaseKey(target.index(), candidate);
        }
      }

      for (Edge e : node.transfers()) {
        Node target = e.to();
        int candidate = node.best_cost() + e.duration();
        boolean is_best = candidate < target.best_cost();
        if (is_best) {
          target.set_best(candidate, e);
          heap.decreaseKey(target.index(), candidate);
        }
      }
    }

    // NOT SUPPOSED TO HAPPEND
    List<Edge> output = new ArrayList<Edge>();
    return output;
  }

  /**
   * From a target node, build the list of edge such as the first
   */
  private List<Edge> build_solution(Node target) {
    List<Edge> output = new ArrayList<Edge>();

    Edge current = target.best_edge();
    while (!current.from().is_source()) {
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
        node.set_best(h, null);
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
