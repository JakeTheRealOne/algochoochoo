import java.awt.*;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointRenderer;

public class CustomWaypointRenderer
    implements WaypointRenderer<CustomWaypoint> {
  @Override
  public void paintWaypoint(
      Graphics2D g, JXMapViewer map, CustomWaypoint waypoint) {
    Point2D point =
        map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());
    final int size = 20;
    final float thickness = 6f;

    g.setColor(waypoint.is_intersection() ? Color.WHITE : waypoint.color());
    g.fill(new Ellipse2D.Double(
        point.getX() - size / 2, point.getY() - size / 2, size, size));

    if (waypoint.is_intersection()) {
      g.setStroke(new BasicStroke(thickness));
      g.setColor(Color.BLACK);
      g.draw(new Ellipse2D.Double(
          point.getX() - size / 2, point.getY() - size / 2, size, size));
    }
  }
}