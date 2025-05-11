/**
 * Convert a CSV row into an object of type T
 *
 * @param <T> The type of the factory output objects
 * @author Bilal Vandenberge
 */
@FunctionalInterface
public interface CsvFactory<T> {
  /**
   * Call the constructor of T with a CSV row
   *
   * @param row The CSV row
   * @return The result object of type T
   */
  T fromRow(String[] row);
}
