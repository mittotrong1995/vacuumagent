package tildeTeam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.HamiltonianCycle;
import org.jgrapht.alg.TransitiveClosure;
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

public class TestPath {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		testJgraphT();
	}
	
	public void testAstar(){
		VAFloor floor = new VAFloor(4);
		//floor.getFloor()[1][1].setStatus(VATileStatus.BLOCK);
		
		TildeFloorAStar floorAstart = new TildeFloorAStar(floor.getFloor());
		
		PathFinder finder = new AStarPathFinder(floorAstart, 10000, false);
		
		
		Path path = finder.findPath(new TildeMover(), 0, 0, 3, 3);
		
		
		for(int i = 0; i < path.getLength(); i++){
			System.out.print(path.getStep(i).getX());
			System.out.print("-");
			System.out.print(path.getStep(i).getY());
			System.out.println("");
		}
	}
	
	public static void testJgraphT(){
		SimpleWeightedGraph<Point, DefaultWeightedEdge> floor = new SimpleWeightedGraph<Point, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		floor.addVertex(new Point(0, 0));
		floor.addVertex(new Point(0, 1));
		//floor.addVertex(new Point(0, 2));
		floor.addVertex(new Point(1, 0));
		floor.addVertex(new Point(1, 2));
		floor.addVertex(new Point(2, 0));
		floor.addVertex(new Point(2, 1));
		floor.addVertex(new Point(2, 2));
		
		for(Point p: floor.vertexSet()){
			
		    int x = p.x;
		    int y = p.y;
			if (floor.containsVertex(new Point(x+1, y))){
				floor.addEdge(p,new Point(x+1, y));
				floor.setEdgeWeight(floor.getEdge(p,new Point(x+1, y)), 1);
			}
			if (floor.containsVertex(new Point(x-1, y))){
				floor.addEdge(p,new Point(x-1, y));
				floor.setEdgeWeight(floor.getEdge(p,new Point(x-1, y)), 1);
			}
			if (floor.containsVertex(new Point(x, y+1))){
				floor.addEdge(p,new Point(x, y+1));
				floor.setEdgeWeight(floor.getEdge(p,new Point(x, y+1)), 1);
			}
			if (floor.containsVertex(new Point(x, y-1))){
				floor.addEdge(p,new Point(x, y-1));
				floor.setEdgeWeight(floor.getEdge(p,new Point(x, y-1)), 1);
			}
			
			
		}
		
		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> floorBack = new SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		floorBack.addVertex(new Point(0, 0));
		floorBack.addVertex(new Point(0, 1));
		//floorBack.addVertex(new Point(0, 2));
		floorBack.addVertex(new Point(1, 0));
		floorBack.addVertex(new Point(1, 2));
		floorBack.addVertex(new Point(2, 0));
		floorBack.addVertex(new Point(2, 1));
		floorBack.addVertex(new Point(2, 2));
		
		for(Point p: floorBack.vertexSet()){
			
		    int x = p.x;
		    int y = p.y;
			if (floorBack.containsVertex(new Point(x+1, y))){
				floorBack.addEdge(p,new Point(x+1, y));
				floorBack.setEdgeWeight(floorBack.getEdge(p,new Point(x+1, y)), 1);
			}
			if (floorBack.containsVertex(new Point(x-1, y))){
				floorBack.addEdge(p,new Point(x-1, y));
				floorBack.setEdgeWeight(floorBack.getEdge(p,new Point(x-1, y)), 1);
			}
			if (floorBack.containsVertex(new Point(x, y+1))){
				floorBack.addEdge(p,new Point(x, y+1));
				floorBack.setEdgeWeight(floorBack.getEdge(p,new Point(x, y+1)), 1);
			}
			if (floorBack.containsVertex(new Point(x, y-1))){
				floorBack.addEdge(p,new Point(x, y-1));
				floorBack.setEdgeWeight(floorBack.getEdge(p,new Point(x, y-1)), 1);
			}
			
			
		}
		
		for(Point p1: floor.vertexSet()){
			for(Point p2: floor.vertexSet()){
				if(p1 != p2){
					DijkstraShortestPath<Point, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<Point, DefaultWeightedEdge>(floor, p1, p2);
					GraphPath<Point, DefaultWeightedEdge> path = pathfinder.getPath();
					floor.addEdge(p1,p2);
					floor.setEdgeWeight(floor.getEdge(p1,p2), path.getWeight());
				}
			}
		}
		
		floor.removeVertex(new Point(0, 1));
		//floor.removeVertex(new Point(0, 2));
		floor.removeVertex(new Point(1, 0));
		floor.removeVertex(new Point(2, 0));
		floor.removeVertex(new Point(2, 1));
		//floor.removeVertex(new Point(1, 2));
		
		
		
        DepthFirstIterator<Point, DefaultWeightedEdge> iter =
            new DepthFirstIterator<Point,DefaultWeightedEdge>(floor);
        Point vertex;
        while (iter.hasNext()) {
            vertex = iter.next();
            System.out.println(
                "Vertex " + vertex.toString() + " is connected to: "
                + floor.edgesOf(vertex).toString());
        }
        

		
        for(Point p: HamiltonianCycle.getApproximateOptimalForCompleteGraph(floor)){
        	System.out.println(p);
        }
        
        
        List<Point> hm = HamiltonianCycle.getApproximateOptimalForCompleteGraph(floor);
        
        int startIndex = hm.indexOf(new Point(0,0));
        
        ArrayList<Point> hmFromStart = new ArrayList<Point>();
        
        for(int i = startIndex; i < hm.size(); i++){
        	hmFromStart.add(hm.get(i));
        }
        for(int i = 0; i <= startIndex; i++){
        	hmFromStart.add(hm.get(i));
        }
        
        for(Point p: hmFromStart){
        	System.out.println(p);
        }
        
        
        
        System.out.println("BIULD FINAL PATHHHH");
        ArrayList<Point> finalPath = new ArrayList<Point>();
        for(int i = 0; i < hmFromStart.size()-1; i++){
        	
        	Point p1 = hmFromStart.get(i);
        	System.out.println(p1);
        	Point p2 = hmFromStart.get(i+1);
        	System.out.println(p2);
        	System.out.println("--------");
        	
        	
        	DijkstraShortestPath<Point, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<Point, DefaultWeightedEdge>(floorBack, p1, p2);
			GraphPath<Point, DefaultWeightedEdge> path = pathfinder.getPath();
        	List<DefaultWeightedEdge> edgeList = path.getEdgeList();
        	
        	ArrayList<Point> tempPath = new ArrayList<Point>();
        	for(int j = 0; j < edgeList.size(); j++)
        	{
        		DefaultWeightedEdge edge = edgeList.get(j);
        		tempPath.add(floorBack.getEdgeTarget(edge));
        		System.out.println(floorBack.getEdgeSource(edge));
        		System.out.println(floorBack.getEdgeTarget(edge));
        	}
        	System.out.println("--------");
        	finalPath.addAll(tempPath);
        }
        
        System.out.println("FINAL PATHHHH");
        for(Point p: finalPath){
        	System.out.println(p);
        }
        
        
	}

}
