import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.*;

/**
 * Parse a CSV file
 *
 * @author Bilal Vandenberge
 */
public class Parser {
  // #### Public methods ####

  /** Do not allow Parser instantiation */
  private Parser() {}

  public static <T> Iterable<T> iterate(String path, CsvFactory<T> factory) {
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

  // /**
  //  * Convert the content of a stops file into a list of Stop objects
  //  *
  //  * @param path Path of the CSV file
  //  * @param list List receiving the output objects
  //  */
  // public static void stops(String path, List<Stop> list) {
  //   generic(path, Stop::new, list);
  // }

  // /**
  //  * Convert the content of a routes file into a list of Route objects
  //  *
  //  * @param path Path of the CSV file
  //  * @param list List receiving the output objects
  //  */
  // public static void routes(String path, List<Route> list) {
  //   generic(path, Route::new, list);
  // }

  // // TODO add list and change return in method docstrings

  // /**
  //  * Convert the content of a trips file into a list of Trip objects
  //  *
  //  * @param path Path of the CSV file
  //  * @param list List receiving the output objects
  //  */
  // public static void trips(String path, List<Trip> list) {
  //   generic(path, Trip::new, list);
  // }

  // /**
  //  * Convert the content of a stop_times file into a list of StopTime objects
  //  *
  //  * @param path Path of the CSV file
  //  * @param list List receiving the output objects
  //  */
  // public static void stop_times(String path, List<StopTime> list) {
  //   generic(path, StopTime::new, list);
  // }

  // #### Private helpers ####

  // /** Expected header for routes files */
  // private static final String[] ROADS_HEADER = {
  //   "route_id", "route_short_name", "route_long_name", "route_type"
  // };

  // /**
  //  * Check if the content of a routes file is valid
  //  *
  //  * @exception IllegalArgumentException If the content is invalid
  //  */
  // private static void check_routes_integrity(List<String[]> content) {
  //   if (content.isEmpty()) {
  //     throw new IllegalArgumentException("Routes file cannot be empty");
  //   } else if (!Arrays.equals(content.get(0), ROADS_HEADER)) {
  //     throw new IllegalArgumentException("Invalid routes header");
  //   }
  // }

  // TODO add integrity check

  // private static <T> void generic(String path, CsvFactory<T> factory, List<T> into) {
  //   CsvParserSettings settings = new CsvParserSettings();
  //   settings.setHeaderExtractionEnabled(true); // skip header
  //   CsvParser parser = new CsvParser(settings);

  //   try (Reader reader = Files.newBufferedReader(Path.of(path))) {
  //     for (String[] row : parser.iterate(reader)) {
  //       into.add(factory.fromRow(row));
  //     }
  //   } catch (IOException e) {
  //     throw new IllegalArgumentException("An error occured while reading: '" + path + "'");
  //   }
  // }
}
