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
    time_test_skibidi();
  }

  // #### Private helpers ####

  private static void test_a() {
    // TODO remove
    // Remplace ce chemin par le dossier que tu veux parcourir
    Path root = Paths.get("src/main/resources/GTFS/");

    long start = System.currentTimeMillis();
    try {
      Files.walk(root)
          .filter(Files::isRegularFile)
          .parallel() // lit les fichiers en parallèle
          .forEach(Main::readFileFast);
    } catch (IOException e) {
      System.out.println("AAAAAAAAAAAAaaa");
    }
    long end = System.currentTimeMillis();

    System.out.println("Lecture terminée en " + (end - start) + " ms");
  }

  private static void readFileFast(Path filePath) {
    System.out.println(filePath);
    try (BufferedInputStream bis =
        new BufferedInputStream(new FileInputStream(filePath.toFile()), 8192)) {
      byte[] buffer = new byte[8192];
      while (bis.read(buffer) != -1) {
        // On ne fait rien avec les données pour maximiser la vitesse
      }
    } catch (IOException e) {
      System.err.println("Erreur lors de la lecture de : " + filePath + " -> " + e.getMessage());
    }
  }

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

  private static void time_test_skibidi() {
    long start = System.nanoTime();
    test_opencsv();
    double duration = (double) (System.nanoTime() - start) / 1000000000;
    System.out.println("OpenCSV in " + duration + " seconds");

    start = System.nanoTime();
    Parser.stop_times("/home/bvan/Developer/algotrain_preambule/algotrain/src/main/resources/GTFS/DELIJN/stop_times.csv");
    duration = (double) (System.nanoTime() - start) / 1000000000;
    System.out.println("univocity in " + duration + " seconds");
  }

  private static void test_univocity() {
    String dir = "src/main/resources/GTFS/";
    String path = "/home/bvan/Developer/algotrain_preambule/algotrain/src/main/resources/GTFS/DELIJN/stop_times.csv";
    
    List<StopTime> result = new ArrayList<>();

    CsvParserSettings settings = new CsvParserSettings();
    settings.setHeaderExtractionEnabled(true); // skip header
    CsvParser parser = new CsvParser(settings);

    try (Reader reader = Files.newBufferedReader(Path.of(path))) {
        for (String[] row : parser.iterate(reader)) {
            result.add(new StopTime(row));
        }
    } catch (IOException e) {
      throw new IllegalArgumentException("An error occured while reading: '" + path + "'");
    }    
  }

  private static void test_opencsv() {
    String dir = "src/main/resources/GTFS/";
    String path = "/home/bvan/Developer/algotrain_preambule/algotrain/src/main/resources/GTFS/DELIJN/stop_times.csv";
    
    List<StopTime> result = new ArrayList<>();

    try (CSVReader reader = new CSVReader(new FileReader(path))) {
      String[] row;
      boolean first = true;
      while ((row = reader.readNext()) != null) {
          if (first) { first = false; continue; } // Skip header
            result.add(new StopTime(row));
      }
    } catch (CsvException e) {
      throw new IllegalArgumentException("An error occured while parsing: '" + path + "'");
    } catch (IOException e) {
      throw new IllegalArgumentException("An error occured while reading: '" + path + "'");
    }    
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
        output.addAll(Parser.routes(csv_file));
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
        output.addAll(Parser.trips(csv_file));
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
        output.addAll(Parser.stops(csv_file));
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
        output.addAll(Parser.stop_times(csv_file));
      }
    }
    return output;
  }
}

// TODO change the docstring of the Main class
