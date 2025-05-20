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

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

/**
 * The waypoint renderer for StopWaypoint
 *
 * @author Bilal Vandenberge
 */
public class StopWaypointRenderer implements WaypointRenderer<StopWaypoint> {
  // #### Public methods ####

  /** Construct a default StopWaypointRenderer */
  public StopWaypointRenderer() {}

  @Override
  /**
   * Draw the waypoint on the map
   *
   * @param g        the graphics object
   * @param map      The map widget
   * @param waypoint The waypoint to draw
   */
  public void paintWaypoint(Graphics2D g, JXMapViewer map,
                            StopWaypoint waypoint) {
    Point2D point =
        map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());

    int size = Math.max(
        13, Math.min(waypoint.is_intersection() ? 1000000 : 20,
                     (int)(150f / (map.getZoom() * Math.log(map.getZoom())))));
    float thickness = Math.max(
        4f, Math.min(waypoint.is_intersection() ? 1000000 : 6f,
                     (float)(40f / (map.getZoom() * Math.log(map.getZoom())))));

    g.setColor(waypoint.is_intersection() ? Color.WHITE : waypoint.color());
    g.fill(new Ellipse2D.Double(point.getX() - size / 2,
                                point.getY() - size / 2, size, size));

    if (waypoint.is_intersection()) {
      g.setStroke(new BasicStroke(thickness));
      g.setColor(Color.BLACK);
      g.draw(new Ellipse2D.Double(point.getX() - size / 2,
                                  point.getY() - size / 2, size, size));
    }
  }
}