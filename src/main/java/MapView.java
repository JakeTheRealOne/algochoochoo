import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import org.jxmapviewer.*;
import org.jxmapviewer.viewer.*;
import org.jxmapviewer.input.*;

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

      // TileFactoryInfo info = new OSMTileFactoryInfo();
      TileFactoryInfo info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
      DefaultTileFactory tileFactory = new DefaultTileFactory(info);
      mapViewer.setTileFactory(tileFactory);

      GeoPosition geoPosition = new GeoPosition(50.8503, 4.3517);
      mapViewer.setCenterPosition(geoPosition);
      mapViewer.setZoom(5);

      MouseInputListener mia = new PanMouseInputListener(mapViewer);
      mapViewer.addMouseListener(mia);
      mapViewer.addMouseMotionListener(mia);
      mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

      frame.add(mapViewer, BorderLayout.CENTER);
      frame.setVisible(true);
    });
  }
}
