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
 * Paints a route
 * @author Martin Steiger
 */
public class RoutePainter implements Painter<JXMapViewer> {
  private Color color = Color.RED;
  private boolean antiAlias = true;

  private List<GeoPosition> track;
  private List<Color> colors;
  private List<Boolean> walks;

  public RoutePainter() {
    this.track = new ArrayList<>();
  }

  /**
   * @param track the track
   */
  public RoutePainter(List<GeoPosition> t, List<Color> c, List<Boolean> w) {
    // copy the list so that changes in the
    // original list do not have an effect here
    track = t;
    colors = c;
    walks = w;
  }

  @Override
  public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
    g = (Graphics2D) g.create();

    // convert from viewport to world bitmap
    Rectangle rect = map.getViewportBounds();
    g.translate(-rect.x, -rect.y);

    if (antiAlias)
      g.setRenderingHint(
          RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    drawRoute(g, map);

    g.dispose();
  }

  /**
   * @param g the graphics object
   * @param map the map
   */
  private void drawRoute(Graphics2D g, JXMapViewer map) {
    final float thickness = 12f;
    final float[] dash_pattern = {5, 5};
    final BasicStroke connection_stroke = new BasicStroke(thickness);
    final BasicStroke transfer_stroke = new BasicStroke(thickness,
        BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dash_pattern, 0);

    int lastX = 0;
    int lastY = 0;

    boolean first = true;

    for (int i = 0; i < track.size(); ++i) {
      GeoPosition gp = track.get(i);
      Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());

      if (first) {
        first = false;
      } else {
        Color color = colors.get(i - 1);
        Boolean walk = walks.get(i - 1);
        g.setStroke(walk ? transfer_stroke : connection_stroke);
        g.setColor(color);
        g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
      }

      lastX = (int) pt.getX();
      lastY = (int) pt.getY();
    }
  }
}
