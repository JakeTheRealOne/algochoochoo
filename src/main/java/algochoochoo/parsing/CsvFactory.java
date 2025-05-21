/*
 * This file is part of Algochoochoo.
 *
 * Licensed under the GNU General Public License v3.
 * See <https://www.gnu.org/licenses/gpl-3.0.html> for details.
 *
 * 2025
 * Bilal Vandenberge
 */

package algochoochoo.parsing;

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
