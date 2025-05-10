import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.border.*;
import javax.swing.event.*;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.viewer.WaypointRenderer;

public class MapView {
  public static void main(String[] args) {
    MapView view = new MapView();
    view.set_up();
  }

  private void set_up() {
    sources = new HashSet<Waypoint>();
    targets = new HashSet<Waypoint>();

    source_painter = new WaypointPainter<>();
    source_painter.setRenderer(new TerminusRenderer());
    target_painter = new WaypointPainter<>();
    source_painter.setRenderer(new TerminusRenderer());

    AlgoSettings set = new AlgoSettings();
    // Graph graph = new Graph("src/main/resources/GTFS", set);
    map_viewer = new JXMapViewer();
    JFrame frame = new JFrame("Belgium trip planner");

    // Create a TileFactoryInfo for OpenStreetMap
    TileFactoryInfo info =
        new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
    DefaultTileFactory tileFactory = new DefaultTileFactory(info);
    map_viewer.setTileFactory(tileFactory);

    GeoPosition bruxelles = new GeoPosition(50.8503, 4.3517);
    map_viewer.setAddressLocation(bruxelles);
    map_viewer.setZoom(1);

    MouseInputListener mia = new PanMouseInputListener(map_viewer);
    map_viewer.addMouseListener(mia);
    map_viewer.addMouseMotionListener(mia);
    map_viewer.addMouseWheelListener(
        new ZoomMouseWheelListenerCursor(map_viewer));

    // Create a side panel with some components
    Dimension size = new Dimension(Integer.MAX_VALUE, 30);
    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
    JLabel label1 = new JLabel("Source stop");
    label1.setMaximumSize(size);
    label1.setBorder(new EmptyBorder(5, 5, 5, 5));
    field1 = new JTextField("", 15);
    field1.setMaximumSize(size);
    field1.setBorder(new EmptyBorder(5, 5, 5, 5));
    JButton button1 = new JButton("Find");
    button1.setMaximumSize(size);
    button1.setBorder(new EmptyBorder(5, 5, 5, 5));

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
    panel2.setBorder(new EmptyBorder(5, 5, 5, 5));
    JLabel label2 = new JLabel("Target stop");
    label2.setMaximumSize(size);
    label2.setBorder(new EmptyBorder(5, 5, 5, 5));
    field2 = new JTextField("", 15);
    field2.setMaximumSize(size);
    field2.setBorder(new EmptyBorder(5, 5, 5, 5));
    JButton button2 = new JButton("Find");
    button2.setMaximumSize(size);
    button2.setBorder(new EmptyBorder(5, 5, 5, 5));

    panel1.add(label1);
    panel1.add(field1);
    panel1.add(button1);

    panel2.add(label2);
    panel2.add(field2);
    panel2.add(button2);

    JPanel sidePanel = new JPanel();
    sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
    sidePanel.add(panel1);
    sidePanel.add(panel2);
    JButton start_button = new JButton("Start search");
    sidePanel.add(start_button);

    JPanel waiting_panel = new JPanel();
    waiting_panel.setLayout(new BoxLayout(waiting_panel, BoxLayout.Y_AXIS));
    waiting_panel.add(Box.createVerticalGlue());
    waiting_panel.add(new JLabel("Constructing the graph", JLabel.CENTER));
    waiting_panel.setBorder(new EmptyBorder(20, 20, 20, 20));
    JProgressBar progress_bar = new JProgressBar();
    progress_bar.setIndeterminate(true);
    waiting_panel.add(progress_bar);
    waiting_panel.add(Box.createVerticalGlue());

    JSplitPane splitPane =
        new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, map_viewer);
    splitPane.setDividerLocation(
        200);

