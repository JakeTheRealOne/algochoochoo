import java.util.*;

/**
 * Print the shortest path based on GTFS datas and certain criteria
 *
 * @author Bilal Vandenberge
 */
public class Main {
  // #### Public methods ####

  /** Construct a new Main object */
  public Main() {}

  /**
   * Main entry point to the project
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    // TODO Testing parser:
    List<Trip> routes = Parser.trips("../src/main/resources/GTFS/DELIJN/trips.csv");
    for (Trip route : routes) {
      System.out.println(route);
    }
  }
}

// TODO change the docstring of the Main class
