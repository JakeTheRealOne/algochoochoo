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
 * (Literally) A route type
 *
 * @author Bilal Vandenberge
 */
public enum RouteType {
  /** The route is a train line */
  TRAIN,

  /** The route is a metro line */
  METRO,

  /** The route is a bus line */
  BUS,

  /** The route is a tram line */
  TRAM,

  /** The route is a footpath */
  FOOT
}
