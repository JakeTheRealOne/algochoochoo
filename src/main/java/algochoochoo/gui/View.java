package algochoochoo.gui;

import algochoochoo.graph.*;
import algochoochoo.parsing.Stop;
import algochoochoo.parsing.Trip;
import algochoochoo.query.AlgoSettings;
import algochoochoo.query.Algorithm;
import algochoochoo.query.AlgoResult;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 * Run the application
 *
 * @author Bilal Vandenberge
 */
public class View {
  /**
   * Set up and run the graphical interface
   *
   * @param args The exec arguments
   */
  public static void main(String[] args) {
    View view = new View();
    view.set_up();
    view.run();
  }

  /**
   * Construct a default View object
   */
  public View() {}

  /**
   * Set up the GUI
   */
  public void set_up() {
    init_var();

    main_frame = new JFrame("Trip finder");
    map_viewer = new JXMapViewer();
    TileFactoryInfo info =
        new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
    DefaultTileFactory tileFactory = new DefaultTileFactory(info);
    map_viewer.setTileFactory(tileFactory);
    GeoPosition bruxelles = new GeoPosition(50.8503, 4.3517);
    map_viewer.setAddressLocation(bruxelles);
    map_viewer.setZoom(10);

    MouseInputListener mia = new PanMouseInputListener(map_viewer);
    map_viewer.addMouseListener(mia);
    map_viewer.addMouseMotionListener(mia);
    map_viewer.addMouseWheelListener(
        new ZoomMouseWheelListenerCursor(map_viewer));

    Dimension size = new Dimension(Integer.MAX_VALUE, 30);
    JPanel panel1 = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JLabel label1 = new JLabel("Source stop");
    label1.setMaximumSize(size);
    label1.setBorder(new EmptyBorder(5, 5, 5, 5));
    field1 = new JTextField("", 15);
    field1.setMaximumSize(size);
    field1.setBorder(new EmptyBorder(5, 5, 5, 5));
    JButton button1 = new JButton("Find");
    button1.setMaximumSize(size);
    button1.setBorder(new EmptyBorder(5, 5, 5, 5));

    gbc.gridx = 0;
    gbc.weightx = 0;
    panel1.add(label1, gbc);

    gbc.gridx = 1;
    gbc.weightx = 1.0;
    panel1.add(field1, gbc);

    gbc.gridx = 2;
    gbc.weightx = 0;
    panel1.add(button1, gbc);

    JPanel panel2 = new JPanel(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JLabel label2 = new JLabel("Target stop");
    label2.setMaximumSize(size);
    label2.setBorder(new EmptyBorder(5, 5, 5, 5));
    field2 = new JTextField("", 15);
    field2.setMaximumSize(size);
    field2.setBorder(new EmptyBorder(5, 5, 5, 5));
    JButton button2 = new JButton("Find");
    button2.setMaximumSize(size);
    button2.setBorder(new EmptyBorder(5, 5, 5, 5));

    gbc.gridx = 0;
    gbc.weightx = 0;
    panel2.add(label2, gbc);

    gbc.gridx = 1;
    gbc.weightx = 1.0;
    panel2.add(field2, gbc);

    gbc.gridx = 2;
    gbc.weightx = 0;
    panel2.add(button2, gbc);

    JPanel panel3 = new JPanel(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JLabel label3 = new JLabel("Departure time");
    label3.setMaximumSize(size);
    label3.setBorder(new EmptyBorder(5, 5, 5, 5));
    SpinnerNumberModel hour_model = new SpinnerNumberModel(8, 0, 23, 1);
    hour_spinner = new JSpinner(hour_model);
    hour_spinner.setMaximumSize(size);
    hour_spinner.setBorder(new EmptyBorder(5, 5, 5, 5));
    SpinnerNumberModel min_model = new SpinnerNumberModel(30, 0, 59, 1);
    min_spinner = new JSpinner(min_model);
    min_spinner.setMaximumSize(size);
    min_spinner.setBorder(new EmptyBorder(5, 5, 5, 5));
    SpinnerNumberModel sec_model = new SpinnerNumberModel(0, 0, 59, 1);
    sec_spinner = new JSpinner(sec_model);
    sec_spinner.setMaximumSize(size);
    sec_spinner.setBorder(new EmptyBorder(5, 5, 5, 5));

    gbc.gridx = 0;
    gbc.weightx = 1.0;
    panel3.add(label3, gbc);

    gbc.gridx = 1;
    gbc.weightx = 0;
    panel3.add(hour_spinner, gbc);

    gbc.gridx = 2;
    gbc.weightx = 0;
    panel3.add(min_spinner, gbc);

    gbc.gridx = 3;
    gbc.weightx = 0;
    panel3.add(sec_spinner, gbc);

    JPanel gset_panel = new JPanel(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JToggleButton toggleButton = new JToggleButton("Show graph settings ▲");
    JPanel gset2_panel = new JPanel(new GridBagLayout());

    JPanel radius_panel = new JPanel(new GridBagLayout());
    SpinnerNumberModel rad_model = new SpinnerNumberModel(500, 1, 100000, 1);
    radius_spinner = new JSpinner(rad_model);
    radius_spinner.setMaximumSize(size);
    radius_spinner.setBorder(new EmptyBorder(5, 5, 5, 5));

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    radius_panel.add(new JLabel("Max footpath dist (meters)"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    radius_panel.add(radius_spinner, gbc);

    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    radius_panel.add(new JButton("Confirm"), gbc);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gset2_panel.add(radius_panel, gbc);

    JPanel dir_panel = new JPanel(new GridBagLayout());
    dir_field = new JTextField("src/main/resources/GTFS", 15);
    dir_field.setMaximumSize(size);
    dir_field.setBorder(new EmptyBorder(5, 5, 5, 5));

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    dir_panel.add(new JLabel("GTFS path"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    dir_panel.add(dir_field, gbc);

    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    dir_panel.add(new JButton("Confirm"), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gset2_panel.add(dir_panel, gbc);
    gset2_panel.setVisible(false);
    toggleButton.addActionListener(e -> {
      boolean expanded = toggleButton.isSelected();
      gset2_panel.setVisible(expanded);
      toggleButton.setText(
          expanded ? "Hide graph settings ▼" : "Show graph settings ▲");
      gset_panel.getParent().revalidate();
    });
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gset_panel.add(toggleButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gset_panel.add(gset2_panel, gbc);

    panel4 = new JPanel();
    JScrollPane scrollPanel4 =
        new JScrollPane(panel4, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPanel4.setPreferredSize(new Dimension(400, 200));
    panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));
    panel4.setBorder(new EmptyBorder(5, 5, 5, 5));

    JButton start_button = new JButton("Start search");
    start_button.setBorder(new EmptyBorder(10, 10, 10, 10));
    JPanel side_panel = new JPanel(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    side_panel.add(panel1, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    side_panel.add(panel2, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    side_panel.add(panel3, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    side_panel.add(start_button, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    side_panel.add(gset_panel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    side_panel.add(scrollPanel4, gbc);

    waiting_panel = new JPanel();
    waiting_panel.setLayout(new BoxLayout(waiting_panel, BoxLayout.Y_AXIS));
    waiting_panel.add(Box.createVerticalGlue());
    waiting_panel.add(new JLabel("Constructing the graph", JLabel.CENTER));
    waiting_panel.setBorder(new EmptyBorder(20, 20, 20, 20));
    JProgressBar progress_bar = new JProgressBar();
    progress_bar.setIndeterminate(true);
    waiting_panel.add(progress_bar);
    waiting_panel.add(Box.createVerticalGlue());

    split_pane =
        new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, side_panel, map_viewer);
    split_pane.setDividerLocation(400);

    main_frame.getContentPane().add(waiting_panel);
    main_frame.setSize(800, 600);
    main_frame.setLocationRelativeTo(null);
    main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
  }

  /**
   * Run the GUI
   */
  public void run() {
    main_frame.setVisible(true);
    SwingWorker<Void, Void> worker = new SwingWorker<>() {
      @Override
      protected Void doInBackground() throws Exception {
        AlgoSettings algoset = new AlgoSettings();
        GraphSettings graphset = new GraphSettings();
        graph = new Graph(graphset);
        algo = new Algorithm(graph, algoset);
        return null;
      }

      @Override
      protected void done() {
        main_frame.remove(waiting_panel);
        main_frame.add(split_pane);
        map_viewer.revalidate();
        map_viewer.repaint();
        main_frame.revalidate();
        main_frame.repaint();
      }
    };

    worker.execute();
  }

  /**
   * Initialize gui variables
   */
  private void init_var() {
    sources = new HashSet<StopWaypoint>();
    targets = new HashSet<StopWaypoint>();
    results = new HashSet<StopWaypoint>();
    intersections = new HashSet<StopWaypoint>();

    source_painter = new WaypointPainter<>();
    source_painter.setRenderer(new StopWaypointRenderer());
    target_painter = new WaypointPainter<>();
    target_painter.setRenderer(new StopWaypointRenderer());
    result_painter = new WaypointPainter<>();
    result_painter.setRenderer(new StopWaypointRenderer());
    intersection_painter = new WaypointPainter<>();
    intersection_painter.setRenderer(new StopWaypointRenderer());
    route_painter = new PathPainter();
  }

  /**
   * Action of the button "Start search"
   */
  private void execute_algorithm() {
    if (s == null || t == null)
      return;

    int hour = (int) hour_spinner.getValue();
    int min = (int) min_spinner.getValue();
    int sec = (int) sec_spinner.getValue();
    h = 3600 * hour + 60 * min + sec;

    AlgoResult result = algo.dijkstra(s, t, h);

    s = "";
    t = "";
    field1.setText(s);
    field2.setText(t);

    List<Edge> path = result.path;
    List<GeoPosition> track = new ArrayList<>(path.size());
    List<Color> colors = new ArrayList<>(path.size());
    List<Boolean> walks = new ArrayList<>(path.size());
    List<StopWaypoint> waypoints = new ArrayList<>();
    List<StopWaypoint> inter = new ArrayList<>();

    for (int i = 0; i < path.size(); ++i) {
      Edge current = path.get(i);
      if (i == 0) {
        Stop begin = current.from().stop();
        GeoPosition first_pos =
            new GeoPosition(begin.latitude(), begin.longitude());
        track.add(first_pos);
        inter.add(new StopWaypoint(first_pos, current.color(), true));
      }
      Trip next_trip =
          i == path.size() - 1 ? null : path.get(i + 1).trip();
      boolean intersection = next_trip == null || current.trip() != next_trip;
      Color color = current.color();
      Stop stop = current.to().stop();
      GeoPosition pos = new GeoPosition(stop.latitude(), stop.longitude());
      track.add(pos);
      (intersection ? inter : waypoints)
          .add(new StopWaypoint(pos, color, intersection));
      colors.add(color);
      walks.add(current.is_transfer());
    }

    results = new HashSet<StopWaypoint>(waypoints);
    intersections = new HashSet<StopWaypoint>(inter);
    sources.clear();
    targets.clear();

    List<GeoPosition> positions = new ArrayList<>();
    for (StopWaypoint waypoint : sources) {
      positions.add(waypoint.getPosition());
    }
    for (StopWaypoint waypoint : targets) {
      positions.add(waypoint.getPosition());
    }
    for (StopWaypoint waypoint : results) {
      positions.add(waypoint.getPosition());
    }
    for (StopWaypoint waypoint : intersections) {
      positions.add(waypoint.getPosition());
    }
    zoom_on(map_viewer, new HashSet<GeoPosition>(positions));

    source_painter.setWaypoints(sources);
    target_painter.setWaypoints(targets);
    route_painter = new PathPainter(track, colors, walks);
    result_painter.setWaypoints(results);
    intersection_painter.setWaypoints(intersections);
    CompoundPainter<JXMapViewer> all_painters = new CompoundPainter<>();
    all_painters.setPainters(List.of(route_painter, result_painter,
        intersection_painter, source_painter, target_painter));
    map_viewer.setOverlayPainter(all_painters);

    show_details(path);
  }

  /**
   * Show the result details in the left bar
   *
   * @param result The TDSP algorithm path
   */
  private void show_details(List<Edge> result) {
    panel4.removeAll();
    panel4.revalidate();
    panel4.repaint();

    Font title_font = new Font("Arial", Font.BOLD, 24);

    int time = h;
    for (int i = 0; i < result.size(); ++i) {
      Edge current = result.get(i);
      time = current.is_connection() ? current.departure_time() : time;
      if (i == 0 || current.trip() != result.get(i - 1).trip()) {
        JLabel route_title = new JLabel(current.directive(), JLabel.LEFT);
        route_title.setFont(title_font);
        panel4.add(route_title);
      }
    }

    panel4.revalidate();
    panel4.repaint();
  }

  /**
   * Action of the button "Find"
   */
  private void click_find(boolean source) {
    String input = (source ? field1 : field2).getText();
    List<StopWaypoint> waypoints = new ArrayList<>();
    for (Node v : graph.vertices()) {
      Stop stop = v.stop();
      if (stop.name().equals(input)) {
        GeoPosition pos = new GeoPosition(stop.latitude(), stop.longitude());
        waypoints.add(new StopWaypoint(pos, new Color(0, 0, 0), true));
      }
    }

    Set<StopWaypoint> tmp;
    if (source) {
      s = input;
      sources = new HashSet<StopWaypoint>(waypoints);
      tmp = sources;
    } else {
      t = input;
      targets = new HashSet<StopWaypoint>(waypoints);
      tmp = targets;
    }

    List<GeoPosition> positions = new ArrayList<>();
    for (StopWaypoint waypoint : sources) {
      positions.add(waypoint.getPosition());
    }
    for (StopWaypoint waypoint : targets) {
      positions.add(waypoint.getPosition());
    }
    for (StopWaypoint waypoint : results) {
      positions.add(waypoint.getPosition());
    }
    for (StopWaypoint waypoint : intersections) {
      positions.add(waypoint.getPosition());
    }
    zoom_on(map_viewer, new HashSet<GeoPosition>(positions));

    (source ? source_painter : target_painter).setWaypoints(tmp);
    CompoundPainter<JXMapViewer> all_painters = new CompoundPainter<>();
    all_painters.setPainters(List.of(route_painter, result_painter,
        intersection_painter, source_painter, target_painter));
    map_viewer.setOverlayPainter(all_painters);
  }

  /**
   * Zoom on a set of geopositions
   *
   * @param map_viewer The map widget
   * @param positions The set of positions
   */
  private void zoom_on(JXMapViewer mapViewer, Set<GeoPosition> positions) {
    if (positions == null || positions.isEmpty()) {
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
  private Algorithm algo;

  private String s;
  private String t;
  private int h = 8 * 3600 + 30 * 60;

  private Set<StopWaypoint> sources;
  private Set<StopWaypoint> targets;
  private Set<StopWaypoint> results;
  private Set<StopWaypoint> intersections;

  private JFrame main_frame;
  private JPanel waiting_panel;
  private JTextField field1;
  private JTextField field2;
  private JTextField dir_field;
  private JXMapViewer map_viewer;
  private JSplitPane split_pane;
  private JSpinner hour_spinner;
  private JSpinner min_spinner;
  private JSpinner sec_spinner;
  private JSpinner radius_spinner;
  private JTextArea result_area;
  private JPanel panel4;

  private WaypointPainter<StopWaypoint> source_painter;
  private WaypointPainter<StopWaypoint> target_painter;
  private WaypointPainter<StopWaypoint> result_painter;
  private WaypointPainter<StopWaypoint> intersection_painter;
  private PathPainter route_painter;
}
