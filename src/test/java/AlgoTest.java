import static org.junit.Assert.assertEquals;
import org.junit.Test;

import algochoochoo.graph.*;
import algochoochoo.query.*;
import algochoochoo.cli.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class AlgoTest {
  @Test
  /**
   * Test the base case, with no errors
   */
  public void regular_test() {

  }

  @Test
  /**
   * Test with source == target
   */
  public void s_eq_t_test() {

  }

  @Test
  /**
   * Test with inexistant source
   */
  public void s_nexist_test() {

  }

  @Test
  /**
   * Test with inexistant target
   */
  public void t_nexist_test() {

  }

  @Test
  /**
   * Test with wrong exec args
   */
  public void invalid_args_test() {

  }

  @Test
  /**
   * Test with an h > 24:00:00
   */
  public void big_h_test() {

  }

  @Test
  /**
   * Test the footpath and footpath search radius
   */
  public void foot_test() {

  }

  @Test
  /**
   * Test the transport weights
   */
  public void weight_test() {

  }
  

  public void print_test() {
    AlgoSettings set = new AlgoSettings();
    Graph graph = new Graph(new GraphSettings("src/main/resources/GTFS"));
    Algorithm algo = new Algorithm(graph, set);
    List<Edge> path1 = algo.dijkstra("AUMALE", "DELTA", 8 * 3600 + 0 * 60);
    List<Edge> path2 = algo.dijkstra("Antwerpen Centraal Station",
        "CHIMAY Petit Virelles", 14 * 3600 + 14 * 60 + 14);
    List<Edge> path3 = algo.dijkstra(
        "Alveringem Nieuwe Herberg", "Aubange", 10 * 3600 + 30 * 60);
    System.out.println();
    View.print(path1, 8 * 3600 + 0 * 60);
    System.out.println();
    View.print(path2, 14 * 3600 + 14 * 60 + 14);
    System.out.println();
    View.print(path3, 10 * 3600 + 30 * 60);
  }

  public void runtime_test() {
    long start = System.nanoTime();
    GraphSettings graphset = new GraphSettings("src/main/resources/GTFS");
    AlgoSettings settings = new AlgoSettings();
    Graph graph = new Graph(graphset);
    Algorithm algo = new Algorithm(graph, settings);
    long end = System.nanoTime();
    long duration = end - start;
    System.out.println("[Dijkstra] : Construction en " + duration / 1000000000f
        + " secondes");

    start = System.nanoTime();
    List<Edge> path1 = algo.dijkstra("AUMALE", "FRAITEUR", 7 * 3600 + 0 * 60);
    end = System.nanoTime();
    duration = end - start;
    System.out.println("[Dijkstra] : Requete #1   en " + duration / 1000000000f
        + " secondes");
    start = System.nanoTime();
    List<Edge> path2 = algo.dijkstra("Antwerpen Centraal Station",
        "CHIMAY Petit Virelles", 14 * 3600 + 14 * 60 + 14);
    end = System.nanoTime();
    duration = end - start;
    System.out.println("[Dijkstra] : Requete #2   en " + duration / 1000000000f
        + " secondes");
    start = System.nanoTime();
    List<Edge> path3 = algo.dijkstra(
        "Alveringem Nieuwe Herberg", "Aubange", 10 * 3600 + 30 * 60);
    end = System.nanoTime();
    duration = end - start;
    System.out.println("[Dijkstra] : Requete #3   en " + duration / 1000000000f
        + " secondes");
    start = System.nanoTime();
    List<Edge> path4 = algo.dijkstra(
        "Paris Nord (FR)", "Amsterdam Cs (NL)", 10 * 3600 + 30 * 60);
    end = System.nanoTime();
    duration = end - start;
    System.out.println("[Dijkstra] : Requete #4   en " + duration / 1000000000f
        + " secondes");
  }
}
