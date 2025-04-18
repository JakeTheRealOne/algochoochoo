import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Parse a CSV file
 *
 * @author Bilal Vandenberge
 */
public class Parser {
  // #### Public methods ####

  /** Do not allow Parser instantiation */
  private Parser() {}

  /**
   * Convert the content of a stops file into a list of Stop objects
   *
   * @param path Path of the CSV file
   * @return List of stops
   */
  public static List<Stop> stops(String path) {
    return generic(path, Stop::new);
  }

  /**
   * Convert the content of a routes file into a list of Route objects
   *
   * @param path Path of the CSV file
   * @return List of routes
   */
  public static List<Route> routes(String path) {
    return generic(path, Route::new);
  }

  /**
   * Convert the content of a trips file into a list of Trip objects
   *
   * @param path Path of the CSV file
   * @return List of trips
   */
  public static List<Trip> trips(String path) {
    return generic(path, Trip::new);
  }

  /**
   * Convert the content of a stop_times file into a list of StopTime objects
   *
   * @param path Path of the CSV file
   * @return List of stop times
   */
  public static List<StopTime> stop_times(String path) {
    return generic(path, StopTime::new);
  }

  // #### Private helpers ####

  /** Expected header for routes files */
  private static final String[] ROADS_HEADER = {
    "route_id", "route_short_name", "route_long_name", "route_type"
  };

  /**
   * Check if the content of a routes file is valid
   *
   * @exception IllegalArgumentException If the content is invalid
   */
  private static void check_routes_integrity(List<String[]> content) {
    if (content.isEmpty()) {
      throw new IllegalArgumentException("Routes file cannot be empty");
    } else if (!Arrays.equals(content.get(0), ROADS_HEADER)) {
      throw new IllegalArgumentException("Invalid routes header");
    }
  }

  // TODO add integrity check

  /**
   * Convert the content of a CSV file to a list of objects
   *
   * @param <T> Object type
   * @param path Path of the CSV file
   * @param factory Object factory
   * @return List of objects
   */
  public static <T> List<T> generic(String path, CsvFactory<T> factory) {
    List<T> output = new ArrayList<T>();

    CsvParserSettings settings = new CsvParserSettings();
    settings.setHeaderExtractionEnabled(true); // skip header
    CsvParser parser = new CsvParser(settings);

    try (Reader reader = Files.newBufferedReader(Path.of(path))) {
        for (String[] row : parser.iterate(reader)) {
            output.add(factory.fromRow(row));
        }
    } catch (IOException e) {
      throw new IllegalArgumentException("An error occured while reading: '" + path + "'");
    } 
    return output;
  }
}
