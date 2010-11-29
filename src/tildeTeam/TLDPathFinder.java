package tildeTeam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.HamiltonianCycle;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

// TODO: Auto-generated Javadoc
/**
 * The Class TLDPathFinder.
 */
public class TLDPathFinder {

	/** The graph. */
	SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph;

	/**
	 * Instantiates a new tLD path finder.
	 *
	 * @param graph the graph
	 */
	public TLDPathFinder(
			SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph) {
		super();
		this.graph = graph;
	}

	/**
	 * Find path.
	 *
	 * @param start the start
	 * @param nodes the dirty tiles
	 * @return the array list
	 */
	public ArrayList<Point> findPath(Point start, ArrayList<Point> nodes) {
		SimpleWeightedGraph<Point, DefaultWeightedEdge> unDirGraph = toUnDirGraph(this.graph);
		this.transitiveClosure(unDirGraph);

		if (!unDirGraph.containsVertex(start))
			return null;

		ArrayList<Point> toRemove = new ArrayList<Point>();
		for (Point p : unDirGraph.vertexSet()) {
			if (!nodes.contains(p))
				toRemove.add(p);
		}
		toRemove.remove(start);
		for (Point p : toRemove) {
			unDirGraph.removeVertex(p);
		}

		List<Point> hm = HamiltonianCycle
				.getApproximateOptimalForCompleteGraph(unDirGraph);

		ArrayList<Point> hmFromStart = new ArrayList<Point>();

		int startIndex = hm.indexOf(start);
		for (int i = startIndex; i < hm.size(); i++) {
			hmFromStart.add(hm.get(i));
		}
		for (int i = 0; i <= startIndex; i++) {
			hmFromStart.add(hm.get(i));
		}

		return buildPath(hmFromStart);

	}

	/**
	 * Builds the path.
	 *
	 * @param nodes the nodes
	 * @return the array list
	 */
	private ArrayList<Point> buildPath(ArrayList<Point> nodes) {
		ArrayList<Point> finalPath = new ArrayList<Point>();
		for (int i = 0; i < nodes.size() - 1; i++) {

			Point p1 = nodes.get(i);
			Point p2 = nodes.get(i + 1);

			DijkstraShortestPath<Point, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<Point, DefaultWeightedEdge>(
					this.graph, p1, p2);
			GraphPath<Point, DefaultWeightedEdge> path = pathfinder.getPath();
			List<DefaultWeightedEdge> edgeList = path.getEdgeList();

			ArrayList<Point> tempPath = new ArrayList<Point>();
			for (int j = 0; j < edgeList.size(); j++) {
				DefaultWeightedEdge edge = edgeList.get(j);
				tempPath.add(this.graph.getEdgeTarget(edge));
			}
			finalPath.addAll(tempPath);
		}
		return finalPath;
	}

	/**
	 * To un dir graph.
	 *
	 * @param in the in
	 * @return the simple weighted graph
	 */
	private SimpleWeightedGraph<Point, DefaultWeightedEdge> toUnDirGraph(
			SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> in) {
		SimpleWeightedGraph<Point, DefaultWeightedEdge> out = new SimpleWeightedGraph<Point, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		for (Point p : in.vertexSet()) {
			out.addVertex(p);
		}

		for (DefaultWeightedEdge edge : in.edgeSet()) {
			out.addEdge(in.getEdgeSource(edge), in.getEdgeTarget(edge));
		}

		return out;
	}

	/**
	 * Transitive closure.
	 *
	 * @param toClose the to close
	 */
	private void transitiveClosure(
			SimpleWeightedGraph<Point, DefaultWeightedEdge> toClose) {

		for (Point p1 : toClose.vertexSet()) {
			for (Point p2 : toClose.vertexSet()) {
				if (p1 != p2) {
					DijkstraShortestPath<Point, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<Point, DefaultWeightedEdge>(
							toClose, p1, p2);
					GraphPath<Point, DefaultWeightedEdge> path = pathfinder
							.getPath();
					toClose.addEdge(p1, p2);
					toClose.setEdgeWeight(toClose.getEdge(p1, p2),
							path.getWeight());
				}
			}
		}

	}

}
