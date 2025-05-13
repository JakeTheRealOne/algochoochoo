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
  public Color color() {
    return color;
  }

  /**
   * Get if the waypoint is an intersection
   * 
   * @return The intersection flag
   */
  public boolean is_intersection() {
    return intersection;
  }

  // #### Attributes ####

  private GeoPosition position;
  private Color color;
  private boolean intersection;
}
