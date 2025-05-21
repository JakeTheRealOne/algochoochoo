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

import java.util.ArrayList;

/**
 * (Literally) A stop
 *
 * @author Bilal Vandenberge
 */
public class Stop {
  // #### Public methods ####

  /**
   * Construct a new Stop object
   *
   * @exception IllegalArgumentException If the argument is invalid
   * @param data A CSV file row
   */
  public Stop(String[] data) {
    if (data.length != 4) {
      throw new IllegalArgumentException(
          "Wrong stop entry size (Input: " + data.length + " != Expected: 4)");
    }
    id = data[0];
    name = data[1];
    pos = new EarthPos(data[2], data[3]);
  }

  /** Convert a Stop object to string */
  @Override
  public String toString() {
    return "Stop(" + id + ", '" + name + "', " + pos + ")";
  }

  // #### Getters ####

  /**
   * Get the id of the stop
   *
   * @return The id
   */
  public String id() { return id; }

  /**
   * Get the name of the stop
   *
   * @return The name
   */
  public String name() { return name; }

  /**
   * Get the position of the stop
   *
   * @return An earth position
   */
  public EarthPos pos() { return pos; }

  /**
   * Get the latitude of the stop
   *
   * @return The distance north or south of the equator
   */
  public double latitude() { return pos.latitude(); }

  /**
   * Get the longitude of the stop
   *
   * @return The distance east or west of the Prime Meridian
   */
  public double longitude() { return pos.longitude(); }

  // #### Attributes ####

  private String id;
  private String name;
  private EarthPos pos;
}
