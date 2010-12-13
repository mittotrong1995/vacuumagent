package tildeTeam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.HamiltonianCycle;
import org.jgrapht.alg.TransitiveClosure;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFinder;

import vacuumAgent.VAFloor;
import vacuumAgent.VATile;
import vacuumAgent.VATile.VATileStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class TestPath.
 */
public class TestPath {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		testJgraphT2();

		testVaflor();

	}

	/**
	 * Test astar.
	 */
	public void testAstar() {
		VAFloor floor = new VAFloor(4);
		// floor.getFloor()[1][1].setStatus(VATileStatus.BLOCK);

		TildeFloorAStar floorAstart = new TildeFloorAStar(floor.getFloor());

		PathFinder finder = new AStarPathFinder(floorAstart, 10000, false);

		Path path = finder.findPath(new TildeMover(), 0, 0, 3, 3);

		for (int i = 0; i < path.getLength(); i++) {
			System.out.print(path.getStep(i).getX());
			System.out.print("-");
			System.out.print(path.getStep(i).getY());
			System.out.println("");
		}
	}

	/**
	 * Test vaflor.
	 */
	public static void testVaflor() {

		VAFloor floor = new VAFloor(5);

		ArrayList<Point> dirtyNodes = new ArrayList<Point>();

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {

				if (		i == 3 && j == 0
						|| i == 2 && j == 1
						|| i == 3 && j == 3
						|| i == 2 && j == 3
						|| i == 1 && j == 3
						|| i == 0&& j == 3)
					floor.getTile(new Point(i, j))
							.setStatus(VATileStatus.BLOCK);
				else if (i == 0 && j == 0 || i == 1 && j == 2 || i == 1
						&& j == 4) {

					floor.getTile(new Point(i, j))
							.setStatus(VATileStatus.DIRTY);

					dirtyNodes.add(new Point(i, j));/// dirtyNodes
					
					
				} else
					floor.getTile(new Point(i, j))
							.setStatus(VATileStatus.CLEAN);
			}
		}

		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> floorDir = TLDConvertToGraph
				.toGraph(floor);

		// //

		TLDPathFinder pathFinder = new TLDPathFinder(floorDir);

		ArrayList<Point> finalPath = pathFinder
				.findCycle(new Point(4,0), dirtyNodes);

		System.out.println("FINAL PATHHHHYOOYYOYOYOOY");
		for (Point p : finalPath) {
			System.out.println(p);
		}

	}

	/**
	 * Test jgraph t2.
	 */
	public static void testJgraphT2() {
		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> floorDir = new SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		floorDir.addVertex(new Point(1, 1));
		floorDir.addVertex(new Point(1, 2));
		floorDir.addVertex(new Point(1, 3));
		floorDir.addVertex(new Point(1, 5));
		floorDir.addVertex(new Point(2, 1));
		floorDir.addVertex(new Point(2, 2));
		floorDir.addVertex(new Point(2, 3));
		floorDir.addVertex(new Point(2, 5));
		floorDir.addVertex(new Point(3, 1));
		floorDir.addVertex(new Point(3, 3));
		floorDir.addVertex(new Point(3, 5));
		floorDir.addVertex(new Point(4, 2));
		floorDir.addVertex(new Point(4, 3));
		floorDir.addVertex(new Point(4, 5));
		floorDir.addVertex(new Point(5, 1));
		floorDir.addVertex(new Point(5, 2));
		floorDir.addVertex(new Point(5, 3));
		floorDir.addVertex(new Point(5, 4));
		floorDir.addVertex(new Point(5, 5));

		for (Point p : floorDir.vertexSet()) {

			int x = p.x;
			int y = p.y;
			if (floorDir.containsVertex(new Point(x + 1, y))) {
				floorDir.addEdge(p, new Point(x + 1, y));
				floorDir.setEdgeWeight(
						floorDir.getEdge(p, new Point(x + 1, y)), 1);
			}
			if (floorDir.containsVertex(new Point(x - 1, y))) {
				floorDir.addEdge(p, new Point(x - 1, y));
				floorDir.setEdgeWeight(
						floorDir.getEdge(p, new Point(x - 1, y)), 1);
			}
			if (floorDir.containsVertex(new Point(x, y + 1))) {
				floorDir.addEdge(p, new Point(x, y + 1));
				floorDir.setEdgeWeight(
						floorDir.getEdge(p, new Point(x, y + 1)), 1);
			}
			if (floorDir.containsVertex(new Point(x, y - 1))) {
				floorDir.addEdge(p, new Point(x, y - 1));
				floorDir.setEdgeWeight(
						floorDir.getEdge(p, new Point(x, y - 1)), 1);
			}

		}

		TLDPathFinder pathFinder = new TLDPathFinder(floorDir);

		ArrayList<Point> nodes = new ArrayList<Point>();
		nodes.add(new Point(3, 1));
		nodes.add(new Point(3, 3));
		nodes.add(new Point(5, 3));
		nodes.add(new Point(2, 5));
		nodes.add(new Point(1, 1));
		ArrayList<Point> finalPath = pathFinder
				.findCycle(new Point(5, 1), nodes);

		System.out.println("FINAL PATHHHH");
		for (Point p : finalPath) {
			System.out.println(p);
		}

	}

	/**
	 * Test jgraph t.
	 */
	public static void testJgraphT() {
		SimpleWeightedGraph<Point, DefaultWeightedEdge> floorUndir = new SimpleWeightedGraph<Point, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		floorUndir.addVertex(new Point(1, 1));
		floorUndir.addVertex(new Point(1, 2));
		floorUndir.addVertex(new Point(1, 3));
		floorUndir.addVertex(new Point(1, 5));
		floorUndir.addVertex(new Point(2, 1));
		floorUndir.addVertex(new Point(2, 2));
		floorUndir.addVertex(new Point(2, 3));
		floorUndir.addVertex(new Point(2, 5));
		floorUndir.addVertex(new Point(3, 1));
		floorUndir.addVertex(new Point(3, 3));
		floorUndir.addVertex(new Point(3, 5));
		floorUndir.addVertex(new Point(4, 2));
		floorUndir.addVertex(new Point(4, 3));
		floorUndir.addVertex(new Point(4, 5));
		floorUndir.addVertex(new Point(5, 1));
		floorUndir.addVertex(new Point(5, 2));
		floorUndir.addVertex(new Point(5, 3));
		floorUndir.addVertex(new Point(5, 4));
		floorUndir.addVertex(new Point(5, 5));

		for (Point p : floorUndir.vertexSet()) {

			int x = p.x;
			int y = p.y;
			if (floorUndir.containsVertex(new Point(x + 1, y))) {
				floorUndir.addEdge(p, new Point(x + 1, y));
				floorUndir.setEdgeWeight(
						floorUndir.getEdge(p, new Point(x + 1, y)), 1);
			}
			if (floorUndir.containsVertex(new Point(x - 1, y))) {
				floorUndir.addEdge(p, new Point(x - 1, y));
				floorUndir.setEdgeWeight(
						floorUndir.getEdge(p, new Point(x - 1, y)), 1);
			}
			if (floorUndir.containsVertex(new Point(x, y + 1))) {
				floorUndir.addEdge(p, new Point(x, y + 1));
				floorUndir.setEdgeWeight(
						floorUndir.getEdge(p, new Point(x, y + 1)), 1);
			}
			if (floorUndir.containsVertex(new Point(x, y - 1))) {
				floorUndir.addEdge(p, new Point(x, y - 1));
				floorUndir.setEdgeWeight(
						floorUndir.getEdge(p, new Point(x, y - 1)), 1);
			}

		}

		for (Point p1 : floorUndir.vertexSet()) {
			for (Point p2 : floorUndir.vertexSet()) {
				if (p1 != p2) {
					DijkstraShortestPath<Point, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<Point, DefaultWeightedEdge>(
							floorUndir, p1, p2);
					GraphPath<Point, DefaultWeightedEdge> path = pathfinder
							.getPath();
					floorUndir.addEdge(p1, p2);
					floorUndir.setEdgeWeight(floorUndir.getEdge(p1, p2),
							path.getWeight());
				}
			}
		}

		floorUndir.removeVertex(new Point(1, 2));
		floorUndir.removeVertex(new Point(1, 5));
		floorUndir.removeVertex(new Point(2, 1));
		floorUndir.removeVertex(new Point(2, 2));
		floorUndir.removeVertex(new Point(2, 3));
		floorUndir.removeVertex(new Point(3, 5));
		floorUndir.removeVertex(new Point(4, 2));
		floorUndir.removeVertex(new Point(4, 3));
		floorUndir.removeVertex(new Point(4, 5));
		floorUndir.removeVertex(new Point(5, 2));
		floorUndir.removeVertex(new Point(5, 3));
		floorUndir.removeVertex(new Point(5, 4));
		floorUndir.removeVertex(new Point(5, 5));

//		DepthFirstIterator<Point, DefaultWeightedEdge> iter = new DepthFirstIterator<Point, DefaultWeightedEdge>(
//				floorUndir);
//		Point vertex;
//		while (iter.hasNext()) {
//			vertex = iter.next();
//			System.out.println("Vertex " + vertex.toString()
//					+ " is connected to: "
//					+ floorUndir.edgesOf(vertex).toString());
//		}

		for (Point p : HamiltonianCycle
				.getApproximateOptimalForCompleteGraph(floorUndir)) {
			System.out.println(p);
		}

		List<Point> hm = HamiltonianCycle
				.getApproximateOptimalForCompleteGraph(floorUndir);

		int startIndex = hm.indexOf(new Point(5, 1));

		ArrayList<Point> hmFromStart = new ArrayList<Point>();

		for (int i = startIndex; i < hm.size(); i++) {
			hmFromStart.add(hm.get(i));
		}
		for (int i = 0; i <= startIndex; i++) {
			hmFromStart.add(hm.get(i));
		}

		for (Point p : hmFromStart) {
			System.out.println(p);
		}

		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> floorDir = new SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		floorDir.addVertex(new Point(1, 1));
		floorDir.addVertex(new Point(1, 2));
		floorDir.addVertex(new Point(1, 3));
		floorDir.addVertex(new Point(1, 5));
		floorDir.addVertex(new Point(2, 1));
		floorDir.addVertex(new Point(2, 2));
		floorDir.addVertex(new Point(2, 3));
		floorDir.addVertex(new Point(2, 5));
		floorDir.addVertex(new Point(3, 1));
		floorDir.addVertex(new Point(3, 3));
		floorDir.addVertex(new Point(3, 5));
		floorDir.addVertex(new Point(4, 2));
		floorDir.addVertex(new Point(4, 3));
		floorDir.addVertex(new Point(4, 5));
		floorDir.addVertex(new Point(5, 1));
		floorDir.addVertex(new Point(5, 2));
		floorDir.addVertex(new Point(5, 3));
		floorDir.addVertex(new Point(5, 4));
		floorDir.addVertex(new Point(5, 5));

		for (Point p : floorDir.vertexSet()) {

			int x = p.x;
			int y = p.y;
			if (floorDir.containsVertex(new Point(x + 1, y))) {
				floorDir.addEdge(p, new Point(x + 1, y));
				floorDir.setEdgeWeight(
						floorDir.getEdge(p, new Point(x + 1, y)), 1);
			}
			if (floorDir.containsVertex(new Point(x - 1, y))) {
				floorDir.addEdge(p, new Point(x - 1, y));
				floorDir.setEdgeWeight(
						floorDir.getEdge(p, new Point(x - 1, y)), 1);
			}
			if (floorDir.containsVertex(new Point(x, y + 1))) {
				floorDir.addEdge(p, new Point(x, y + 1));
				floorDir.setEdgeWeight(
						floorDir.getEdge(p, new Point(x, y + 1)), 1);
			}
			if (floorDir.containsVertex(new Point(x, y - 1))) {
				floorDir.addEdge(p, new Point(x, y - 1));
				floorDir.setEdgeWeight(
						floorDir.getEdge(p, new Point(x, y - 1)), 1);
			}

		}

		System.out.println("BIULD FINAL PATHHHH");
		ArrayList<Point> finalPath = new ArrayList<Point>();
		for (int i = 0; i < hmFromStart.size() - 1; i++) {

			Point p1 = hmFromStart.get(i);
			System.out.println(p1);
			Point p2 = hmFromStart.get(i + 1);
			System.out.println(p2);
			System.out.println("--------");

			DijkstraShortestPath<Point, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<Point, DefaultWeightedEdge>(
					floorDir, p1, p2);
			GraphPath<Point, DefaultWeightedEdge> path = pathfinder.getPath();
			List<DefaultWeightedEdge> edgeList = path.getEdgeList();

			ArrayList<Point> tempPath = new ArrayList<Point>();
			for (int j = 0; j < edgeList.size(); j++) {
				DefaultWeightedEdge edge = edgeList.get(j);
				tempPath.add(floorDir.getEdgeTarget(edge));
				System.out.println(floorDir.getEdgeSource(edge));
				System.out.println(floorDir.getEdgeTarget(edge));
			}
			System.out.println("--------");
			finalPath.addAll(tempPath);
		}

		System.out.println("FINAL PATHHHH");
		for (Point p : finalPath) {
			System.out.println(p);
		}

	}

}
