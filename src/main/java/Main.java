import java.util.*;

/**
 * Print the shortest path based on GTFS datas and certain criteria
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
    List<Road> roads = Parser.roads("../src/main/resources/GTFS/DELIJN/routes.csv");
    for (Road road : roads) {
        System.out.println(road);
    }
  }
}
