package algochoochoo.query;

import algochoochoo.cli.View;
import algochoochoo.graph.*;
import algochoochoo.parsing.Stop;
import algochoochoo.parsing.StopTime;
import edu.princeton.cs.algs4.IndexMinPQ;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
   * Run the CLI
   *
   * @param args User preferences for algo and graph
   */
  public static void main(String[] args) {
    if (args.length < 3) {
      System.err.println("Usage: java -jar target/tui.jar s t HH:MM:SS [--priority] [--gtfs-path] [--foot-radius] [--foot-weight] [--metro-weight] [--train-weight] [--tram-weight] [--bus-weight]");
      System.exit(1);
    }

    String s_input = args[0];
    String t_input = args[1];
    int h_input = 0;
    try {
      h_input = StopTime.read_time(args[2]);
      h_input = Math.floorMod(h_input, 24 * 3600);
    } catch (IllegalArgumentException e) {
      System.err.println("Error: " + e.getMessage());
      System.exit(1);
    }

    System.out.println("### Path query\n");
    System.out.println("s = " + s_input);
    System.out.println("t = " + t_input);
    System.out.println("h = " + args[2] + "\n");

    AlgoSettings algoset = new AlgoSettings(args);
    GraphSettings graphset = new GraphSettings(args);
    System.out.println("### Settings\n");
    graphset.print();
    algoset.print();
    System.out.println();

    System.out.println("### Graph building...\n");
    Graph graph = new Graph(graphset);
    Algorithm algo = new Algorithm(graph, algoset);
    System.out.println(graph + "\n");

    AlgoResult result = new AlgoResult();
    try {
      result = algo.dijkstra(s_input, t_input, h_input);
    } catch (IllegalArgumentException e) {
      System.err.println("Error: " + e.getMessage());
      System.exit(1);
    }
    View.print(result, h_input);
  }

  /* Made on Earth by humans */

  /**
   * Construct a new Algorithm object
   *
   * @param G   The graph of the algorithm
   * @param set The algorithm settings
   */
  public Algorithm(Graph G, AlgoSettings set) {
    graph = G;
    settings = set;
  }

  // #### Private helpers ####

  /**
   * Execute dijkstra algorithm to find the best path in the graph between two
   * stops
   *
   * @param s The source node
   * @param t The target node
   * @param h The departure time (in seconds)
   * @return The best path from s to t at h
   */
  public AlgoResult dijkstra(String s, String t, int h) {
    init(s, t, h);
    AlgoResult result = new AlgoResult();
    // Debug:
    int visited_vertices = 0;
    long start_time = System.nanoTime();

    IndexMinPQ<Long> heap = new IndexMinPQ<>(graph.V_card());
    for (Node node : graph.vertices()) {
      heap.insert(node.index(), node.best_cost());
    }

    while (heap.size() > 0) {
      int index = heap.delMin();
      Node node = graph.vertices().get(index);
      ++visited_vertices;

      if (node.is_target()) {
        // Best path found
        result.path = build_solution(node);
        break;
      } else if (node.best_cost() == Long.MAX_VALUE) {
        // No path found
        break;
      }
      relaxe(node, heap);
    }

    long end_time = System.nanoTime();
    result.runtime = ((double) (end_time - start_time)) / 1_000_000_000f;
    result.preruntime = graph.reload_runtime();
    result.visited_vertices = visited_vertices;
    return result;
  }

  private void relaxe(Node node, IndexMinPQ heap) {
    int time = node.best_time();
    for (Edge e : node.connections()) {
      Node target = e.to();
      long candidate = settings.cost_function(node, e);
      boolean is_best = candidate < target.best_cost();
      if (is_best) {
        target.set_best(candidate, e.departure_time() + e.duration(), e);
        heap.decreaseKey(target.index(), candidate);
      }
    }

    for (Edge e : node.transfers()) {
      Node target = e.to();
      long candidate = settings.cost_function(node, e);
      boolean is_best = candidate < target.best_cost();
      if (is_best) {
        target.set_best(candidate, node.best_time() + e.duration(), e);
        heap.decreaseKey(target.index(), candidate);
      }
    }
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
    while (current != null && !current.from().is_source()) {
      output.add(current);
      current = current.from().best_edge();
    }
    if (current != null)
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
          "At least one existing source and one existing target is required");
    }
  }

  /**
   * Check if two strings are equals
   */
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
