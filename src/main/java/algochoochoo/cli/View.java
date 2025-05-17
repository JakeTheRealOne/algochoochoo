package algochoochoo.cli;

import algochoochoo.graph.*;
import algochoochoo.query.AlgoResult;
import algochoochoo.query.AlgoSettings;
import algochoochoo.query.Algorithm;
import algochoochoo.parsing.StopTime;
import algochoochoo.parsing.Trip;
import java.util.List;

/**
 * Handle the project view
 *
 * @author Bilal Vandenberge
 */
public class View {
  // #### Public methods ####

  /**
   * Run the REPL
   *
   * @param args Irrelevant here
   */
  public static void main(String[] args) {
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

  /** Construct a default Out object */
  public View() {}

  /**
   * Print a result, concatenate the edges into trips
   *
   * @param result The result from the algorithm
   * @param h The departure time
   */
  public static void print(AlgoResult result, int h) {
    System.out.println("### Results\n");
    List<Edge> path = result.path;
    int n = path == null ? 0 : path.size();
    if (n == 0) {
      System.out.println("Empty path");
    }
    int time = h;
    for (int i = 0; i < n; ++i) {
      Edge current = path.get(i);
      Trip trip = current.trip();
      if (current.is_connection()) {
        time = current.departure_time();
      }
      System.out.print(current.directive());
      System.out.print(
          " from " + current.from().stop().name() + beautiful_time(time));
      time += current.duration();

      int j = i + 1;
      while (j < n && path.get(j).trip() == trip) {
        if (path.get(j).is_connection()) {
          time = path.get(j).departure_time();
        }
        time += path.get(j).duration();
        ++j;
      }
      i = j - 1;
      System.out.println(
          " to " + path.get(i).to().stop().name() + beautiful_time(time));
    }
    System.out.println();
    show_debug_info(result);
  }

  // #### Private helpers ####

  /**
   * Convert a time into beautiful string
   *
   * @param time The time (in seconds)
   */
  private static String beautiful_time(int time) {
    return String.format(
        " (%02d:%02d:%02d)", time / 3600, (time % 3600) / 60, time % 60);
  }

  /**
   * Print the debug informations of the execution of an algorithm
   *
   * @param result The result from the algorithm
   */
  private static void show_debug_info(AlgoResult result) {
    System.out.println("### Debug infos\n");
    System.out.println("Precomputing runtime: " + result.preruntime + "s");
    System.out.println("Query runtime: " + result.runtime + "s");
    System.out.println("Explored stops: " + result.visited_vertices);
  }
}
