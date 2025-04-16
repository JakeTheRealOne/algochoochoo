import java.util.*;

/**
 * Print the shortest path based on assets/GTFS data and certain criteria
 * 
 * @author Bilal Vandenberge
 */
public class Main {
  /**
   * Main entry point to the project
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    List<String[]> stops = CSV.parse("../assets/GTFS/DELIJN/stops.csv");
    for (String[] stop : stops) {
        for (String info : stop) {
            System.out.print(info + " ");
        }
        System.out.println();
    }
  }
}

// TODO: Decide if we give assets/GTFS or not