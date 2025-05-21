/*
 * This file is part of Algochoochoo.
 *
 * Licensed under the GNU General Public License v3.
 * See <https://www.gnu.org/licenses/gpl-3.0.html> for details.
 *
 * 2025
 * Bilal Vandenberge
 */

package algochoochoo.parsing;

/**
 * Raw trip datas from GTFS trips.csv file
 *
 * @author Bilal Vandenberge
 */
public class TripData {
  // #### Public methods ####

  /**
   * Construct a new RTrip object
   *
   * @exception IllegalArgumentException If the argument is invalid
   * @param data A CSV file row
   */
  public TripData(String[] data) {
    if (data.length != 2) {
      throw new IllegalArgumentException(
          "Wrong trip entry size (Input: " + data.length + " != Expected: 2)");
    }
    trip_id = data[0];
    route_id = data[1];
  }

  /** Convert a RTrip object to string */
  @Override
  public String toString() {
    return "RTrip(" + trip_id + " refers to route" + route_id + ")";
  }

  // #### Getters ####

  /**
   * Get the id of the related trip
   *
   * @return The unique string representing the trip id
   */
  public String trip_id() { return trip_id; }

  /**
   * Get the id of the related route
   *
   * @return The unique string representing the route id
   */
  public String route_id() { return route_id; }

  // #### Attributes ####

  String trip_id;
  String route_id;
}
