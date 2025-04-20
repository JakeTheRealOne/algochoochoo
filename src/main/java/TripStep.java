import java.util.Map;

/**
 * (Literally) A trip step
 *
 * @author Bilal Vandenberge
 */
public class TripStep {
  // #### Public methods ####

  public TripStep(StopTime stop_time, Map<String, Stop> stops) {
    stop = stops.get(stop_time.stop_id());
    if (stop == null) {
      throw new IllegalArgumentException("Unknown stop id: '" + stop_time.stop_id() + "'");
    }
    time = stop_time.departure_time();
  }

  @Override
  public String toString() {
    return "Step(" + stop.name() + ", " + beautiful_time() + ")";
  }

  // #### Getter ####

  public Stop stop() {
    return stop;
  }

  public long time() {
    return time;
  }

  // #### Attribute ####

  Stop stop;
  long time;

  // #### Private helpers ####

  private String beautiful_time() {
    return String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60);
  }
}