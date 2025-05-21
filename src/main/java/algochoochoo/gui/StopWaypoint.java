/*
 * This file is part of Algochoochoo.
 *
 * Licensed under the GNU General Public License v3.
 * See <https://www.gnu.org/licenses/gpl-3.0.html> for details.
 *
 * 2025
 * Bilal Vandenberge
 */

package algochoochoo.gui;

import java.awt.Color;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

/**
 * A waypoint on the GUI of a stop
 *
 * @author Bilal Vandenberge
 */
public class StopWaypoint implements Waypoint {
  // #### Public methods ####

  /**
   * Construct a new StopWaypoint object
   *
   * @param pos The position of the waypoint
   * @param c The color of the waypoint
   * @param intersec If the waypoint is an intersection of two trips
   */
  public StopWaypoint(GeoPosition pos, Color c, boolean intersec) {
    position = pos;
    color = c;
    intersection = intersec;
  }

  // #### Getters ####

  @Override
  /**
   * Get the position of the waypoint
   *
   * @return The waypoint position
   */
  public GeoPosition getPosition() {
    return position;
  }

  /**
   * Get the color of the waypoint
   *
   * @return The waypoint color
   */
  public Color color() { return color; }

  /**
   * Get if the waypoint is an intersection
   *
   * @return The intersection flag
   */
  public boolean is_intersection() { return intersection; }

  // #### Attributes ####

  private GeoPosition position;
  private Color color;
  private boolean intersection;
}
