/**
 * 
 */
package main.tildeTeam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;


import vacuumAgent.VANeighborhood;
import vacuumAgent.VATile.VATileStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class TLDInnerWorld.
 *
 * @author PsRob
 */
public class TLDInnerWorld {
	
	/** The floor. */
	private HashMap<Point , VATileStatus> floor;

	/**
	 * Instantiates a new tLD inner world.
	 */
	public TLDInnerWorld() {
		super();
		this.floor = new HashMap<Point, VATileStatus>();
	}

	/**
	 * Gets the floor.
	 *
	 * @return the floor
	 */
	public HashMap<Point, VATileStatus> getFloor() {
		return floor;
	}
	
	/**
	 * Sets the tile.
	 *
	 * @param t the t
	 * @param a the a
	 */
	public void setTile( Point t , VATileStatus a) {
		if(floor.containsKey(t))
			floor.remove(t);
		floor.put(t,a); 
	}
	
	/**
	 * Gets the tile.
	 *
	 * @param p the p
	 * @return the tile
	 */
	public VATileStatus getTile( Point p) {		
		if(!floor.containsKey(p)){
			return VATileStatus.UNDEFINED;
		}
		return floor.get(p); 
	}

	/**
	 * dato un punto vede se c'� vicolo cieco.
	 *
	 * @param p the p
	 * @return true, if successful
	 */
	public boolean deadEnd (Point p){
		int x =p.x;
		int y =p.y;
		if(this.getTile(new Point(x+1,y)) == VATileStatus.UNDEFINED || this.getTile(new Point(x+1,y)) == VATileStatus.DIRTY) return false;
		if(this.getTile(new Point(x-1,y)) == VATileStatus.UNDEFINED || this.getTile(new Point(x-1,y)) == VATileStatus.DIRTY) return false;
		if(this.getTile(new Point(x,y-1)) == VATileStatus.UNDEFINED || this.getTile(new Point(x,y-1)) == VATileStatus.DIRTY) return false;
		if(this.getTile(new Point(x,y+1)) == VATileStatus.UNDEFINED || this.getTile(new Point(x,y+1)) == VATileStatus.DIRTY) return false;
		return true;
	}
	
	/**
	 * Visited.
	 *
	 * @param p the p
	 * @return true, if successful
	 */
	public boolean visited (Point p){
		if (this.getTile(p) == VATileStatus.UNDEFINED || this.getTile(p) == VATileStatus.DIRTY) return false;
		return true;
	}
	
	public void updateWorld ( Point currentPosition, VANeighborhood neighborhood){
		this.setTile(currentPosition, VATileStatus.CLEAN);
		
		Point point = new Point(currentPosition.x+1, currentPosition.y);
		if(!neighborhood.northIsFree()){
			this.setTile(point, VATileStatus.BLOCK);
		}
		else{
			if(getTile(point) == VATileStatus.UNDEFINED)
				this.setTile(point, VATileStatus.DIRTY);
		}
		
		point = new Point(currentPosition.x-1, currentPosition.y);
		if(!neighborhood.southIsFree()){
			this.setTile(point, VATileStatus.BLOCK);
		}
		else{
			if(getTile(point) == VATileStatus.UNDEFINED)
				this.setTile(point, VATileStatus.DIRTY);
		}
		
		point = new Point(currentPosition.x, currentPosition.y+1);
		if(!neighborhood.eastIsFree()){
			this.setTile(point, VATileStatus.BLOCK);
		}
		else{
			if(getTile(point) == VATileStatus.UNDEFINED)
				this.setTile(point, VATileStatus.DIRTY);
		}
		
		point = new Point(currentPosition.x, currentPosition.y-1);
		if(!neighborhood.westIsFree()){
			this.setTile(point, VATileStatus.BLOCK);
		}
		else{
			if(getTile(point) == VATileStatus.UNDEFINED)
				this.setTile(point, VATileStatus.DIRTY);
		}
	}
	
	public ArrayList<Point> findPath (Point p1,Point p2){
		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph = TLDConvertToGraph.toGraphInnerWorld(this);
		List<DefaultWeightedEdge> edgeList = DijkstraShortestPath.findPathBetween(graph, p1, p2);
		
		ArrayList<Point> tempPath = new ArrayList<Point>();
		for (int j = 0; j < edgeList.size(); j++) {
			DefaultWeightedEdge edge = edgeList.get(j);
			tempPath.add(graph.getEdgeTarget(edge));
		}
		return tempPath;		
	}
	
	@Override
	public String toString() {
		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph = TLDConvertToGraph.toGraphInnerWorld(this);
		String out = "";
		DepthFirstIterator<Point, DefaultWeightedEdge> iter = new DepthFirstIterator<Point, DefaultWeightedEdge>(
				graph);
		Point vertex;
		while (iter.hasNext()) {
			vertex = iter.next();
			out += "Vertex " + vertex.toString()
					+ " is connected to: "
					+ graph.edgesOf(vertex).toString()+"\n";
		}
		return out;
	}

	
	
	

}
