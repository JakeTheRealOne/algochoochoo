import java.io.*;
import java.io.File;
import java.nio.file.*;
import java.util.*;

// #### Univocity test ####

// TODO redo all the fucking imports

// TODO: main/resources ???

/**
 * Print the shortest path based on GTFS datas and certain criteria
 *
 * @author Bilal Vandenberge
 */
public class Main {
  // #### Public methods ####

  /** Do not allow Main instantiation */
  private Main() {}

  /**
   * Main entry point to the project
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    build_graph("src/test/resources/");
  }

  // #### Private helpers ####

  // TODO Add docstrings below

  private static void build_graph(String main_dir) {
    Map<String, TripLine> lines = all_lines(main_dir, all_routes(main_dir));
    populate_lines(main_dir, lines);
    for (Map.Entry<String, TripLine> entry : lines.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }
  }

  private static Map<String, Route> all_routes(String main_dir) {
    Map<String, Route> output = new HashMap<>();

    File dir = new File(main_dir);
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
    }

    File[] files = dir.listFiles();
    for (File file : files) {
      if (!file.isDirectory()) {
        continue;
      }
      String csv_file = file + "/routes.csv";
      for (Route route : Parser.iterate(csv_file, Route::new)) {
        output.put(route.id(), route);
      }
    }
    return output;
  }

  private static Map<String, TripLine> all_lines(String main_dir, Map<String, Route> routes) {
    Map<String, TripLine> output = new HashMap<>();

    File dir = new File(main_dir);
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
    }

    File[] files = dir.listFiles();
    for (File file : files) {
      if (!file.isDirectory()) {
        continue;
      }
      String csv_file = file + "/trips.csv";
      for (Trip trip : Parser.iterate(csv_file, Trip::new)) {
        output.put(trip.id(), new TripLine(trip, routes));
      }
    }
    return output;
  }

  private static Map<String, Stop> all_stops(String main_dir) {
    Map<String, Stop> output = new HashMap<>();

    File dir = new File(main_dir);
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
    }

    File[] files = dir.listFiles();
    for (File file : files) {
      if (!file.isDirectory()) {
        continue;
      }
      String csv_file = file + "/stops.csv";
      for (Stop stop : Parser.iterate(csv_file, Stop::new)) {
        output.put(stop.id(), stop);
      }
    }
    return output;
  }

  private static void populate_lines(String main_dir, Map<String, TripLine> lines) {
    Map<String, Stop> stops = all_stops(main_dir);
    File dir = new File(main_dir);
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
    }

    File[] files = dir.listFiles();
    for (File file : files) {
      if (!file.isDirectory()) {
        continue;
      }
      String csv_file = file + "/stop_times.csv";
      for (StopTime time : Parser.iterate(csv_file, StopTime::new)) {
        TripLine line = lines.get(time.trip_id());
        if (line == null) {
          throw new IllegalArgumentException("Unknown trip id: '" + time.trip_id() + "'");
        }
        line.add(time, stops);
      }
    }
  }


  private static void time_test() {
    String dir = "src/test/resources/";

    // long start = System.nanoTime();
    // List<Route> routes = all_routes_from(dir);
    // double duration = (double) (System.nanoTime() - start) / 1000000000;
    // System.out.println("Total number of routes: " + routes.size());
    // System.out.println("In " + duration + " seconds");
    // for (Route r : routes) {
    //   System.out.println(" " + r);
    // }

    // start = System.nanoTime();
    // List<Trip> trips = all_trips_from(dir);
    // duration = (double) (System.nanoTime() - start) / 1000000000;
    // System.out.println("Total number of trips: " + trips.size());
    // System.out.println("In " + duration + " seconds");
    // for (Trip t : trips) {
    //   System.out.println(" " + t);
    // }

    // start = System.nanoTime();
    // List<Stop> stops = all_stops_from(dir);
    // duration = (double) (System.nanoTime() - start) / 1000000000;
    // System.out.println("Total number of stops: " + stops.size());
    // System.out.println("In " + duration + " seconds");
    // for (Stop s : stops) {
    //   System.out.println(" " + s);
    // }

    // start = System.nanoTime();
    // List<StopTime> stop_times = all_stop_times_from(dir);
    // duration = (double) (System.nanoTime() - start) / 1000000000;
    // System.out.println("Total number of stop times: " + stop_times.size());
    // System.out.println("In " + duration + " seconds");
    // for (StopTime s : stop_times) {
    //   System.out.println(" " + s);
    // }
  }

  // private static List<Route> all_routes_from(String main_dir) {
  //   List<Route> output = new ArrayList<>();

  //   File dir = new File(main_dir);
  //   if (!dir.isDirectory()) {
  //     throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
  //   }

  //   File[] files = dir.listFiles();
  //   for (File file : files) {
  //     if (file.isDirectory()) {
  //       String csv_file = file + "/routes.csv";
  //       Parser.routes(csv_file, output);
  //     }
  //   }
  //   return output;
  // }

  // private static List<Trip> all_trips_from(String main_dir) {
  //   List<Trip> output = new ArrayList<>();

  //   File dir = new File(main_dir);
  //   if (!dir.isDirectory()) {
  //     throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
  //   }

  //   File[] files = dir.listFiles();
  //   for (File file : files) {
  //     if (file.isDirectory()) {
  //       String csv_file = file + "/trips.csv";
  //       Parser.trips(csv_file, output);
  //     }
  //   }
  //   return output;
  // }

  // private static List<Stop> all_stops_from(String main_dir) {
  //   List<Stop> output = new ArrayList<>();

  //   File dir = new File(main_dir);
  //   if (!dir.isDirectory()) {
  //     throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
  //   }

  //   File[] files = dir.listFiles();
  //   for (File file : files) {
  //     if (file.isDirectory()) {
  //       String csv_file = file + "/stops.csv";
  //       Parser.stops(csv_file, output);
  //     }
  //   }
  //   return output;
  // }

  // private static List<StopTime> all_stop_times_from(String main_dir) {
  //   List<StopTime> output = new ArrayList<>();

  //   File dir = new File(main_dir);
  //   if (!dir.isDirectory()) {
  //     throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
  //   }

  //   File[] files = dir.listFiles();
  //   for (File file : files) {
  //     if (file.isDirectory()) {
  //       String csv_file = file + "/stop_times.csv";
  //       Parser.stop_times(csv_file, output);
  //     }
  //   }
  //   return output;
  // }
}

// TODO change the docstring of the Main class
