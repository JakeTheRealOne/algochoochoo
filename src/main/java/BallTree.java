import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * A simple BallTree implementation for geographic Stops using Haversine distance.
 * Optimized pivot selection for large datasets (~67k stops) via two-sweep farthest-point heuristic.
 *
 * @author ChatGPT
 */
public class BallTree {
    private static final int LEAF_SIZE = 10;
    private final Node root;

    // public static void main(String[] args) {
    //   Map<String, Stop> stops = Parser.stops("src/main/resources/GTFS");
    //   BallTree bt = new BallTree(stops.values());

    //   Stop test_stop = stops.get("STIB-8162");
    //   System.out.println(test_stop);
    //   ArrayList<Neighbor> nears = bt.get(test_stop, 0.5);
    //   for (Neighbor stop : nears)
    //   {System.out.println("!" + stop);}
    // }

    public void update_footpaths(Collection<Stop> stops) {
      int footpaths = 0;
      for (Stop stop : stops) {
        ArrayList<Neighbor> neighbors = get(stop, 0.5);
        stop.setNeighbors(neighbors);
        footpaths += neighbors.size();
      }
      System.out.println("Number of footpaths: " + footpaths);
    }

    /**
     * Constructs a BallTree from a collection of stops.
     * @param stops the collection of stops to index
     * @throws IllegalArgumentException if the collection is null or empty
     */
    public BallTree(Collection<Stop> stops) {
      if (stops == null || stops.isEmpty()) {
        throw new IllegalArgumentException("Stop collection must be non-null and non-empty");
      }
      // Copy to a list for indexing and recursion
      List<Stop> stopList = new ArrayList<>(stops);
      this.root = buildTree(stopList);
    }

    /**
     * Returns all stops within the given radius (in kilometers) of the source stop.
     * @param source the query stop
     * @param radius search radius in kilometers
     * @return list of stops within radius
     */
    public ArrayList<Neighbor> get(Stop source, double radius) {
        ArrayList<Neighbor> result = new ArrayList<>();
        search(root, source, radius, result);
        return result;
    }

    // Recursively builds the BallTree
    private Node buildTree(List<Stop> stops) {
        Node node = new Node();
        if (stops.size() <= LEAF_SIZE) {
            node.isLeaf = true;
            node.points = new ArrayList<>(stops);
            node.center = computeCentroid(stops);
            node.radius = maxDistance(node.center, stops);
            return node;
        }

        // Select pivots via two-sweep heuristic (O(n))
        Stop[] pivots = findFarthestPair(stops);
        Stop p1 = pivots[0];
        Stop p2 = pivots[1];

        List<Stop> leftList = new ArrayList<>();
        List<Stop> rightList = new ArrayList<>();
        for (Stop s : stops) {
            if (distance(s, p1) < distance(s, p2)) leftList.add(s);
            else rightList.add(s);
        }
        // Fallback to leaf if imbalance
        if (leftList.isEmpty() || rightList.isEmpty()) {
            node.isLeaf = true;
            node.points = new ArrayList<>(stops);
            node.center = computeCentroid(stops);
            node.radius = maxDistance(node.center, stops);
            return node;
        }

        node.isLeaf = false;
        node.left = buildTree(leftList);
        node.right = buildTree(rightList);
        node.center = computeCentroid(stops);
        node.radius = maxDistance(node.center, stops);
        return node;
    }

    // Searches the tree for stops within radius of source
    private void search(Node node, Stop source, double radius, ArrayList<Neighbor> result) {
        double distToCenter = distance(source, node.center);
        if (distToCenter > radius + node.radius) {
            return;  // prune this branch
        }
        if (node.isLeaf) {
            for (Stop s : node.points) {
                if (s == source) { continue; }
                double dist = distance(source, s);
                if (dist <= radius) {
                    result.add(new Neighbor(s, (int) (dist * 1000)));
                }
            }
        } else {
            search(node.left, source, radius, result);
            search(node.right, source, radius, result);
        }
    }

    // Two-sweep farthest-point heuristic in O(n)
    private Stop[] findFarthestPair(List<Stop> stops) {
        Stop p1 = stops.get(0);
        Stop far = p1;
        double maxDist = -1;
        // First sweep: farthest from initial p1
        for (Stop s : stops) {
            double d = distance(p1, s);
            if (d > maxDist) {
                maxDist = d;
                far = s;
            }
        }
        // Second sweep: farthest from 'far'
        Stop p2 = far;
        Stop p1New = far;
        maxDist = -1;
        for (Stop s : stops) {
            double d = distance(p1New, s);
            if (d > maxDist) {
                maxDist = d;
                p2 = s;
            }
        }
        return new Stop[]{p1New, p2};
    }

    // Finds the centroid (mean latitude/longitude) of a point set
    private Stop computeCentroid(List<Stop> stops) {
        double sumLat = 0, sumLon = 0;
        for (Stop s : stops) {
            sumLat += s.latitude();
            sumLon += s.longitude();
        }
        double avgLat = sumLat / stops.size();
        double avgLon = sumLon / stops.size();
        EarthPos pos = new EarthPos(Double.toString(avgLat), Double.toString(avgLon));
        return new Stop(new String[]{"", "centroid", Double.toString(avgLat), Double.toString(avgLon)});
    }

    // Computes max distance from center to any stop in list
    private double maxDistance(Stop center, List<Stop> stops) {
        double max = 0;
        for (Stop s : stops) {
            double d = distance(center, s);
            if (d > max) max = d;
        }
        return max;
    }

    // Haversine distance (in kilometers) between two stops
    private double distance(Stop s1, Stop s2) {
        double lat1 = Math.toRadians(s1.latitude());
        double lon1 = Math.toRadians(s1.longitude());
        double lat2 = Math.toRadians(s2.latitude());
        double lon2 = Math.toRadians(s2.longitude());
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double R = 6371; // Earth radius in km
        return R * c;
    }

    // Tree node
    private static class Node {
        Stop center;
        double radius;
        Node left, right;
        List<Stop> points;
        boolean isLeaf;
    }
}
