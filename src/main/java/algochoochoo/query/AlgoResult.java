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

import algochoochoo.graph.Edge;
import java.util.ArrayList;
import java.util.List;

/**
 * Store result from a TDSP algorithm
 *
 * @author Bilal Vandenberge
 */
public class AlgoResult {
  // #### Attributes ####

  /** The shortest path found */
  public List<Edge> path = new ArrayList<>();

  /** The number of vertices visited during the algorithm */
  public int visited_vertices = 0;

  /** The runtime of the algorithm */
  public double runtime = 0;

  /** The runtime of the precomputing (graph construction and GTFS parsing) */
  public double preruntime = 0;
}
