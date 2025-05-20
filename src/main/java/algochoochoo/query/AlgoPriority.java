/*
 * This file is part of Algochoochoo.
 *
 * Licensed under the GNU General Public License v3.
 * See <https://www.gnu.org/licenses/gpl-3.0.html> for details.
 * 
 * 2025
 * Bilal Vandenberge
 */

package algochoochoo.query;

/**
 * The priority of a TDSP algorithm
 *
 * @author Bilal Vandenberge
 */
public enum AlgoPriority {
  /** The algorithm will minimize the path duration */
  TOTAL_DURATION,

  /** The algorithm will minimize the number of trip changes */
  TRIP_CHANGES,
}
