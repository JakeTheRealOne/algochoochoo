import java.io.*;
import java.util.*;

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
   * Convert the content of a Routes file into a list of Route objects
   *
   * @param path Path of the CSV file
   * @return List of routes
   */
  public static List<Route> routes(String path) {
    List<Route> output = new ArrayList<>();
    List<String[]> raw = Parser.regular(path);
    check_routes_integrity(raw);
    for (int index = 1; index < raw.size(); ++index) {
      Route route = new Route(raw.get(index));
      output.add(route);
    }
    return output;
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
        String[] values = line.split(",");
        output.add(values);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return output;
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

  // stops:      (stop_id,stop_name,stop_lat,stop_lon)
  // stop_times: (trip_id,departure_time,stop_id,stop_sequence)
  // trips:      (trip_id,route_id)
}
