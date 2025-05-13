package algochoochoo.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * GUI Painter for routes
 * 
 * @author Bilal Vandenberge
 */
public class PathPainter implements Painter<JXMapViewer> {
  // #### Public methods ####

  /**
   * Construct a default PathPainter object
   */
  public PathPainter() {
    path = new ArrayList<>();
    colors = new ArrayList<>();
    walks = new ArrayList<>();
  }

  /**
   * Construct a RoutePainter object
   * 
   * @param t The track of the route
   * @param c The colors of each edge of the path
   * @param w The 'is_transfer' flag for each edge of the path
   */
  public PathPainter(List<GeoPosition> t, List<Color> c, List<Boolean> w) {
    path = t;
    colors = c;
    walks = w;
  }

  @Override
  /**
   * Paint the path
   * 
   * @param g   the graphics object
   * @param map The map widget
   * @param w   The width
   * @param h   The height
   */
  public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
    g = (Graphics2D) g.create();
    Rectangle rect = map.getViewportBounds();
    g.translate(-rect.x, -rect.y);
    if (anti_alias)
      g.setRenderingHint(
          RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    drawLines(g, map);
    g.dispose();
  }

  // #### Attributes ####

  private boolean anti_alias = true;
  private List<GeoPosition> path;
  private List<Color> colors;
  private List<Boolean> walks;

  // #### Private helpers ####

  /**
   * Draw the lines of the route
   * 
   * @param g   the graphics object
   * @param map the map
   */
  private void drawLines(Graphics2D g, JXMapViewer map) {
    final float thickness = 12f;
    final float[] dash_pattern = { 5, 5 };
    final BasicStroke connection_stroke = new BasicStroke(thickness);
    final BasicStroke transfer_stroke = new BasicStroke(thickness,
        BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dash_pattern, 0);

    if (path.isEmpty())
      return;

    Point2D before = map.getTileFactory().geoToPixel(path.get(0), map.getZoom());
    for (int i = 1; i < path.size(); ++i) {
      GeoPosition gp = path.get(i);
      Point2D current = map.getTileFactory().geoToPixel(gp, map.getZoom());
      Color color = colors.get(i - 1);
      Boolean walk = walks.get(i - 1);
      g.setStroke(walk ? transfer_stroke : connection_stroke);
      g.setColor(color);
      g.drawLine((int) before.getX(), (int) before.getY(), (int) current.getX(), (int) current.getY());
      before = current;
    }
  }
}
