package algochoochoo.query;

import java.util.List;
import java.util.ArrayList;
import algochoochoo.graph.Edge;

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

  /** The runtime of the precomputing (graph construction & GTFS parsing) */
  public double preruntime = 0;
}
