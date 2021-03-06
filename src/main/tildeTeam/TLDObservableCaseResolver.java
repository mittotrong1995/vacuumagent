package main.tildeTeam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

// TODO: Auto-generated Javadoc
/**
 * The Class TLDPathFinder.
 */
public class TLDObservableCaseResolver {

	/** The graph. */
	SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph;

	/**
	 * Instantiates a new tLD path finder.
	 * 
	 * @param graph
	 *            the graph
	 */
	public TLDObservableCaseResolver(
			SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph) {
		super();
		this.graph = graph;
	}

	public ArrayList<Point> findGoodCycle(Point start,
			ArrayList<Point> dirtyNodes, int energy) {

		dirtyNodes.remove(start);
		ArrayList<Point> goodCycle = this.findCycle(start, dirtyNodes, energy);
		int maxVisited = goodCycle.size() - 2;
		if (maxVisited == dirtyNodes.size()) {
			return buildPath(goodCycle);
		}
		int n = dirtyNodes.size();
		int k = maxVisited;
		System.out.println("N:" + n);
		System.out.println("K:" + k);

		for (int i = n; i > k && i > 1; i--) {
			Point furthestNode = this.furthestNodeFrom(start, dirtyNodes);
			System.out.println(furthestNode);
			dirtyNodes.remove(furthestNode);
			ArrayList<Point> tempCycle = this.findCycle(start, dirtyNodes,
					energy);
			System.out.println("N:" + n);
			int tempVisited = tempCycle.size() - 2;
			System.out.println("K:" + tempVisited);
			if (tempVisited > maxVisited) {
				maxVisited = tempVisited;
				goodCycle = tempCycle;
			}
		}

		if (maxVisited == 0) {
			return new ArrayList<Point>();
		}

		return buildPath(goodCycle);

	}

	/**
	 * Find path.
	 * 
	 * @param start
	 *            the start
	 * @param nodes
	 *            the dirty tiles
	 * @return the array list
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Point> findCycle(Point start, ArrayList<Point> nodes,
			int energy) {
		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> tempUnDirGraph = (SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge>) this.graph
				.clone();

		if (!tempUnDirGraph.containsVertex(start))
			return null;

		ArrayList<Point> toRemove = new ArrayList<Point>();
		for (Point p : tempUnDirGraph.vertexSet()) {
			if (!nodes.contains(p))
				toRemove.add(p);
		}
		toRemove.remove(start);
		for (Point p : toRemove) {
			tempUnDirGraph.removeVertex(p);
		}
		toRemove.clear();

		this.transitiveClosure(tempUnDirGraph);

		Point curr = start;
		ArrayList<Point> hmFromStart = new ArrayList<Point>();
		hmFromStart.add(start);
		boolean finish = false;
		int energySpent = 0;

		while (hmFromStart.size() <= nodes.size() && !finish) {
			boolean firstIter = true;
			int minDistance = 0;
			Point nearest = null;
//			int minObiettive = 0;

			for (DefaultWeightedEdge edge : tempUnDirGraph.edgesOf(curr)) {
				Point newNode = tempUnDirGraph.getEdgeTarget(edge);
				int distance = (int) tempUnDirGraph.getEdgeWeight(edge);
//				int dirtyAround = dirtyAround(newNode, nodes);
				
				if (!hmFromStart.contains(newNode) && newNode != curr) {

					if (firstIter) {
						firstIter = false;
						minDistance = distance;
						nearest = newNode;
//						minObiettive = distance - dirtyAround;
					} else {
//						if (distance - dirtyAround < minObiettive) {
						if (distance < minDistance) {
							minDistance = distance;
							nearest = newNode;
//							minObiettive = distance - dirtyAround;
						}
					}
				}
			}
			if (energySpent + minDistance + 1 <= energy) {
				hmFromStart.add(nearest);
				curr = nearest;
				energySpent += minDistance + 1;
			} else {
				finish = true;
			}
		}

		hmFromStart.add(start);
		return hmFromStart;
	}
	
	private int dirtyAround(Point node, ArrayList<Point> dirtyNodes){
		int count = 0;
		for(Point p: dirtyNodes){
			if(Math.abs(p.x - node.x) +  Math.abs(p.y - node.y) == 1){
				count++;
			}
		}
		return count;
	}

	/**
	 * Builds the path.
	 * 
	 * @param nodes
	 *            the nodes
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
	 * Transitive closure.
	 * 
	 * @param graphToClose
	 *            the to close
	 */
	private void transitiveClosure(
			SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graphToClose) {

		System.out.println(graphToClose.vertexSet().size());
		for (Point p1 : graphToClose.vertexSet()) {
			for (Point p2 : graphToClose.vertexSet()) {
				if (!p1.equals(p2)) {
					if (!graphToClose.containsEdge(p1, p2)) {
						int weight = DijkstraShortestPath.findPathBetween(
								this.graph, p1, p2).size();
						graphToClose.addEdge(p1, p2);
						graphToClose.setEdgeWeight(
								graphToClose.getEdge(p1, p2), weight);
					}
				}
			}
		}
	}
	


	public List<Point> findPath(Point p1, Point p2) {
		List<DefaultWeightedEdge> edgeList = DijkstraShortestPath
				.findPathBetween(graph, p1, p2);

		ArrayList<Point> tempPath = new ArrayList<Point>();
		for (int j = 0; j < edgeList.size(); j++) {
			DefaultWeightedEdge edge = edgeList.get(j);
			tempPath.add(graph.getEdgeTarget(edge));
		}
		return tempPath;
	}

	private int distanceBetween(Point p1, Point p2) {
		return findPath(p1, p2).size();
	}

	public Point furthestNodeFrom(Point start, List<Point> nodes) {
		Point furthest = nodes.get(0);
		int max = distanceBetween(start, furthest);
		for (Point point : nodes) {
			int dist = distanceBetween(start, point);
			if (dist > max) {
				max = dist;
				furthest = point;
			}
		}
		return furthest;
	}

}
