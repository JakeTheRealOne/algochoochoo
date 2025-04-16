import java.util.*;
import java.io.*;

/**
 * Parse a CSV file
 * 
 * @author Bilal Vandenberge
 */
public class CSV {
  /**
   * Main entry point to the project
   *
   * @param args Command line arguments
   */
  public static List<String[]> parse(String path) {
    List<String[]> output = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        output.add(values);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return output;
  }

  // routes:     (route_id,route_short_name,route_long_name,route_type)
  // stops:      (stop_id,stop_name,stop_lat,stop_lon)
  // stop_times: (trip_id,departure_time,stop_id,stop_sequence)
  // trips:      (trip_id,route_id)
}
