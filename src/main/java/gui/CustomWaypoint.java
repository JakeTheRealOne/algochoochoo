import java.awt.Color;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

// TODO Change this name

public class CustomWaypoint implements Waypoint {
  private GeoPosition position;
  private Color color;
  private boolean intersection;

  public CustomWaypoint(GeoPosition pos, Color c, boolean intersec) {
    position = pos;
    color = c;
    intersection = intersec;
  }

  @Override
  public GeoPosition getPosition() {
    return position;
  }

  public Color color() {
    return color;
  }

  public boolean is_intersection() {
    return intersection;
  }
}
