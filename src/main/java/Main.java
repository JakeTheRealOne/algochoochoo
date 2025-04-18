import java.io.*;
import java.io.File;
import java.nio.file.*;
import java.util.*;

// #### Univocity test ####
import java.util.List;
import java.util.ArrayList;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

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
    time_test();
  }

  // #### Private helpers ####

  // TODO Add docstrings below

  private static void time_test() {
    String dir = "src/main/resources/GTFS/";

    long start = System.nanoTime();
    List<Route> routes = all_routes_from(dir);
    double duration = (double) (System.nanoTime() - start) / 1000000000;
    System.out.println("Total number of routes: " + routes.size());
    System.out.println("In " + duration + " seconds");

    start = System.nanoTime();
    List<Trip> trips = all_trips_from(dir);
    duration = (double) (System.nanoTime() - start) / 1000000000;
    System.out.println("Total number of trips: " + trips.size());
    System.out.println("In " + duration + " seconds");

    start = System.nanoTime();
    List<Stop> stops = all_stops_from(dir);
    duration = (double) (System.nanoTime() - start) / 1000000000;
    System.out.println("Total number of stops: " + stops.size());
    System.out.println("In " + duration + " seconds");

    start = System.nanoTime();
    List<StopTime> stop_times = all_stop_times_from(dir);
    duration = (double) (System.nanoTime() - start) / 1000000000;
    System.out.println("Total number of stop times: " + stop_times.size());
    System.out.println("In " + duration + " seconds");
  }

  private static List<Route> all_routes_from(String main_dir) {
    List<Route> output = new ArrayList<>();

    File dir = new File(main_dir);
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
    }

    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        String csv_file = file + "/routes.csv";
        Parser.routes(csv_file, output);
      }
    }
    return output;
  }

  private static List<Trip> all_trips_from(String main_dir) {
    List<Trip> output = new ArrayList<>();

    File dir = new File(main_dir);
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
    }

    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        String csv_file = file + "/trips.csv";
        Parser.trips(csv_file, output);
      }
    }
    return output;
  }

  private static List<Stop> all_stops_from(String main_dir) {
    List<Stop> output = new ArrayList<>();

    File dir = new File(main_dir);
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
    }

    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        String csv_file = file + "/stops.csv";
        Parser.stops(csv_file, output);
      }
    }
    return output;
  }

  private static List<StopTime> all_stop_times_from(String main_dir) {
    List<StopTime> output = new ArrayList<>();

    File dir = new File(main_dir);
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
    }

    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        String csv_file = file + "/stop_times.csv";
        Parser.stop_times(csv_file, output);
      }
    }
    return output;
  }
}

// TODO change the docstring of the Main class
