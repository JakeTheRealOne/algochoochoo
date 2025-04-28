import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Run the application
 *
 * @author Bilal Vandenberge
 */
public class Main {
  // #### Public methods ####

  /**
   * Main entry point to the project
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    run("AUMALE", "FRAITEUR", 8*3600 + 0*60, new AlgoSettings());
    // run("Antwerpen Centraal Station", "CHIMAY Petit Virelles", 14 * 3600 + 14 * 60 + 14, new AlgoSettings());
    // run("Alveringem Nieuwe Herberg", "Aubange", 10 * 3600 + 30 * 60, new AlgoSettings());
  }

  /**
   * Run the pathfinding algorithm and print the result
   * 
   * @param s The source stop (name)
   * @param t The target stop (name)
   * @param h The source departure time
   * @param strict If the source & target name search should be strict or not
   */
  public static void run(String s, String t, int h, AlgoSettings set) {
    final String main_dir = "src/main/resources/GTFS";
    Map<String, Stop> stops = Parser.stops(main_dir);
    ArrayList<Trip> trips = Parser.trips(main_dir, stops);
    ArrayList<Connection> conns = trips_to_conns(trips);
    BallTree bt = new BallTree(stops.values());
    bt.update_footpaths(stops.values(), set);

    ArrayList<String> sources = new ArrayList<>();
    ArrayList<String> targets = new ArrayList<>();
    s = s.toLowerCase();
    t = t.toLowerCase();

    for (Stop stop : stops.values()) {
      String name = stop.name().toLowerCase();
      if (equal(s, name, set.strict_search)) {
        sources.add(stop.id());
      }
      if (equal(t, name, set.strict_search)) {
        targets.add(stop.id());
      }
    }
    ArrayList<Connection> result = CSA(s, sources, t, targets, h, conns, set, stops);
    print_result(result);
  }

  // #### Private helpers ####

  /**
   * Check if the candidate is (partially) equal to the sample
   * 
   * @param candidate The candidate string
   * @param sample The sample string
   * @param strict If the equality is only partial
   */
  private static boolean equal(String candidate, String sample, boolean strict) {
    if (strict) {
      return candidate.equals(sample);
    } else {
      return sample.contains(candidate);
    }
  }

  /**
   * Build a graph G(V, E) where E is the set of temp. link between two stops and V is the set of
   * stops
   *
   * @return The list of connections of the graph
   */
  // private static ArrayList<Connection> build_graph(String main_dir) {
    // Map<String, Stop> stops = Parser.stops(main_dir);
    // // System.out.println("Number of stops: " + stops.size());
    // ArrayList<Trip> trips = Parser.trips(main_dir, stops);
    // // System.out.println("Number of trips: " + trips.size());
    // ArrayList<Connection> conns = trips_to_conns(trips);
    // // System.out.println("Number of conns: " + conns.size());
    // BallTree bt = new BallTree(stops.values());
    // bt.update_footpaths(stops.values());

    // return conns;
  // }

