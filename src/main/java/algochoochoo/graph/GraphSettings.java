package algochoochoo.graph;

/**
 * Store graph settings
 */
public class GraphSettings {
  // #### Public methods ####

  /**
   * Construct a default GraphSettings object
   */
  public GraphSettings() {}

  /**
   * Construct a GraphSettings object
   *
   * @param gtfs_path The GTFS directory path
   */
  public GraphSettings(String gtfs_path) {
    GTFS_path = gtfs_path;
  }

  /**
   * Construct a GraphSettings object from exec arguments
   *
   * @param args The arguments of the executable
   */
  public GraphSettings(String[] args) {
    int n = args.length;
    for (int i = 3; i < n; ++i) {
      String arg = args[i];
      if (arg.startsWith(FOOT_RADIUS_ARG)) {
        parseFootRadius(arg);
      } else if (arg.startsWith(PATH_ARG)) {
        parsePath(arg);
      }
    }
  }

  /**
   * Print the settings on STDOUT
   */
  public void print() {
    System.out.println("GTFS directory path: " + GTFS_path);
    System.out.println("Footpath search radius: " + foot_radius + " meters");
  }

  // #### Constants ####

  final private String FOOT_RADIUS_ARG = "--foot-radius=";
  final private String PATH_ARG = "--gtfs-path=";

  // #### Attributes ####

  /** The path of the GTFS directory */
  public String GTFS_path = "src/main/resources/GTFS";

  /** The proximity criteria for Transfers evaluation */
  public int foot_radius = 500;

  // #### Private helpers ####

  /**
   * Update the footpath radius from exec args
   */
  private void parseFootRadius(String arg) {
    int i = FOOT_RADIUS_ARG.length();
    double radius = 0;
    try {
      radius = Double.parseDouble(arg.substring(i));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Invalid distance: " + arg.substring(i));
    }
    int rounded = (int) Math.round(radius);
    foot_radius = rounded;
  }

  /**
   * Reevaluate the GTFS path from arg
   *
   * @param arg One executable argument
   */
  private void parsePath(String arg) {
    int i = PATH_ARG.length();
    GTFS_path = arg.substring(i);
  }
}
