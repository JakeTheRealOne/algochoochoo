import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Parse a CSV file
 *
 * @author Bilal Vandenberge
 */
public class Parser {
  // #### Public methods ####

  /** Construct a new Parser object */
  public Parser() {}

  /**
   * Convert the content of a Stops file into a list of Stop objects
   *
   * @param path Path of the CSV file
   * @return List of stops
   */
  public static List<Stop> stops(String path) {
    return generic(path, Stop::new);
  }

  /**
   * Convert the content of a Routes file into a list of Route objects
   *
   * @param path Path of the CSV file
   * @return List of routes
   */
  public static List<Route> routes(String path) {
    return generic(path, Route::new);
  }

  // #### Private helpers ####

  /**
   * Convert the content of a CSV file into a list of string arrays
   *
   * @param path Path of the CSV file
   * @return List of file rows (as an array of strings)
   */
  private static List<String[]> regular(String path) {
    List<String[]> output = new ArrayList<>();

    try (BufferedReader file = new BufferedReader(new FileReader(path))) {
      String line;
      while ((line = file.readLine()) != null) {
        output.add(csv_to_array(line));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return output;
  }

  /**
   * Convert a CSV row to an array of string
   * 
   * @param row CSV row
   * @return Array of all elements of the CSV row, seperated by a coma
   */
  private static String[] csv_to_array(String row) {
    String regex = "\"([^\"]*)\"|([^\",]+)"; // < Quotes and comas separator
    List<String> values = new ArrayList<>();
    Matcher matcher = Pattern.compile(regex).matcher(row);

    // Parse the CSV row
    while (matcher.find()) {
      if (matcher.group(1) != null) { // Quoted word found
        values.add(matcher.group(1));
      } else if (matcher.group(2) != null) { // Coma found
        values.add(matcher.group(2));
      }
    }
    return values.toArray(new String[0]);
  }

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
    List<T> output = new ArrayList<>();
    List<String[]> raw = Parser.regular(path);
    for (int index = 1; index < raw.size(); ++index) {
      output.add(factory.fromRow(raw.get(index)));
    }
    return output;
  }

  // TODO stop_times: (trip_id,departure_time,stop_id,stop_sequence)
  // TODO trips:      (trip_id,route_id)
}