  /**
   * Convert a list of trips to a list of connections
   *
   * @param trips The list of trips
   * @return The list of connections
   */
  private static ArrayList<Connection> trips_to_conns(ArrayList<Trip> trips) {
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

  /**
   * Operate a connection scanning algorithm to get the fastest path from sources to targets
   *
   * @implNote This algorithm takes mutliple source and target nodes, since two differents stops can
   *     have the same names. It returns the best result in all the results for each targets and
   *     sources
   * @param sources The list of source nodes (stop ids)
   * @param source The common name for all sources
   * @param targets The list of targets nodes (stop id)
   * @param target The common name for all targets
   * @param h The source departure time (in seconds)
   * @param conns The connections between the stops
   * @exception IllegalArgumentException If targets or sources are empty
   * @return The list of connection to takes to arrive at destination as fast as possible
   */
  private static ArrayList<Connection> CSA(String source_name,
      ArrayList<String> sources, String target_name, ArrayList<String> targets, int h, ArrayList<Connection> conns, AlgoSettings set, Map<String, Stop> stops) {
    if (sources.isEmpty() || targets.isEmpty()) {
      throw new IllegalArgumentException(
          "At least one source and one target is required (s: "
              + sources.size()
              + ", t: "
              + targets.size()
              + ")");
    }

    Map<String, Integer> earliest = new LinkedHashMap<>();
    Map<String, Connection> predecessor = new LinkedHashMap<>();

    for (Connection conn : conns) {
      earliest.put(conn.from().id(), Integer.MAX_VALUE);
      earliest.put(conn.to().id(), Integer.MAX_VALUE);
    }
    for (String s : sources) {
      earliest.put(s, h);
      for (Neighbor neighbor : stops.get(s).neighbors()) {
        if (earliest.getOrDefault(neighbor.stop().id(), Integer.MAX_VALUE) > h + neighbor.duration()) {
          earliest.put(neighbor.stop().id(), h + neighbor.duration());
          predecessor.put(
            neighbor.stop().id(),
            new Connection(stops.get(s), neighbor.stop(), h, neighbor.duration()));
        }
      }
    }

    for (Connection conn : conns) {
      if (!earliest.containsKey(conn.from().id())) continue;
      int ahead = 0;
      Connection p = predecessor.getOrDefault(conn.from().id(), null);
      if (p != null) {
        if (p.trip() != conn.trip()) {
          ahead = set.min_ahead_time;
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

    ArrayList<Connection> output = new ArrayList<>();
    int best_result = Integer.MAX_VALUE;
    String best_target = "None";
    for (String t : targets) {
      Integer res = earliest.get(t);
      if (res == null) {
        continue;
      } else if (res < best_result) {
        best_result = res;
        best_target = t;
      }
    }
    if (best_result == Integer.MAX_VALUE) {
      return output;
    }

    Connection conn = predecessor.get(best_target);
    // TODO: we actually don't need target_name
    while (conn != null) {
      output.add(conn);
      if (conn.from().name().toLowerCase().equals(source_name)) {
        break;
      }
      conn = predecessor.get(conn.from().id());
    }
    Collections.reverse(output);
    return output;
  }

  private static int distance(Stop s1, Stop s2) { // TODO remove
    double lat1 = Math.toRadians(s1.latitude());
    double lon1 = Math.toRadians(s1.longitude());
    double lat2 = Math.toRadians(s2.latitude());
    double lon2 = Math.toRadians(s2.longitude());
    double dLat = lat2 - lat1;
    double dLon = lon2 - lon1;
    double a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    final double R = 6371000;
    return (int) (R * c);
  }

  private static void print_result(ArrayList<Connection> path) {
    System.out.println("──────────────────────── RESULTS ────────────────────────");

    int i = 0;
    int n = path.size();

    // for (int j = 0; j < n; ++ j) {
    //   System.out.println(path.get(j).from());
    //   int dist = distance(path.get(j).from(), path.get(j).to());
    //   System.out.println("Walk distance: " + dist);
    //   Neighbor fake = new Neighbor(path.get(j).to(), dist);
    //   System.out.println("Walk duration: " + fake.duration());
    // }

    while (i < n) {
      Trip trip = path.get(i).trip();
      int time = path.get(i).departure_time();

      System.out.print(
          path.get(i).directive()
              + " from "
              + path.get(i).from().name()
              + " ("
              + String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60)
              + ")");

      int j = i;
      while (j < n && path.get(j).trip() == trip) {
        ++j;
      }
      time = path.get(j - 1).arrival_time();
      System.out.println(
          " to "
              + path.get(j - 1).to().name()
              + " ("
              + String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60)
              + ")");
      i = j;
    }
  }

  // #### Speed tests ####

  private static void main_runtime_test() {
  //   long start = System.nanoTime();
  //   ArrayList<Connection> conns = build_graph("src/main/resources/GTFS");
  //   // Map<
  //   print_result(
  //       CSA(
  //           "STIB-8733",
  //           "STIB-8162",
  //           5 * 3600 + 35 * 60 + 40,
  //           conns)); // DE Gare de l'Ouest À Stockel
  //   print_result(
  //       CSA(
  //           "DELIJN-153267",
  //           "SNCB-8872009",
  //           7 * 3600 + 7 * 60 + 7,
  //           conns)); // DE Anvers sud À Charleroi Central
  //   print_result(CSA("DELIJN-504014", "SNCB-8866654", 10 * 3600 + 30 * 60, conns));
  //   long end = System.nanoTime();
  //   // System.out.println("Main execution time: " + (end - start) / 1_000_000 + " ms");
  //   start = System.nanoTime();
  }
}
