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
    return "Step(" + stop.name() + ", " + String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60) + ")";
  }

  // #### Getter ####

  // TODO EHEHEH

  // #### Attribute ####

  Stop stop;
  long time;
}