package algochoochoo.cli;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.exec.CommandLine;

import algochoochoo.graph.Edge;
import algochoochoo.graph.Graph;
import algochoochoo.graph.GraphSettings;
import algochoochoo.parsing.RouteType;
import algochoochoo.parsing.StopTime;
import algochoochoo.parsing.Trip;
import algochoochoo.query.AlgoPriority;
import algochoochoo.query.AlgoResult;
import algochoochoo.query.AlgoSettings;
import algochoochoo.query.Algorithm;

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
    System.out.println("### Trip finder environment\n");
    AlgoSettings algoset = new AlgoSettings(args);
    GraphSettings graphset = new GraphSettings(args);
    System.out.println();

    System.out.println("### Graph building...\n");
    Graph graph = new Graph(graphset);
    Algorithm algo = new Algorithm(graph, algoset);
    System.out.println(graph + "\n");

    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.print("> ");
      System.out.flush();
      String line;
      try {
        line = scanner.nextLine().strip();
      } catch (Exception e) {
        break;
      }
      if (EXIT_KEYWORDS.contains(line))
        break;
      CommandLine tmp_command = CommandLine.parse(line);
      CommandLine command = new CommandLine(tmp_command.getExecutable());
      for (String arg : tmp_command.getArguments()) {
        command.addArgument(arg, true);
      }
      if (command == null || command.getExecutable() == null)
        continue;
      if (command.getExecutable().equals("search")) {
        if (command.getArguments().length != 3) {
          System.err.println("Usage: search <s> <t> HH:MM:SS\n");
          continue;
        }

        String s_input = command.getArguments()[0];
        String t_input = command.getArguments()[1];
        int h_input = 0;
        try {
          h_input = StopTime.read_time(command.getArguments()[2]);
          h_input = Math.floorMod(h_input, 24 * 3600);
        } catch (IllegalArgumentException e) {
          System.err.println("Error: " + e.getMessage());
          continue;
        }

        System.out.println("### Path query\n");
        System.out.println("s = " + s_input);
        System.out.println("t = " + t_input);
        System.out.println("h = " + command.getArguments()[2] + "\n");

        System.out.println("### Settings\n");
        graphset.print();
        algoset.print();
        System.out.println();

        AlgoResult result = new AlgoResult();
        try {
          result = algo.dijkstra(s_input, t_input, h_input);
        } catch (IllegalArgumentException e) {
          System.err.println("Error: " + e.getMessage());
          continue;
        }
        View.print(result, h_input);
        System.out.println();
      } else if (command.getExecutable().equals("update")) {
        if (command.getArguments().length != 3 || !command.getArguments()[1].equals("=")) {
          System.err.println("Usage: update <setting> = <value>\n");
          continue;
        }
        switch (command.getArguments()[0]) {
          case "priority":
            switch (command.getArguments()[2]) {
              case "time":
                algoset.priority = AlgoPriority.TOTAL_DURATION;
                break;
              case "trips":
                algoset.priority = AlgoPriority.TRIP_CHANGES;
                break;
              default:
                System.err
                    .println("Error: unknown priority: " + command.getArguments()[2] + " available: (time, trips)");
                break;
            }
            break;
          case "gtfs-path":
            String previous_path = graphset.GTFS_path;
            graphset.GTFS_path = command.getArguments()[2];
            try {
              graph.reload(graphset);
              System.err.println("Reloading graph...");
            } catch (Exception e) {
              System.err.println("Error: unable to reload graph - " + e.getMessage());
              graphset.GTFS_path = previous_path;
            }
            break;
          case "foot-radius":
            int previous_radius = graphset.foot_radius;
            try {
              graphset.foot_radius = (int) Math.round(Double.parseDouble(command.getArguments()[2]));
              System.err.println("Reloading graph...");
              graph.reload(graphset);
            } catch (Exception e) {
              System.err.println("Error: unable to reload graph - " + e.getMessage());
              graphset.foot_radius = previous_radius;
            }
            break;
          case "foot-weight":
          case "metro-weight":
          case "tram-weight":
          case "bus-weight":
          case "train-weight":
            double w;
            try {
              w = Double.parseDouble(command.getArguments()[2]);
            } catch (Exception e) {
              System.err.println("Error: cannot apply weight - " + e.getMessage() + "\n");
              continue;
            }
            if (w <= 0.0 || w > 10.0) {
              System.err.println("Error: transport weight is not in the range (0, 10]\n");
              continue;
            }
            RouteType type = command.getArguments()[0].equals("foot-weight") ? RouteType.FOOT
                : command.getArguments()[0].equals("metro-weight") ? RouteType.METRO
                    : command.getArguments()[0].equals("tram-weight") ? RouteType.TRAM
                        : command.getArguments()[0].equals("bus-weight") ? RouteType.BUS : RouteType.TRAIN;
            algoset.weights.put(type, w);
            break;
          default:
            System.err.println("Error: unknown setting: " + command.getArguments()[0] + ", see help to seek help");
            break;
        }
      } else if (command.getExecutable().equals("get")) {
        if (command.getArguments().length != 1) {
          System.err.println("Usage: get <setting>");
        }
        switch (command.getArguments()[0]) {
          case "priority":
            System.out.println("priority = " + algoset.priority);
            break;
          case "gtfs-path":
            System.out.println("GTFS path = " + graphset.GTFS_path);
            break;
          case "foot-radius":
            System.out.println("Footpath radius = " + graphset.foot_radius + " meters");
            break;
          case "weights":
            System.out.println("weights = [foot: " + algoset.weights.getOrDefault(RouteType.FOOT, 1.0) + ", metro: "
                + algoset.weights.getOrDefault(RouteType.METRO, 1.0) + ", bus: "
                + algoset.weights.getOrDefault(RouteType.BUS, 1.0) + ", tram: "
                + algoset.weights.getOrDefault(RouteType.TRAM, 1.0) + ", train: "
                + algoset.weights.getOrDefault(RouteType.TRAIN, 1.0) + "]&");
            break;
          case "foot-weight":
          case "metro-weight":
          case "tram-weight":
          case "bus-weight":
          case "train-weight":
            RouteType type = command.getArguments()[0].equals("foot-weight") ? RouteType.FOOT
                : command.getArguments()[0].equals("metro-weight") ? RouteType.METRO
                    : command.getArguments()[0].equals("tram-weight") ? RouteType.TRAM
                        : command.getArguments()[0].equals("bus-weight") ? RouteType.BUS : RouteType.TRAIN;
            System.out.println(command.getArguments()[0] + " = " + algoset.weights.getOrDefault(type, 1.0));
            break;
          default:
            System.err.println("Error: unknown setting: " + command.getArguments()[0] + ", see help to seek help");
            break;
        }
      } else if (command.getExecutable().equals("update")) {
      } else if (command.getExecutable().equals("help")) {
        System.out.println("### Help page:");
        System.out.println("Algochoochoo Env v1.0 - Bilal Vandenberge\n\nCommands:\n");
        System.out.println(" search <s> <t> <h>\t\tSearch the minimum cost path from s to t at h");
        System.out.println(" update <setting> = <value>\tUpdate a settings with a new value");
        System.out.println(" get <setting>\t\t\tGet a settings value");
        System.out.println(" help\t\t\t\tPrint help page");
        System.out.println("\nSettings:\n name\t\tdescription\t\t\t\t\t\t\tvalues\n");
        System.out.println(" gtfs-path\tThe path of the GTFS directory\t\t\t\t\tstring");
        System.out.println(" foot-radius\tThe maximum distance of a footpath\t\t\t\tpositive integer");
        System.out.println(" priority\tThe criterion that is minimized by the search algorithm\t\ttime OR trips");
        System.out.println(" weights\tThe weights for each transport type\t\t\t\tmap[foot, metro, tram, bus, train]");
      } else {
        System.err.println("Error: unknown command: " + command.getExecutable() + ", see help to seek help");
      }
      System.out.println();
    }
    scanner.close();
  }

  /** Construct a default Out object */
  public View() {
  }

  /**
   * Print a result, concatenate the edges into trips
   *
   * @param result The result from the algorithm
   * @param h      The departure time
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

  // #### Constants ####

  private static final List<String> EXIT_KEYWORDS = List.of("exit", "quit", "q");

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
