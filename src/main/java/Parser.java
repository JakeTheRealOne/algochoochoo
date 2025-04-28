import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import raw.RTrip;
import raw.RStopTime;

/**
 * Parse GTFS files
 *
 * @author Bilal Vandenberge
 */
public class Parser {
  // #### Public methods ####

  /**
   * Return the list of trips of a GTFS directory
   *
   * @param main_dir The main GTFS directory
   * @param stops The list of stops
   * @return The list of populated trips
   */
  public static ArrayList<Trip> trips(String main_dir, Map<String, Stop> stops) {
    ArrayList<Trip> output = new ArrayList<>();
    Map<String, Trip> map = new LinkedHashMap<>();

    File dir = new File(main_dir);
    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("'" + main_dir + "' is not a directory");
    }

    Map<String, Route> routes = routes(main_dir);
    File[] files = dir.listFiles();
    for (File file : files) {
      if (!file.isDirectory()) {
        continue;
      }
      String csv_file = file + "/trips.csv";
      for (RTrip rtrip : iterate(csv_file, RTrip::new)) {
        Trip trip = new Trip(rtrip, routes);
        output.add(trip);
        map.put(trip.id(), trip);
      }
    }
    populate_trips(main_dir, map, stops);
    for (Trip trip : map.values()) {
      trip.sort();
    }
    return output;
  }

  /**
   * Return the map {id, route} of routes of a GTFS directory
   *
   * @param main_dir The main GTFS directory
   * @return The map of routes
   */
  public static Map<String, Route> routes(String main_dir) {
    Map<String, Route> output = new LinkedHashMap<>();

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
      for (Route route : iterate(csv_file, Route::new)) {
        route.setAgency(file.getName());
        output.put(route.id(), route);
      }
    }
    return output;
  }

  /**
   * Return the map of stops {id, stop} of a GTFS directory
   *
   * @param main_dir The main GTFS directory
   * @return The list of stops
   */
  public static Map<String, Stop> stops(String main_dir) {
    Map<String, Stop> output = new LinkedHashMap<>();

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
      for (Stop stop : iterate(csv_file, Stop::new)) {
        output.put(stop.id(), stop);
      }
    }
    return output;
  }

  // #### Private helpers ####

  /**
   * Populate the trips of a GTFS directory with stop times
   *
   * @param main_dir The main GTFS directory
   * @param trips The list of trips
   * @param stops The list of stops
   */
  private static void populate_trips(
      String main_dir, Map<String, Trip> trips, Map<String, Stop> stops) {
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
      for (RStopTime stop_time : iterate(csv_file, RStopTime::new)) {
        TripElement element = new TripElement(stop_time, stops);
        trips.get(stop_time.trip_id()).add(element);
      }
    }
  }

  /**
   * Iterate trough the rows of a GTFS .csv file
   *
   * @param <T> The object type stored in the GTFS file (e.g Stop, Trip)
   * @param path The path of the CSV file
   * @param factory The factory of objects
   * @return An iterable over the objects stored in the file
   */
  private static <T> Iterable<T> iterate(String path, CsvFactory<T> factory) {
    return () -> {
      try {
        Reader reader = Files.newBufferedReader(Path.of(path));
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(settings);

        Iterator<String[]> it = parser.iterate(reader).iterator();

        return new Iterator<>() {
          @Override
          public boolean hasNext() {
            return it.hasNext();
          }

          @Override
          public T next() {
            return factory.fromRow(it.next());
          }
        };
      } catch (IOException e) {
        throw new IllegalArgumentException("An error occurred while reading: '" + path + "'");
      }
    };
  }
}
