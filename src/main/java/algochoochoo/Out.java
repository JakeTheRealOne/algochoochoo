package algochoochoo;

import algochoochoo.*;
import algochoochoo.graph.*;
import algochoochoo.raw.*;
import java.awt.Color;
import java.util.List;
import java.util.Random;

/**
 * Handle the project view
 *
 * @author Bilal Vandenberge
 */
public class Out {
  // #### Public methods ####

  /**
   * Print a result, concatenate the edges into trips
   *
   * @param path The path to print
   */
  public static void print(List<Edge> path, int h) {
    int n = path.size();
    if (n == 0) {
      System.out.println("Sorry, no path has been found");
      return;
    }

    // TODO fix the time bug
    // TODO un autre bug: on doit pas partir à h, mais à h + W
    int time = h;
    for (int i = 0; i < n; ++i) {
      Edge current = path.get(i);
      Trip trip = current.trip();
      if (current.is_connection()) {
        time = current.departure_time();
      }
      System.out.print(current.directive());
      System.out.print(" from " + current.from().stop().name() + beautiful_time(time));
      time += current.duration();

      int j = i + 1;
      while (j < n && path.get(j).trip() == trip) {
        if (path.get(j).is_connection()) {
          time = path.get(j).departure_time();
        }
        time += path.get(j).duration();
        ++j;
      }
      i = j - 1;
      System.out.println(" to " + path.get(i).to().stop().name() + beautiful_time(time));
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
