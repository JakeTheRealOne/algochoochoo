import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.Map;
import java.util.Comparator;
import java.util.ListIterator;

/**
 * Run the application
 *
 * @author Bilal Vandenberge
 */
public class Main {
  /** Do not allow Main instantiation */
  private Main() {}

  // #### Public methods ####

  /**
   * Main entry point to the project
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    main_runtime_test();
  }

  // #### Private helpers ####

  /**
   * Build a graph G(V, E) where E is the set of temp. link between two stops and V is the set of
   * stops
   * 
   * @return The list of connections of the graph
   */
  private static ArrayList<Connection> build_graph(String main_dir) {
    Map<String, Stop> stops = Parser.stops(main_dir);
    System.out.println("Number of stops: " + stops.size());
    ArrayList<Trip> trips = Parser.trips(main_dir, stops);
    System.out.println("Number of trips: " + trips.size());
    ArrayList<Connection> conns = trips_to_conns(trips);
    System.out.println("Number of conns: " + conns.size());
    return conns;
  }

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
        Connection conn = new Connection(seq.get(i), seq.get(i + 1), trip.id());
        output.add(conn);
      }
    }
    output.sort(Comparator.comparingInt(Connection::departure_time));
    return output;
  }

  private static ArrayList<Connection> CSA(String s, String t, int h, ArrayList<Connection> conns) {
    Map<String, Integer> earliest = new LinkedHashMap<>();
    Map<String, Connection> predecessor = new LinkedHashMap<>();

    for (Connection conn : conns) {
      earliest.put(conn.from().id(), Integer.MAX_VALUE);
      earliest.put(conn.to().id(), Integer.MAX_VALUE);
    }
    earliest.put(s, h);

    for (Connection conn : conns) {
      if (!earliest.containsKey(conn.from().id())) continue;
      if (!(conn.departure_time() < earliest.get(conn.from().id()))) {
        if (conn.arrival_time() < earliest.get(conn.to().id())) {
          earliest.put(conn.to().id(), conn.arrival_time());
          predecessor.put(conn.to().id(), conn);
        }
      }
    }


    ArrayList<Connection> output = new ArrayList<>();
    if (!predecessor.containsKey(t)) {
        return output; 
    }
    String current = t;
    while (!current.equals(s)) {
        Connection conn = predecessor.get(current);
        output.add(conn);
        current = conn.from().id();
    }
    Collections.reverse(output);
    return output;
  }

  private static void print_result(ArrayList<Connection> path) {
    System.out.println("--- RESULTS ---");

    int i = 0;
    int n = path.size();

    while (i < n) {
      String trip_id = path.get(i).trip_id();
      int time = path.get(i).departure_time();
      System.out.print("Prendre le TRANSPORT depuis " + path.get(i).from().name() + " à " + String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60));
      
      int j = i;
      while (j < n && path.get(j).trip_id().equals(trip_id)) {
        ++j;
      }
      time = path.get(j-1).arrival_time();
      System.out.println(" puis descendre à " + path.get(j-1).to().name() + " à " + String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60));
      i = j;
    }
    System.out.println("--- ------- ---");
  }

  // #### Speed tests ####

  private static void main_runtime_test() {
    long start = System.nanoTime();
    ArrayList<Connection> conns = build_graph("src/main/resources/GTFS");
    print_result(CSA("STIB-8733", "STIB-8162", 5*3600 + 35*60 + 40, conns));
    print_result(CSA("SNCB-8007817", "STIB-8162", 5*3600 + 35*60 + 40, conns));
    print_result(CSA("DELIJN-320843", "TEC-Baegd741", 5*3600 + 35*60 + 40, conns));
    long end = System.nanoTime();
    System.out.println("Main execution time: " + (end - start) / 1_000_000 + " ms");
    start = System.nanoTime();
  }
}
