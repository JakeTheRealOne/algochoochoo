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
    for (int i = 0; i < n; ++i) {
      Edge e = path.get(i);
      System.out.println(e);
      // Trip trip = e.trip();
      // e.print_directive();
      // e.print_to();
      // while (i < (n-1) && e.trip() == trip) {
      //   ++i;
      //   e = path.get(i);
      // }
      // path.get(i-1).print_to();
      // System.out.println();
    }
  }

  // TODO implementé un cli et un gui

  // /**
  //  * Show a result
  //  *
  //  * @param path The path to print
  //  */
  // public static void show(List<Edge> path) {
  //   int n = path.size();
  //   int j;
  //   for (int i = 0; i < n; ++i) {
  //     Trip trip = path.get(i).trip();
  //     for (j = i; j < n && path.get(j).trip() == trip; ++j);
  //     print_line(path.get(i), path.get(j - 1), j - i);
  //     i = j - 1;
  //   }
  // }

  // // #### Private helpers ####

  // /**
  //  * Print a connection
  //  *
  //  * @param conn The connection
  //  * @param from If we will print the first stop of the connection (!to)
  //  */
  // private static void print_connection(Edge conn, boolean from) {
  //   // if ((from) && conn.is_footpath()) {
  //   // System.out.println(conn);
  //   // }
  //   Stop stop = from ? conn.from() : conn.to();
  //   String keyword = from ? " from " : " to ";
  //   String endl = from ? "" : "\n";
  //   int dur = from ? conn.departure_time() : conn.arrival_time();
  //   System.out.print(keyword + stop.name() + time(dur) + endl);
  // }

  // /**
  //  * Convert a duration to HH:MM:SS time
  //  *
  //  * @param dur The duration (in seconds)
  //  * @return The string representing the time
  //  */
  // private static String time(int dur) {
  //   return String.format(
  //       " (%02d:%02d:%02d)", dur / 3600, (dur % 3600) / 60, dur % 60);
  // }

  // private static final String TIP = "●";
  // private static final String MID = "○ ";
  // private static final String TRANS = "━━";
  // private static final String FOOT = "╺";

  // /**
  //  * Print a trip
  //  *
  //  * @param beg The beginning of the trip
  //  * @param end The end of the trip
  //  * @param n The trip length
  //  */
  // private static void print_line(Edge beg, Edge end, int n) {
  //   // boolean foot = beg.is_footpath();
  //   // if (foot) {
  //   //   System.out.print("Walking " + TIP);
  //   //   System.out.print(FOOT + FOOT + FOOT);
  //   //   System.out.println(TIP);
  //   //   print_trip(beg, end);
  //   //   return;
  //   // }

  //   Color color = trip_to_color(beg.trip());
  //   enable_color(color);
  //   System.out.print(beg.trip_name() + " " + TIP + TRANS);
  //   for (int i = 0; i < n - 1; ++i) {
  //     System.out.print(MID + TRANS);
  //   }
  //   System.out.println(TIP);
  //   print_trip(beg, end);
  //   disable_color();
  // }

  // private static void print_trip(Edge beg, Edge end) {
  //   print_connection(beg, true);
  //   print_connection(end, false);
  //   System.out.println();
  // }

  // private static Color trip_to_color(Trip trip) {
  //   int hash = trip.route().long_name().hashCode();
  //   Random random = new Random(hash);
  //   float value = random.nextFloat();
  //   if (value >= 0.25f && value <= 0.45f) {
  //     value = (value + 0.1f) % 1.0f;
  //   }
  //   float saturation = 0.8f;
  //   float brightness = 0.8f;
  //   return Color.getHSBColor(value, saturation, brightness);
  // }

  // private static void enable_color(Color color) {
  //   int red = color.getRed();
  //   int green = color.getGreen();
  //   int blue = color.getBlue();
  //   System.out.print(String.format("\033[38;2;%d;%d;%dm", red, green,
  //   blue));
  // }

  // private static void disable_color() {
  //   System.out.print("\033[0m");
  // }
}
