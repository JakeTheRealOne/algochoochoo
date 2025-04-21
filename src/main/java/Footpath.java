/**
 * (Literally) A footpath
 * 
 * @author Bilal Vandenberge
 */
public class Footpath {
  // #### Public methods ####

  /**
   * Construct a new Footpath object
   * 
   * @param first The first stop of the path
   * @param second The second stop of the path
   * @param dur The duration of the footpath
   */
  public Footpath(Stop first, Stop second, int dur) {
    from = first;
    to = second;
    duration = dur;
    if (duration < 0) {
      throw new IllegalArgumentException("Cannot create footpath with negative duration");
    }
  }

  /** Convert a Footpath object to string */
  @Override
  public String toString() {
    return "Footpath(" + from.id() + " to " + to.id() + " in " + duration + "s)";
  }

  // #### Getters ####

  /**
   * Get the first node of the path
   * 
   * @return The first node of the path
   */
  public Stop from() {
    return from;
  }

  /**
   * Get the second node of the path
   * 
   * @return The second node of the path
   */
  public Stop to() {
    return to;
  }

  /**
   * Get the duration of the path
   * 
   * @return The duration of the path
   */
  public int duration() {
    return duration;
  }


  // #### Attributes ####

  private Stop from;
  private Stop to;
  private int duration;
}