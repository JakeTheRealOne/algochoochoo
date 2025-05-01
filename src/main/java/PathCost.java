/**
 * Store cost of a path
 * 
 * @author Bilal Vandenberge
 */
public class PathCost {
  // #### Public methods ####

  /**
   * Construct a new PathCost object
   * 
   * @param dur The duration of the path
   */
  PathCost(int dur) {
    duration = dur;
  }

  // #### Getters ####

  /**
   * Get the duration of the path
   * 
   * @return The duration of the path 
   */
  public int duration() {
    return duration;
  }

  // #### Setters ####

  /**
   * Set the duration of the path
   */
  public void set_duration(int dur) {
    duration = dur;
  }

  // #### Attributes ####

  private int duration;
}
