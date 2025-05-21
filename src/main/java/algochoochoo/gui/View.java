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

import algochoochoo.graph.*;
import algochoochoo.parsing.RouteType;
import algochoochoo.parsing.Stop;
import algochoochoo.parsing.StopTime;
import algochoochoo.parsing.Trip;
import algochoochoo.query.AlgoPriority;
import algochoochoo.query.AlgoResult;
import algochoochoo.query.AlgoSettings;
import algochoochoo.query.Algorithm;
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
    button1 = new JButton("Find");
    button1.setMaximumSize(size);
    button1.setBorder(new EmptyBorder(1, 1, 1, 1));

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
    button2 = new JButton("Find");
    button2.setMaximumSize(size);
    button2.setBorder(new EmptyBorder(1, 1, 1, 1));

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
    JToggleButton togg1_btn = new JToggleButton("Show graph settings ▲");
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
    JButton rad_confirm_button = new JButton("Confirm");
    radius_panel.add(rad_confirm_button, gbc);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gset2_panel.add(radius_panel, gbc);

    JPanel dir_panel = new JPanel(new GridBagLayout());
    dir_field = new JTextField("src/main/resources/EMPTY_GTFS", 15);
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
    JButton dir_confirm_button = new JButton("Confirm");
    dir_panel.add(dir_confirm_button, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gset2_panel.add(dir_panel, gbc);
    gset2_panel.setVisible(false);
    togg1_btn.addActionListener(e -> {
      boolean expanded = togg1_btn.isSelected();
      gset2_panel.setVisible(expanded);
      togg1_btn.setText(expanded ? "Hide graph settings ▼"
                                 : "Show graph settings ▲");
      gset_panel.getParent().revalidate();
    });
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gset_panel.add(togg1_btn, gbc);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gset_panel.add(gset2_panel, gbc);

    JPanel aset_panel = new JPanel(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JToggleButton togg2_btn = new JToggleButton("Show algo settings ▲");
    JPanel aset2_panel = new JPanel(new GridBagLayout());

    JPanel priority_panel = new JPanel(new GridBagLayout());
    String[] choices = {"Duration", "Trip changes"};
    priority_combo = new JComboBox<>(choices);
    priority_combo.setMaximumSize(size);
    priority_combo.setBorder(new EmptyBorder(5, 5, 5, 5));

    SpinnerNumberModel foot_weight_model =
        new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1);
    SpinnerNumberModel metro_weight_model =
        new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1);
    SpinnerNumberModel tram_weight_model =
        new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1);
    SpinnerNumberModel bus_weight_model =
        new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1);
    SpinnerNumberModel train_weight_model =
        new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1);
    foot_spinner = new JSpinner(foot_weight_model);
    foot_spinner.setMaximumSize(size);
    foot_spinner.setBorder(new EmptyBorder(5, 5, 5, 5));
    train_spinner = new JSpinner(train_weight_model);
    train_spinner.setMaximumSize(size);
    train_spinner.setBorder(new EmptyBorder(5, 5, 5, 5));
    tram_spinner = new JSpinner(tram_weight_model);
    tram_spinner.setMaximumSize(size);
    tram_spinner.setBorder(new EmptyBorder(5, 5, 5, 5));
    metro_spinner = new JSpinner(metro_weight_model);
    metro_spinner.setMaximumSize(size);
    metro_spinner.setBorder(new EmptyBorder(5, 5, 5, 5));
    bus_spinner = new JSpinner(bus_weight_model);
    bus_spinner.setMaximumSize(size);
    bus_spinner.setBorder(new EmptyBorder(5, 5, 5, 5));

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    priority_panel.add(new JLabel("Priority"), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    priority_panel.add(priority_combo, gbc);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    aset2_panel.add(priority_panel, gbc);

    JPanel weight_panel = new JPanel(new GridBagLayout());

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    weight_panel.add(new JLabel("Foot weight: "), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    weight_panel.add(foot_spinner, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    weight_panel.add(new JLabel("Train weight: "), gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    weight_panel.add(train_spinner, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    weight_panel.add(new JLabel("Tram weight: "), gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    weight_panel.add(tram_spinner, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    weight_panel.add(new JLabel("Metro weight: "), gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    weight_panel.add(metro_spinner, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    weight_panel.add(new JLabel("Bus weight: "), gbc);

    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    weight_panel.add(bus_spinner, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    aset2_panel.add(weight_panel, gbc);
    aset2_panel.setVisible(false);
    togg2_btn.addActionListener(e -> {
      boolean expanded = togg2_btn.isSelected();
      aset2_panel.setVisible(expanded);
      togg2_btn.setText(expanded ? "Hide algo settings ▼"
                                 : "Show algo settings ▲");
      aset_panel.getParent().revalidate();
    });
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    aset_panel.add(togg2_btn, gbc);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;
    aset_panel.add(aset2_panel, gbc);

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
    gbc.anchor = GridBagConstraints.CENTER;
    side_panel.add(aset_panel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 6;
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

    rad_confirm_button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        change_radius();
      }
    });

    dir_confirm_button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        change_path();
      }
    });

    priority_combo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        change_priority();
      }
    });

    tram_spinner.addChangeListener(e -> {
      change_weight(RouteType.TRAM, (double)tram_spinner.getValue());
    });

    metro_spinner.addChangeListener(e -> {
      change_weight(RouteType.METRO, (double)metro_spinner.getValue());
    });

    train_spinner.addChangeListener(e -> {
      change_weight(RouteType.TRAIN, (double)train_spinner.getValue());
    });

    bus_spinner.addChangeListener(
        e -> { change_weight(RouteType.BUS, (double)bus_spinner.getValue()); });

    foot_spinner.addChangeListener(e -> {
      change_weight(RouteType.FOOT, (double)foot_spinner.getValue());
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
   * Change the path for GTFS data
   */
  private void change_path() {
    // main_frame.remove(waiting_panel);
    // main_frame.add(split_pane);
    main_frame.remove(split_pane);
    main_frame.add(waiting_panel);
    map_viewer.revalidate();
    map_viewer.repaint();
    main_frame.revalidate();
    main_frame.repaint();

    SwingWorker<Void, Void> worker = new SwingWorker<>() {
      @Override
      protected Void doInBackground() throws Exception {
        GraphSettings old_set = graph.settings().clone();
        String old_path = old_set.GTFS_path;
        try {
          String path = (String)dir_field.getText();
          if (!path.isEmpty()) {
            GraphSettings new_set = graph.settings().clone();
            new_set.GTFS_path = path;
            graph.reload(new_set);
          }
        } catch (Exception e) {
          dir_field.setText((old_path));
          graph.reload(old_set);
        }

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
   * Change the foot radius
   */
  private void change_radius() {
    // main_frame.remove(waiting_panel);
    // main_frame.add(split_pane);
    main_frame.remove(split_pane);
    main_frame.add(waiting_panel);
    map_viewer.revalidate();
    map_viewer.repaint();
    main_frame.revalidate();
    main_frame.repaint();

    SwingWorker<Void, Void> worker = new SwingWorker<>() {
      @Override
      protected Void doInBackground() throws Exception {
        GraphSettings old_set = graph.settings().clone();
        int old_value = old_set.foot_radius;
        try {
          int value = (int)radius_spinner.getValue();
          if (value > 0) {
            GraphSettings new_set = graph.settings().clone();
            new_set.foot_radius = value;
            graph.reload(new_set);
          }
        } catch (Exception e) {
          radius_spinner.setValue((old_value));
          graph.reload(old_set);
        }

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
   * Change the priority of the algorithm
   */
  private void change_priority() {
    String p = (String)priority_combo.getSelectedItem();
    if (p == "trip") {
      algo.settings().priority = AlgoPriority.TRIP_CHANGES;
    } else {
      algo.settings().priority = AlgoPriority.TOTAL_DURATION;
    }
  }

  private void change_weight(RouteType type, double value) {
    if (value > 0 && value <= 10) {
      algo.settings().weights.put(type, value);
    }
  }

  /**
   * Action of the button "Start search"
   */
  private void execute_algorithm() {
    if (s == null) {
      button1.setBorder(new LineBorder(Color.RED, 2));
      return;
    } else if (t == null) {
      button1.setBorder(new EmptyBorder(2, 2, 2, 2));
      button2.setBorder(new LineBorder(Color.RED, 2));
      return;
    }

    button1.setBorder(new EmptyBorder(2, 2, 2, 2));
    button2.setBorder(new EmptyBorder(2, 2, 2, 2));

    int hour = (int)hour_spinner.getValue();
    int min = (int)min_spinner.getValue();
    int sec = (int)sec_spinner.getValue();
    h = 3600 * hour + 60 * min + sec;

    AlgoResult result = null;
    try {
      result = algo.dijkstra(s, t, h);
    } catch (IllegalArgumentException e) {
      field1.setBorder(new LineBorder(Color.RED, 2));
      field2.setBorder(new LineBorder(Color.RED, 2));
    }

    s = null;
    t = null;

    if (result == null)
      return;

    field1.setText("");
    field2.setText("");

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
      Trip next_trip = i == path.size() - 1 ? null : path.get(i + 1).trip();
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
                                     intersection_painter, source_painter,
                                     target_painter));
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
    Font reg_font = new Font("Arial", Font.PLAIN, 20);
    Font smol_font = new Font("Arial", Font.ITALIC, 18);

    int time = h;
    for (int i = 0; i < result.size(); ++i) {
      Edge current = result.get(i);
      if (i == 0 || current.trip() != result.get(i - 1).trip()) {
        if (i > 0) {
          JLabel stop_name = new JLabel(
              " " + result.get(i - 1).to().stop().name(), JLabel.LEFT);
          JLabel stop_time =
              new JLabel(String.format(" %02d:%02d:%02d", time / 3600,
                                       (time % 3600) / 60, time % 60),
                         JLabel.LEFT);
          stop_name.setFont(reg_font);
          stop_time.setFont(smol_font);
          stop_name.setAlignmentX(Component.LEFT_ALIGNMENT);
          stop_time.setAlignmentX(Component.LEFT_ALIGNMENT);
          panel4.add(stop_name);
          panel4.add(stop_time);
        }

        JPanel panel = new JPanel(new BorderLayout());
        JPanel inner_panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 5);

        JPanel circle = new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(current.color());
            g.fillOval(0, 0, getWidth(), getHeight());
          }
        };
        circle.setPreferredSize(new Dimension(15, 15));

        gbc.gridx = 0;
        gbc.gridy = 0;
        inner_panel.add(circle, gbc);

        JLabel route_title = new JLabel(current.directive(), JLabel.LEFT);
        route_title.setFont(title_font);

        gbc.gridx = 1;
        inner_panel.add(route_title, gbc);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(inner_panel, BorderLayout.WEST);
        panel4.add(panel);
      }
      time = current.is_connection() ? current.departure_time() : time;
      JLabel stop_name =
          new JLabel(" " + current.from().stop().name(), JLabel.LEFT);
      JLabel stop_time =
          new JLabel(String.format(" %02d:%02d:%02d", time / 3600,
                                   (time % 3600) / 60, time % 60),
                     JLabel.LEFT);
      stop_name.setFont(reg_font);
      stop_time.setFont(smol_font);
      stop_name.setAlignmentX(Component.LEFT_ALIGNMENT);
      stop_time.setAlignmentX(Component.LEFT_ALIGNMENT);
      panel4.add(stop_name);
      panel4.add(stop_time);
      time += current.duration();
    }
    if (!result.isEmpty()) {
      JLabel stop_name = new JLabel(
          " " + result.get(result.size() - 1).to().stop().name(), JLabel.LEFT);
      JLabel stop_time =
          new JLabel(String.format(" %02d:%02d:%02d", time / 3600,
                                   (time % 3600) / 60, time % 60),
                     JLabel.LEFT);
      stop_name.setFont(reg_font);
      stop_time.setFont(smol_font);
      stop_name.setAlignmentX(Component.LEFT_ALIGNMENT);
      stop_time.setAlignmentX(Component.LEFT_ALIGNMENT);
      panel4.add(stop_name);
      panel4.add(stop_time);
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
                                     intersection_painter, source_painter,
                                     target_painter));
    map_viewer.setOverlayPainter(all_painters);
  }

  /**
   * Zoom on a set of geopositions
   *
   * @param map_viewer The map widget
   * @param positions  The set of positions
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
  private JButton button1;
  private JButton button2;
  private JTextField field2;
  private JTextField dir_field;
  private JXMapViewer map_viewer;
  private JSplitPane split_pane;
  private JSpinner hour_spinner;
  private JSpinner min_spinner;
  private JSpinner sec_spinner;
  private JSpinner radius_spinner;
  private JComboBox priority_combo;
  private JTextArea result_area;
  private JPanel panel4;
  private JSpinner foot_spinner;
  private JSpinner tram_spinner;
  private JSpinner train_spinner;
  private JSpinner metro_spinner;
  private JSpinner bus_spinner;

  private WaypointPainter<StopWaypoint> source_painter;
  private WaypointPainter<StopWaypoint> target_painter;
  private WaypointPainter<StopWaypoint> result_painter;
  private WaypointPainter<StopWaypoint> intersection_painter;
  private PathPainter route_painter;
}
