import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (Literally) A trip line
 *
 * @author Bilal Vandenberge
 */
public class TripLine {
  // #### Public methods ####

  public TripLine(Trip trip, Map<String, Route> routes) {
    route = routes.get(trip.route_id());
    if (route == null) {
      throw new IllegalArgumentException("Unknown route id: '" + trip.route_id() + "'");
    }
    id = trip.id();
    sequence = new ArrayList<>();
    null_steps = 0;
    sequence.ensureCapacity(50); // Alloc optimization
  }

  public void add(StopTime stop_time, Map<String, Stop> stops) {
    int index = stop_time.stop_idx(); // 1-Indexed
    if (index == 0) {
      throw new IllegalArgumentException("Illegal stop index in 1-Indexed: 0");
    }
    while (sequence.size() < index) {
      sequence.add(null);
      ++null_steps;
    }
    --null_steps;
    sequence.set(index - 1, new TripStep(stop_time, stops));
  }

  @Override
  public String toString() {
    return "Line(" + id + ", " + sequence.size() + " stops)";
  }

  // #### Getters ####

  // TODO o_o

  // !!!! docstrings and other getters

  public String id() {
    return id;
  }

  public ArrayList<TripStep> sequence() {
    return sequence;
  }

  public int null_steps() {
    return null_steps;
  }

  // #### Attributes ###

  Route route;
  ArrayList<TripStep> sequence;
  String id;
  int null_steps;
}
