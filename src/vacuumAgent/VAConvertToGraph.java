package vacuumAgent;

import java.awt.Point;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import vacuumAgent.VAFloor;
import vacuumAgent.VATile.VATileStatus;

public class VAConvertToGraph {
	public static SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> toGraph(VAFloor floor){
		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		int size = floor.getSize();
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
			{
				Point p = new Point(i,j);
				VATileStatus status = floor.getTile(p).getStatus();
				if(status == VATileStatus.CLEAN || status == VATileStatus.DIRTY)
				{
					graph.addVertex(p);
				}
			}
		
		for(Point p: graph.vertexSet()){
			
		    int x = p.x;
		    int y = p.y;
			if (graph.containsVertex(new Point(x+1, y))){
				graph.addEdge(p,new Point(x+1, y));
				graph.setEdgeWeight(graph.getEdge(p,new Point(x+1, y)), 1);
			}
			if (graph.containsVertex(new Point(x-1, y))){
				graph.addEdge(p,new Point(x-1, y));
				graph.setEdgeWeight(graph.getEdge(p,new Point(x-1, y)), 1);
			}
			if (graph.containsVertex(new Point(x, y+1))){
				graph.addEdge(p,new Point(x, y+1));
				graph.setEdgeWeight(graph.getEdge(p,new Point(x, y+1)), 1);
			}
			if (graph.containsVertex(new Point(x, y-1))){
				graph.addEdge(p,new Point(x, y-1));
				graph.setEdgeWeight(graph.getEdge(p,new Point(x, y-1)), 1);
			}
			
			
		}
		return graph;
	}
}
