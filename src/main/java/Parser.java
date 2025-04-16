import java.util.*;
import java.io.*;

/**
 * Parse a CSV file
 * 
 * @author Bilal Vandenberge
 */
public class Parser {
  // #### Public methods ####

  public static List<Road> roads(String path) {
    List<Road> output = new ArrayList<>();
    List<String[]> raw = Parser.regular(path);
    check_roads_integrity(raw);
    for (int index = 1; index < raw.size(); ++index) {
      Road road = new Road(raw.get(index));
      output.add(road);
    }
    return output;
  }

  // #### Private helpers ####

  /**
   * Convert the content of a CSV file into a list of string arrays
   * 
   * @param path Path of the CSV file
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

  private static final String[] ROADS_HEADER = {"route_id","route_short_name","route_long_name","route_type"};

  private static void check_roads_integrity(List<String[]> content) {
    if (content.isEmpty()) {
      throw new IllegalArgumentException("Roads file cannot be empty");
    } else if (!Arrays.equals(content.get(0), ROADS_HEADER)) {
      throw new IllegalArgumentException("Invalid roads header");
    }
  }

  // routes:     (route_id,route_short_name,route_long_name,route_type)
  // stops:      (stop_id,stop_name,stop_lat,stop_lon)
  // stop_times: (trip_id,departure_time,stop_id,stop_sequence)
  // trips:      (trip_id,route_id)
}
