import java.awt.*;
import javax.swing.*;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 * Show the algorithm results on a map
 *
 * @author Bilal Vandenberge
 */
public class MapView {
  /**
   * Open the application
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Trip planning");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(800, 600);
      JXMapViewer mapViewer = new JXMapViewer();

      TileFactoryInfo info = new OSMTileFactoryInfo();
      DefaultTileFactory tileFactory = new DefaultTileFactory(info);
      mapViewer.setTileFactory(tileFactory);

      GeoPosition geoPosition = new GeoPosition(50.8503, 4.3517);
      mapViewer.setCenterPosition(geoPosition);
      mapViewer.setZoom(5);

      frame.add(mapViewer, BorderLayout.CENTER);
      frame.setVisible(true);
    });
  }
}
