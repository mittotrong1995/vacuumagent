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

import vacuumAgent.VAPercept;

// TODO: Auto-generated Javadoc
/**
 * The Class TLDPathFinder.
 */
public class TLDObservableCaseResolver {

	/** The graph. */
	SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph;
	SimpleWeightedGraph<Point, DefaultWeightedEdge> unDirGraph;

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
		this.unDirGraph = toUnDirGraph(this.graph);
		this.transitiveClosure(unDirGraph);
	}
	
	public ArrayList<Point> findGoodCycle (VAPercept percept, Point start, ArrayList<Point> dirtyNodes, int energy){
		
		ArrayList<Point> goodCycle = this.findCycle(start, dirtyNodes);
		
		if(goodCycle.size()+dirtyNodes.size()<= energy){
			return goodCycle;
	    }
		
		int maxVisited = this.dirtyVisited(goodCycle, dirtyNodes, energy);
		int n = dirtyNodes.size();
		int k =  maxVisited;
		goodCycle = cleanAndGoBack(start, goodCycle, dirtyNodes, maxVisited);
		System.out.println("N:"+n);
		System.out.println("K:"+k);
		

		for(int i = n; i > k && i > 1; i--){
			Point furthestNode = this.furthestNodeFrom(start, dirtyNodes); 
			System.out.println(furthestNode);
			dirtyNodes.remove(furthestNode);
			ArrayList<Point> tempCycle = this.findCycle(start, dirtyNodes);
			System.out.println("N:"+n);
			int tempVisited = dirtyVisited(tempCycle, dirtyNodes, energy);
			System.out.println("K:"+tempVisited);
			if(tempVisited > maxVisited){
				maxVisited = tempVisited;
				goodCycle = cleanAndGoBack(start, tempCycle, dirtyNodes, maxVisited);
			}
		}
		
		if (maxVisited == 0){
			return new ArrayList<Point>();
		}
		
		return goodCycle;
	}
	
	private int dirtyVisited(ArrayList<Point> path, ArrayList<Point> dirtyNodes, int energy){
		int cont = 0;
		int sucked = 0;
		
		for(int i = 0; i < energy - sucked && i < path.size(); i++){
			if(dirtyNodes.contains(path.get(i))){
				if(i < energy-sucked-1){
					cont++;
					sucked++;
				}
			}
		}
		return cont;
	}
	
	private ArrayList<Point> cleanAndGoBack(Point start, ArrayList<Point> path, ArrayList<Point> dirtyNodes, int maxVisited){
		ArrayList<Point> out = new ArrayList<Point>();
		
		int i = 0;
		
		while(maxVisited > 0 && i < path.size()){
			out.add(path.get(i));
			if(dirtyNodes.contains(path.get(i))){
				maxVisited--;
			}
			i++;
		}
		
		if(out.size() > 0)
			out.addAll(this.findPath(out.get(out.size()-1), start));
		
		return out;
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
	public ArrayList<Point> findCycle(Point start, ArrayList<Point> nodes) {
		SimpleWeightedGraph<Point, DefaultWeightedEdge> tempUnDirGraph = (SimpleWeightedGraph<Point, DefaultWeightedEdge>) this.unDirGraph
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

		List<Point> hm = HamiltonianCycle
				.getApproximateOptimalForCompleteGraph(tempUnDirGraph);

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
	 * To un dir graph.
	 * 
	 * @param in
	 *            the in
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
	 * @param toClose
	 *            the to close
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