    frame.getContentPane().add(waiting_panel);
    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    button1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        click_find(true);
      }
    });

    button2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        click_find(false);
      }
    });

    start_button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        execute_algorithm();
      }
    });

    SwingWorker<Void, Void> worker = new SwingWorker<>() {
      @Override
      protected Void doInBackground() throws Exception {
        AlgoSettings set = new AlgoSettings();
        graph = new Graph("src/main/resources/GTFS", set);
        return null;
      }

      @Override
      protected void done() {
        frame.remove(waiting_panel);
        frame.add(splitPane);
        map_viewer.revalidate();
        map_viewer.repaint();
        frame.revalidate();
        frame.repaint();
      }
    };

    worker.execute();
  }

  private void execute_algorithm() {
    System.err.println("exec");
  }

  private void click_find(boolean source) {
    String input = (source ? field1 : field2).getText();
    List<DefaultWaypoint> waypoints = new ArrayList<>();
    for (Node v : graph.vertices()) {
      Stop stop = v.stop();
      if (stop.name().equals(input)) {
        GeoPosition pos = new GeoPosition(stop.latitude(), stop.longitude());
        waypoints.add(new DefaultWaypoint(pos));
      }
    }

    Set<Waypoint> tmp;
    if (source) {
      s = input;
      sources = new HashSet<Waypoint>(waypoints);
      tmp = sources;
    } else {
      t = input;
      targets = new HashSet<Waypoint>(waypoints);
      tmp = targets;
    }

    List<GeoPosition> positions = new ArrayList<>();
    for (Waypoint waypoint : sources) {
      positions.add(waypoint.getPosition());
    } 
    for (Waypoint waypoint : targets) {
      positions.add(waypoint.getPosition());
    } 
    zoom_on(map_viewer, new HashSet<GeoPosition>(positions), 0.7);

    (source ? source_painter : target_painter).setWaypoints(tmp);
    // painter.setRenderer(new WaypointRenderer<>() {
    //   @Override
    //   public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint wp) {
    //     // Conversion des coordonnées géographiques en pixels
    //     Point2D point =
    //         map.getTileFactory().geoToPixel(wp.getPosition(), map.getZoom());

    //     // Récupération de l'offset à l'intérieur du JXMapViewer
    //     Rectangle viewportBounds = map.getViewportBounds();
    //     int x = (int) (point.getX() - viewportBounds.getX());
    //     int y = (int) (point.getY() - viewportBounds.getY());

    //     // Dessin du cercle à la bonne position
    //     g.setColor(Color.RED);
    //     g.fillOval(x - 5, y - 5, 10, 10);
    //   }
    // });

    CompoundPainter<JXMapViewer> all_painters = new CompoundPainter<>();
    all_painters.setPainters(List.of(source_painter, target_painter));
    map_viewer.setOverlayPainter(all_painters);
  }

  public void zoom_on(
      JXMapViewer mapViewer, Set<GeoPosition> positions, double maxFraction) {
    if (positions == null || positions.isEmpty()) {
      System.out.println("Aucune position trouvée.");
      return;
    }

    // Calcul des limites géographiques (bounding box)
    double minLat = Double.MAX_VALUE;
    double maxLat = Double.MIN_VALUE;
    double minLon = Double.MAX_VALUE;
    double maxLon = Double.MIN_VALUE;

    for (GeoPosition pos : positions) {
      minLat = Math.min(minLat, pos.getLatitude());
      maxLat = Math.max(maxLat, pos.getLatitude());
      minLon = Math.min(minLon, pos.getLongitude());
      maxLon = Math.max(maxLon, pos.getLongitude());
    }

    // Calcul du centre de la bounding box
    double centerLat = (minLat + maxLat) / 2;
    double centerLon = (minLon + maxLon) / 2;
    GeoPosition center = new GeoPosition(centerLat, centerLon);

    // Centrer la carte sur le point calculé
    mapViewer.setCenterPosition(center);

    // Calcul de la taille en degrés
    double latRange = maxLat - minLat;
    double lonRange = maxLon - minLon;

    // Ajustement du zoom en fonction de la taille du viewport
    int width = mapViewer.getWidth();
    int height = mapViewer.getHeight();

    mapViewer.calculateZoomFrom(positions);
  }

  // #### Attributes ####
  private Graph graph;

  private String s;
  private String t;
  private int h = 8*3600; // TODO remove this test value

  private Set<Waypoint> sources;
  private Set<Waypoint> targets;

  private JTextField field1;
  private JTextField field2;
  private JXMapViewer map_viewer;

  private WaypointPainter<Waypoint> source_painter;
  private WaypointPainter<Waypoint> target_painter;
}
