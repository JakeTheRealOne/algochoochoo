import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A data structure that allows fast iteration and constant time access by element id
 *
 * @author
 */
public class DictoList<T> implements Iterable<T> {

  // #### Public methods ####

  public DictoList() {
    list = new ArrayList<>();
    map = new LinkedHashMap<>();
  }

  public DictoList(int size) {
    list = new ArrayList<>(size);
    map = new LinkedHashMap<>(size);
  }

  /** Iterate over the elements of the data structure */
  @Override
  public Iterator<T> iterator() {
    return list.iterator();
  }

  /**
   * Get the value of an id in constant time
   *
   * @param key The id
   * @return The object owning the id
   */
  T get(String key) {
    Integer index = map.get(key);
    if (index == null) {
      return null;
    }
    return list.get(index);
  }

  /**
   * Set the value related to an id
   *
   * @param key The id
   * @param value The new value
   */
  void put(String key, T value) {
    Integer exist = map.get(key);
    if (exist != null && list.get(exist) != value) {
      throw new IllegalArgumentException(
          "The id '"
              + key
              + "' is shared by two different objects: "
              + value
              + " and "
              + list.get(exist));
    } else if (exist != null) {
      return;
    }

    int index = list.size();
    list.add(value);
    map.put(key, index);
  }

  // #### Attributes ####
  private Map<String, Integer> map;
  private ArrayList<T> list;
}
