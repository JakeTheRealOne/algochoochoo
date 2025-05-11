import org.jxmapviewer.viewer.WaypointRenderer;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.JXMapViewer;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.Graphics2D;

public class RegularRenderer implements WaypointRenderer<Waypoint> {

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint waypoint) {
        Point2D point = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());
        int size = 20;
        g.setColor(Color.BLACK);
        g.fill(new Ellipse2D.Double(point.getX() - size / 2, point.getY() - size / 2, size, size));
    }
}