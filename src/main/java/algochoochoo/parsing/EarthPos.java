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
 * (Literally) An earth position
 *
 * @author Bilal Vandenberge
 */
public class EarthPos {
  // #### Public methods ####

  /**
   * Construct a new EarthPos object
   *
   * @param lat_str String representing a latitude
   * @param long_str String representing a longitude
   */
  public EarthPos(String lat_str, String long_str) {
    try {
      latitude = Double.parseDouble(lat_str);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid latitude: '" + lat_str + "'");
    }

    try {
      longitude = Double.parseDouble(long_str);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid number format: '" + long_str +
                                         "'");
    }
  }

  /** Convert an EarthPos object to string */
  @Override
  public String toString() {
    return "Pos(" + latitude + ", " + longitude + ")";
  }

  // #### Getters ####

  /**
   * Get the latitude of the position
   *
   * @return The distance north or south of the equator
   */
  public double latitude() { return latitude; }

  /**
   * Get the longitude of the position
   *
   * @return The distance east or west of the Prime Meridian
   */
  public double longitude() { return longitude; }

  // #### Attributes ####

  private double latitude;
  private double longitude;
}
