/**
 * (Literally) A road
 * 
 * @author Bilal Vandenberge
 */
public class Road {
  private String id;
  private String short_name;
  private String long_name;
  private RoadType type;

  /**
   * Construct a new Road object
   * 
   * @param data A CSV file entry
   */
  public Road(String[] data) {
    if (data.length != 4) {
      throw new IllegalArgumentException("Wrong road entry size (Input: " + data.length + " != Expected: 4)");
    }
    id = data[0];
    short_name = data[1];
    long_name = data[2];
    type = RoadType.valueOf(data[3]);
  }

  /**
   * Convert a Road object to string
   */
  @Override
  public String toString() {
    return "Road(" + id + ", " + short_name + ", " + long_name + ", " + type + ")";
  }

  private RoadType convert_type(String name) {
    switch(name) {
    case "TRAIN":
      return RoadType.TRAIN;
    case "METRO":
      return RoadType.METRO;
    case "BUS":
      return RoadType.BUS;
    case "TRAM":
      return RoadType.TRAM;
    default:
      throw new IllegalArgumentException("Unknown road type: '" + name + "'");
    }
  }

  // routes:     (route_id,route_short_name,route_long_name,route_type)
}