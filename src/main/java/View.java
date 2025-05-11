import java.awt.Color;
import java.util.List;
import java.util.Random;

/**
 * Handle the project view
 *
 * @author Bilal Vandenberge
 */
public class View {
  // #### Public methods ####

  /**
   * Print a result, concatenate the edges into trips
   *
   * @param path The path to print
   */
  public static void print(List<Edge> path) {
    int n = path.size();
    if (n == 0) {
      System.out.println("Sorry, no path has been found");
      return;
    }

    // TODO fix the time bug
    // TODO un autre bug: on doit pas partir à h, mais à h + W
    for (int i = 0; i < n; ++i) {
      Edge e = path.get(i);
      Trip trip = e.trip();

      Node from = e.from();
      int departure = from.best_time();

      while (e.trip() == trip) {
        ++i;
        if (i >= n)
          break;
        e = path.get(i);
      }
      --i;

      Node to = path.get(i).to();
      int arrival = to.best_time();

      path.get(i).print_directive();
      System.out.println(" " + from.stop().name() + beautiful_time(departure)
          + " - " + to.stop().name() + beautiful_time(arrival));
    }
  }

  // #### Private helpers ####

  /**
   * Convert a time into beautiful string
   *
   * @param time The time (in seconds)
   */
  private static String beautiful_time(int time) {
    return String.format(
        " (%02d:%02d:%02d)", time / 3600, (time % 3600) / 60, time % 60);
  }
}
